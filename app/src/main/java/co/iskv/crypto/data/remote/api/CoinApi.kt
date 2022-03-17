package co.iskv.crypto.data.remote.api

import co.iskv.crypto.data.remote.models.CoinDetailsNetworkModel
import co.iskv.crypto.data.remote.models.CoinIconNetworkModel
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinApi {

    @GET("/v1/assets/icons/{iconSize}")
    fun getIcons(@Path("iconSize") iconSize: Int): Single<List<CoinIconNetworkModel>>

    @GET("/v1/ohlcv/{exchange_id}/history")
    fun getCoinDetails(
        @Path("exchange_id") exchangeId: String,
        @Query("period_id") periodId: String,
        @Query("time_end") timeEnd: String,
        @Query("limit") limit: Int
    ): Single<List<CoinDetailsNetworkModel>>

}