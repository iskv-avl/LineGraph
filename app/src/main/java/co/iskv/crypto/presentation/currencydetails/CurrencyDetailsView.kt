package co.iskv.crypto.presentation.currencydetails

import co.iskv.crypto.widget.models.CoinModel
import co.iskv.crypto.widget.models.Period
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.AddToEndSingleTagStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleTagStrategy::class, tag="coin_details")
interface CurrencyDetailsView : MvpView {

    fun setGraphData(period: Period, data: CoinModel)
    fun showLoading()
    fun showError(error: String)

}