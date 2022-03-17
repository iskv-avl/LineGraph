package co.iskv.crypto.di.module

import co.iskv.crypto.di.ServerPath
import co.iskv.crypto.di.providers.OkHttpProvider
import co.iskv.crypto.di.providers.RetrofitProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import toothpick.config.Module

class NetworkModule(
    serverPath: String
) : Module() {

    init {
        bind(String::class.java).withName(ServerPath::class.java).toInstance(serverPath)

        bind(Retrofit::class.java).toProvider(RetrofitProvider::class.java).singleton()
        bind(OkHttpClient::class.java).toProvider(OkHttpProvider::class.java).singleton()
        bind(Gson::class.java).toInstance(GsonBuilder().create())
    }

}