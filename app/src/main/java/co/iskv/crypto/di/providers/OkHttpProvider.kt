package co.iskv.crypto.di.providers

import co.iskv.crypto.BuildConfig
import co.iskv.crypto.data.remote.interceptor.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider

class OkHttpProvider @Inject constructor(
    private val authInterceptor: AuthInterceptor,
) : Provider<OkHttpClient> {

    override fun get(): OkHttpClient =
        OkHttpClient.Builder().apply {
            connectTimeout(40, TimeUnit.SECONDS)
            readTimeout(40, TimeUnit.SECONDS)
            addInterceptor(authInterceptor)
            if (BuildConfig.DEBUG) addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }.build()

}