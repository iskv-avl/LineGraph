package co.iskv.crypto.presentation.currencies

import co.iskv.crypto.domain.entites.CoinIconEntity
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleTagStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleTagStrategy::class, tag = "coin")
interface CurrenciesView: MvpView {

    fun showData(data: List<CoinIconEntity>)
    fun showLoading()
    fun showError()

}