package co.iskv.crypto.di.module

import co.iskv.crypto.data.remote.CoinDataRepository
import co.iskv.crypto.domain.CoinRepository
import toothpick.config.Module

class RepositoryModule : Module() {

    init {
        bind(CoinRepository::class.java).to(CoinDataRepository::class.java)
    }

}