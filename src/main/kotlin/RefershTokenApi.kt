import io.reactivex.Observable
import okhttp3.Credentials
import java.util.concurrent.TimeUnit

// simulate refresh token api
fun refreshToken(): Observable<String> {
  return Observable.just(Credentials.basic("jesse", "password1"))
      .doOnNext { println("GET credential: $it") }
      .delay(2000, TimeUnit.MILLISECONDS)
}
