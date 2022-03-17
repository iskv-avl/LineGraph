package co.iskv.crypto.data.remote.mapper

import co.iskv.crypto.data.remote.models.CoinIconNetworkModel
import co.iskv.crypto.domain.entites.CoinIconEntity
import javax.inject.Inject

class CoinIconEntityMapper @Inject constructor() {

    fun map(data: CoinIconNetworkModel): CoinIconEntity =
        CoinIconEntity(data.assetId, data.url)

}