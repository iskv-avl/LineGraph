package co.iskv.crypto.data.remote.mapper

import co.iskv.crypto.data.remote.models.CoinDetailsNetworkModel
import co.iskv.crypto.domain.entites.CoinDetailsEntity
import javax.inject.Inject

class CoinDetailsEntityMapper @Inject constructor(
    private val dateTimeMapper: DateTimeMapper
) {

    fun map(details: CoinDetailsNetworkModel): CoinDetailsEntity =
        CoinDetailsEntity(
            timePeriodStart = dateTimeMapper.map(details.timePeriodStart),
            timePeriodEnd = dateTimeMapper.map(details.timePeriodEnd),
            timeOpen = dateTimeMapper.map(details.timeOpen),
            timeClose = dateTimeMapper.map(details.timeClose),
            priceHigh = details.priceHigh,
            priceLow = details.priceLow,
            priceClose = details.priceClose,
            volumeTraded = details.volumeTraded,
            tradesCount = details.tradesCount,
        )

}