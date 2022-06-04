
import io.github.sgpublic.biliturbo.core.netty.BiliTurboService
import io.github.sgpublic.biliturbo.core.util.Log
import io.github.sgpublic.biliturbo.core.util.SslSupport
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import java.net.InetSocketAddress
import java.net.Proxy
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProxyTest {
    private val sc = SSLContext.getInstance("TLS")
    private val tm = object : X509TrustManager {
        override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) { }

        override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) { }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf(SslSupport.certificate)
        }
    }

    @BeforeAll
    fun startProxy() {
        sc.init(null, arrayOf<TrustManager>(tm), SecureRandom())
        BiliTurboService.start()
        Thread.sleep(2000)
    }

    @Test
    fun checkProxy() {
        val client = OkHttpClient.Builder()
            .proxy(Proxy(Proxy.Type.HTTP, InetSocketAddress("127.0.0.1", 23333)))
            .sslSocketFactory(sc.socketFactory, tm)
            .build()
        val request = Request.Builder()
            .url("https://www.baidu.com/")
            .build()
        val response = client.newCall(request).execute()
        Log.d("code: ${response.code}, message: ${response.message}")
    }

    @AfterAll
    fun stopProxy() {
        BiliTurboService.stop()
    }
}