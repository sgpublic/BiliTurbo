package io.github.sgpublic.biliturbo.core.util

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
import java.math.BigInteger
import java.net.InetSocketAddress
import java.security.*
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*
import kotlin.random.Random
import kotlin.random.asJavaRandom

object SslSupport {
    private val crt: X509Certificate
    private val der: PrivateKey
    private val issuer: String
    private val keyPair: KeyPair
    init {
        val loader = Thread.currentThread().contextClassLoader
        crt = readCrt(loader)
        der = readDer(loader)
        issuer = crt.issuerX500Principal.toString().split(", ")
            .reversed().joinToString(", ")
        keyPair = genKeyPair()
    }

    private val clientSslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build()
    fun newClientSslHandler(allocator: ByteBufAllocator, request: InetSocketAddress): SslHandler {
        return clientSslCtx.newHandler(allocator, request.hostName, request.port)
    }

    fun newServerSslHandler(allocator: ByteBufAllocator, hostName: String): SslHandler {
        val serverSslCtx = SslContextBuilder.forServer(keyPair.private, getCert(hostName)).build()
        return serverSslCtx.newHandler(allocator)
    }

    private fun readCrt(loader: ClassLoader): X509Certificate {
        val crtPath = "cert/biliturbo.crt"
        val crt = File(crtPath)
            .takeIf { it.fileExist() }?.inputStream()
            ?: loader.getResourceAsStream(crtPath)
        val cf = CertificateFactory.getInstance("X.509")
        return cf.generateCertificate(crt) as X509Certificate
    }

    private fun readDer(loader: ClassLoader): PrivateKey {
        val derPath = "cert/biliturbo_private.der"
        val der = File(derPath)
            .takeIf { it.fileExist() }?.inputStream()
            ?: loader.getResourceAsStream(derPath)
        val factory = KeyFactory.getInstance("RSA")
        val spec = PKCS8EncodedKeySpec(der.readAllBytes())
        return factory.generatePrivate(spec)
    }

    private fun genKeyPair(): KeyPair {
        Security.addProvider(BouncyCastleProvider())
        val caKeyPairGen = KeyPairGenerator.getInstance("RSA", "BC")
        caKeyPairGen.initialize(2048, SecureRandom())
        return caKeyPairGen.genKeyPair()
    }

    private val certCache = HashMap<String, X509Certificate>()
    private val random = Random.asJavaRandom()
    private fun getCert(host: String): X509Certificate {
        val hostname = host.trim().lowercase(Locale.getDefault())
        Log.d("get cert for $hostname")
        certCache[hostname]?.let {
            Log.d("find cert cache for $hostname")
            return it
        }
        val subject = "C=CN, ST=SC, L=CD, O=BiliTurbo, OU=study, CN=${hostname}"
        val id = BigInteger(System.currentTimeMillis().toString() + random.nextLong(1000, 9999), 10)
        Log.d("create cert for $hostname: (id = ${id.toString(10)})")
        val jv3Builder = JcaX509v3CertificateBuilder(
            X500Name(issuer), id, crt.notBefore, crt.notAfter,
            X500Name(subject), keyPair.public
        )
        val names = GeneralNames(GeneralName(GeneralName.dNSName, hostname))
        jv3Builder.addExtension(Extension.subjectAlternativeName, false, names)
        val signer = JcaContentSignerBuilder("SHA256WithRSAEncryption").build(keyPair.private)
        val cert = JcaX509CertificateConverter().getCertificate(jv3Builder.build(signer))
        certCache[hostname] = cert
        return cert
    }

    fun clearCertCaches() {
        certCache.clear()
    }
}