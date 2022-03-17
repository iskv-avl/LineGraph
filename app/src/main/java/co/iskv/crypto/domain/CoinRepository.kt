package co.iskv.crypto.domain

import co.iskv.crypto.domain.entites.CoinDetailsEntity
import co.iskv.crypto.domain.entites.CoinIconEntity
import co.iskv.crypto.widget.models.Period
import io.reactivex.rxjava3.core.Single

interface CoinRepository {

    fun getCoinIcons(iconSize: Int): Single<List<CoinIconEntity>>

    fun getCoinDetails(
        exchangeId: String,
        period: Period,
        timeEnd: String,
        limit: Int
    ): Single<List<CoinDetailsEntity>>

}