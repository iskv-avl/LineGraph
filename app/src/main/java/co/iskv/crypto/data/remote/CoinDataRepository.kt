package co.iskv.crypto.data.remote

import co.iskv.crypto.data.remote.api.CoinApi
import co.iskv.crypto.data.remote.mapper.CoinDetailsEntityMapper
import co.iskv.crypto.data.remote.mapper.CoinIconEntityMapper
import co.iskv.crypto.domain.CoinRepository
import co.iskv.crypto.domain.entites.CoinDetailsEntity
import co.iskv.crypto.domain.entites.CoinIconEntity
import co.iskv.crypto.widget.models.Period
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class CoinDataRepository @Inject constructor(
    private val coinApi: CoinApi,
    private val coinIconEntityMapper: CoinIconEntityMapper,
    private val coinDetailsEntityMapper: CoinDetailsEntityMapper
) : CoinRepository {

    override fun getCoinIcons(iconSize: Int): Single<List<CoinIconEntity>> {
        return coinApi.getIcons(iconSize).map { it.map { coinIconEntityMapper.map(it) } }
    }

    override fun getCoinDetails(
        exchangeId: String,
        period: Period,
        timeEnd: String,
        limit: Int
    ): Single<List<CoinDetailsEntity>> =
        coinApi.getCoinDetails(exchangeId, period.getValue(), timeEnd, limit).map {
            it.map { coinDetailsEntityMapper.map(it) }
        }


}