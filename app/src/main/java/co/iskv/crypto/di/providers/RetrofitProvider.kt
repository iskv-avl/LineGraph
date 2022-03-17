package co.iskv.crypto.di.providers

import co.iskv.crypto.di.ServerPath
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Provider

class RetrofitProvider @Inject constructor(
    private val gson: Gson,
    private val okHttpClient: OkHttpClient,
    @ServerPath private val serverPath: String,
) : Provider<Retrofit> {

    override fun get(): Retrofit {
        val builder = GsonBuilder().apply { serializeNulls() }
        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .baseUrl(serverPath)
            .build()
    }

}