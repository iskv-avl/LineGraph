package co.iskv.crypto.di.module

import co.iskv.crypto.data.remote.api.CoinApi
import co.iskv.crypto.di.providers.CoinApiProvider
import toothpick.config.Module

class ApiModule: Module() {

    init {
        bind(CoinApi::class.java).toProvider(CoinApiProvider::class.java)
    }

}