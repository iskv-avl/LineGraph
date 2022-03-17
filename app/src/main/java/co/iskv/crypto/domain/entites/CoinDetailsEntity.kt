package co.iskv.crypto.domain.entites

import org.joda.time.DateTime

data class CoinDetailsEntity(
    val timePeriodStart: DateTime,
    val timePeriodEnd: DateTime,
    val timeOpen: DateTime,
    val timeClose: DateTime,
    val priceHigh: Double,
    val priceLow: Double,
    val priceClose: Double,
    val volumeTraded: Double,
    val tradesCount: Int
)
