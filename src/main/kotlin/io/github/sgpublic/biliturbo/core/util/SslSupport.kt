package io.github.sgpublic.biliturbo.core.util

import io.github.sgpublic.biliturbo.module.ApiModule
import io.netty.buffer.ByteBufAllocator
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.SslHandler
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.asn1.x509.Extension
import org.bouncycastle.asn1.x509.GeneralName
import org.bouncycastle.asn1.x509.GeneralNames
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import java.io.File
import java.io.InputStream
import java.math.BigInteger
import java.security.*
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.security.spec.EncodedKeySpec
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*
import kotlin.random.Random

object SslSupport {
    val certificate: X509Certificate
    private val privateKey: PrivateKey
    private val issuer: String
    private val keyPair: KeyPair

    init {
        val loader = Thread.currentThread().contextClassLoader
        certificate = readCertificate(loader)
        privateKey = readPrivateKey(loader)
        issuer = certificate.issuerX500Principal.toString()
            .split(", ").reversed().joinToString(", ")
        keyPair = genKeyPair()
    }

    private val clientSslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build()
    fun newClientSslHandler(allocator: ByteBufAllocator, request: HostPort): SslHandler {
        return clientSslCtx.newHandler(allocator, request.hostName, request.port)
    }

    fun newServerSslHandler(allocator: ByteBufAllocator, hostName: String): SslHandler {
        val serverSslCtx = SslContextBuilder.forServer(keyPair.private, getCert(hostName)).build()
        return serverSslCtx.newHandler(allocator)
    }

    private fun readCertificate(loader: ClassLoader): X509Certificate {
        val crtPath = "cert/biliturbo.crt"
        val crt: InputStream = File(crtPath)
            .takeIf { it.fileExist() }?.inputStream()
            ?: loader.getResourceAsStream(crtPath)!!
        val cf = CertificateFactory.getInstance("X.509")
        return cf.generateCertificate(crt) as X509Certificate
    }

    private fun readPrivateKey(loader: ClassLoader): PrivateKey {
        val derPath = "cert/biliturbo_private.der"
        val der: InputStream = File(derPath)
            .takeIf { it.fileExist() }?.inputStream()
            ?: loader.getResourceAsStream(derPath)!!
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
    private val random = Random.Default
    private fun getCert(host: String): X509Certificate {
        val hostname = host.trim().lowercase(Locale.getDefault())
        certCache[hostname]?.let {
            return it
        }
        val subject = "C=CN, ST=SC, L=CD, O=BiliTurbo, OU=study, CN=${hostname}"
        val serial = BigInteger(ApiModule.TS_FULL_STR + random.nextInt(1000, 10000))
        val jv3Builder = JcaX509v3CertificateBuilder(
            X500Name(issuer), serial, certificate.notBefore,
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
}