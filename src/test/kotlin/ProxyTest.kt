
import io.github.sgpublic.biliturbo.core.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import java.net.InetSocketAddress
import java.net.Proxy
import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProxyTest {
    @BeforeAll
    fun startProxy() {
//        BiliTurboService.start()
        Thread.sleep(2000)
    }

    @Test
    fun checkProxy() {
        val client = OkHttpClient.Builder()
            .proxy(Proxy(Proxy.Type.HTTP, InetSocketAddress("127.0.0.1", 23333)))
            .build()
        val request = Request.Builder()
            .url("https://www.baidu.com/favicon.ico")
            .build()
        val response = client.newCall(request).execute()
        Log.d("code: ${response.code}, message: ${response.message}")
    }

    @AfterAll
    fun stopProxy() {
//        BiliTurboService.stop()
    }
}