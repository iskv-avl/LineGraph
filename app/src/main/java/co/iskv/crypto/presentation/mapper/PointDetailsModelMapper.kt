package co.iskv.crypto.presentation.mapper

import android.content.Context
import co.iskv.crypto.R
import co.iskv.crypto.domain.entites.CoinDetailsEntity
import co.iskv.crypto.widget.models.PointDetailsModel
import javax.inject.Inject

class PointDetailsModelMapper @Inject constructor(
    private val context: Context
) {

    private val dateFormat = "dd MMM YYYY"

    fun map(coin: CoinDetailsEntity): PointDetailsModel =
        PointDetailsModel(
            name = context.getString(R.string.label_currency_details_dollar, coin.priceClose),
            date = coin.timeClose.toString(dateFormat),
            value = coin.priceClose
        )

}