package co.iskv.crypto.data.remote.interceptor

import co.iskv.crypto.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(
            chain
                .request()
                .newBuilder()
                .apply {
                    addHeader("X-CoinAPI-Key", BuildConfig.COINAPI_TOKEN)
                }
                .build()
        )

}