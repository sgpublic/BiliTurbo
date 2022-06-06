package io.github.sgpublic.biliturbo.core.util

import io.github.sgpublic.biliturbo.module.ApiModule
import io.netty.buffer.ByteBufAllocator
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.SslHandler
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import okio.FileNotFoundException
import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.asn1.x509.BasicConstraints
import org.bouncycastle.asn1.x509.Extension
import org.bouncycastle.asn1.x509.GeneralName
import org.bouncycastle.asn1.x509.GeneralNames
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import java.io.File
import java.io.InputStream
import java.security.*
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.security.spec.EncodedKeySpec
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*
import java.util.concurrent.TimeUnit

object SslSupport {
    private lateinit var certificate: X509Certificate
    val CERTIFICATE: X509Certificate get() = certificate
    private lateinit var privateKey: PrivateKey
    private lateinit var issuer: String
    private lateinit var keyPair: KeyPair

    private val clientSslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build()
    fun newClientSslHandler(allocator: ByteBufAllocator, request: HostPort): SslHandler {
        return clientSslCtx.newHandler(allocator, request.hostName, request.port)
    }

    fun newServerSslHandler(allocator: ByteBufAllocator, hostName: String): SslHandler {
        val serverSslCtx = SslContextBuilder.forServer(keyPair.private, getCert(hostName)).build()
        return serverSslCtx.newHandler(allocator)
    }

    fun init() {
        try {
            certificate = readCertificate()
            privateKey = readPrivateKey()
            issuer = certificate.issuerX500Principal.toString()
                .split(", ").reversed().joinToString(", ")
            keyPair = genKeyPair()
        } catch (e: java.lang.Exception) {
            throw Exception(e)
        }
    }

    private fun readCertificate(): X509Certificate {
        val crtPath = "cert/certificate.crt"
        val crt: InputStream = File(crtPath)
            .takeIf { it.fileExist() }?.inputStream()
            ?: throw FileNotFoundException()
        val cf = CertificateFactory.getInstance("X.509")
        return cf.generateCertificate(crt) as X509Certificate
    }

    private fun readPrivateKey(): PrivateKey {
        val derPath = "cert/private.der"
        val der: InputStream = File(derPath)
            .takeIf { it.fileExist() }?.inputStream()
            ?: throw FileNotFoundException()
        val factory = KeyFactory.getInstance("RSA")
        val spec = PKCS8EncodedKeySpec(der.readAllBytes())
        return factory.generatePrivate(spec as EncodedKeySpec)
    }

    private fun genKeyPair(): KeyPair {
        Security.addProvider(BouncyCastleProvider())
        val caKeyPairGen = KeyPairGenerator.getInstance("RSA", "BC")
        caKeyPairGen.initialize(2048, SecureRandom())
        return caKeyPairGen.genKeyPair()
    }

    private val certCache = HashMap<String, X509Certificate>()
    private fun getCert(host: String): X509Certificate {
        val hostname = host.trim().lowercase(Locale.getDefault())
        certCache[hostname]?.let {
            return it
        }
        val subject = "C=CN, ST=SC, L=CD, O=BiliTurbo, OU=study, CN=${hostname}"
        val jv3Builder = JcaX509v3CertificateBuilder(
            X500Name(issuer), ApiModule.RANDOM_TS, certificate.notBefore,
            certificate.notAfter, X500Name(subject), keyPair.public
        )
        val names = GeneralNames(GeneralName(GeneralName.dNSName, hostname))
        jv3Builder.addExtension(Extension.subjectAlternativeName, false, names)
        val signer = JcaContentSignerBuilder("SHA256WithRSAEncryption").build(privateKey)
        val cert = JcaX509CertificateConverter().getCertificate(jv3Builder.build(signer))
        certCache[hostname] = cert
        return cert
    }

    fun clearCertCaches() {
        certCache.clear()
    }

    fun createCert(
        crt: File = File("cert/certificate.crt"),
        der: File = File("cert/private.der"),
    ): File {
        val keyPair = genKeyPair()

        val subject = "C=CN, ST=SC, L=CD, O=BiliTurbo, OU=study, CN=BiliTurbo"
        val builder = JcaX509v3CertificateBuilder(
            X500Name(subject), ApiModule.RANDOM_TS, Date(),
            Date(ApiModule.TS_FULL + TimeUnit.SECONDS.toMillis(3650)),
            X500Name(subject),keyPair.public
        )
        builder.addExtension(Extension.basicConstraints, true, BasicConstraints(0))
        val signer = JcaContentSignerBuilder("SHA256WithRSAEncryption")
            .build(keyPair.private)
        val cert = JcaX509CertificateConverter()
            .getCertificate(builder.build(signer))
        crt.recreate()
        crt.writeBytes(cert.encoded)

        val pri = PKCS8EncodedKeySpec(keyPair.private.encoded)
        der.recreate()
        der.writeBytes(pri.encoded)

        return crt
    }

    class Exception(e: kotlin.Exception): kotlin.Exception("CertificateException", e)
}