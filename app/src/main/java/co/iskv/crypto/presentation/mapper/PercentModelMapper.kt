package co.iskv.crypto.presentation.mapper

import android.content.Context
import co.iskv.crypto.R
import co.iskv.crypto.widget.models.Direction
import co.iskv.crypto.widget.models.PercentModel
import javax.inject.Inject
import kotlin.math.absoluteValue

class PercentModelMapper @Inject constructor(
    private val context: Context
) {

    fun map(percentChange: Double): PercentModel =
        if (percentChange >= 0) {
            PercentModel(
                direction = Direction.UP,
                percent = context.getString(
                    R.string.label_currency_details_percent_up,
                    percentChange
                )
            )
        } else {
            PercentModel(
                direction = Direction.FALL,
                percent = context.getString(
                    R.string.label_currency_details_percent_fall,
                    percentChange.absoluteValue
                )
            )
        }

}