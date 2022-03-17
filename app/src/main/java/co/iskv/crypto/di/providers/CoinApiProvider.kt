package co.iskv.crypto.di.providers

import co.iskv.crypto.data.remote.api.CoinApi
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Provider

class CoinApiProvider @Inject constructor(
    private val retrofit: Retrofit
) : Provider<CoinApi> {

    override fun get(): CoinApi = retrofit.create(CoinApi::class.java)

}