import okhttp3.*
import java.io.IOException

object OkHttpAuthenticator2Pattern {

  private val authenticator: Authenticator = object : Authenticator {

    private val tokenStream = refreshToken().publish().refCount()

    override fun authenticate(route: Route, response: Response): Request? {
      if (response.request().header("Authorization") != null) {
        return null
      }

      val credential = tokenStream.blockingFirst()
      return response.request().newBuilder()
          .header("Authorization", credential)
          .build()
    }
  }

  private val client: OkHttpClient = OkHttpClient.Builder()
      .authenticator(authenticator)
      .build()

  private fun doAsyncRequest() {
    val request = Request.Builder()
        .url("http://publicobject.com/secrets/hellosecret.txt")
        .build()
    client.newCall(request).enqueue(object : Callback {
      override fun onFailure(call: Call, e: IOException) {
        TODO("not implemented")
      }

      override fun onResponse(call: Call, response: Response) {
        println("result status code: ${response.code()}")
      }
    })
  }

  fun run() {
    // call `one` refresh token
    doAsyncRequest()
    doAsyncRequest()
    doAsyncRequest()

    // call `one` refresh token
    Thread.sleep(3000)
    doAsyncRequest()
    doAsyncRequest()
  }
}
