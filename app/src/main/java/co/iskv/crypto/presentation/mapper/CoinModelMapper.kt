package co.iskv.crypto.presentation.mapper

import android.content.Context
import co.iskv.crypto.R
import co.iskv.crypto.domain.entites.CoinDetailsEntity
import co.iskv.crypto.domain.errors.NotEnoughPointsError
import co.iskv.crypto.widget.models.CoinModel
import co.iskv.crypto.widget.models.Period
import javax.inject.Inject

class CoinModelMapper @Inject constructor(
    private val context: Context,
    private val percentModelMapper: PercentModelMapper,
    private val pointDetailsModelMapper: PointDetailsModelMapper,
) {

    fun map(
        coinName: String,
        period: Period,
        legendLineQuantity: Int,
        values: List<CoinDetailsEntity>
    ): CoinModel =
    //region Description
    //Используем priceClose в качестве стоимости валюты в конкретной точке на графике
    //В качестве даты на графике используем переменную timeClose
    //endregion
        values.map { it.priceClose }.let { price ->
            CoinModel(
                name = coinName,
                period = period,
                min = price.minOrNull() ?: 0.0,
                max = price.maxOrNull() ?: 0.0,
                percent = percentModelMapper.map(getPercentChange(price)),
                legend = getLegend(legendLineQuantity, price),
                values = values.map { pointDetailsModelMapper.map(it) },
            )
        }

    private fun getPercentChange(values: List<Double>): Double {
        if (values.size >= 2) {
            return (values.last() / values[values.size - 2] - 1) * 100
        }
        NotEnoughPointsError
        return 0.0
    }

    private fun getLegend(legendLineQuantity: Int, values: List<Double>): List<String> {
        val (max, min) = listOfNotNull(values.maxOrNull(), values.minOrNull())
            .takeIf { it.size == 2 }
            ?.zipWithNext()
            ?.first()
            ?: return emptyList()

        val step = (max - min) / (legendLineQuantity - 1)
        return List(legendLineQuantity) {
            context.getString(
                R.string.label_currency_details_dollar,
                max - (step * it)
            )
        }
    }

}