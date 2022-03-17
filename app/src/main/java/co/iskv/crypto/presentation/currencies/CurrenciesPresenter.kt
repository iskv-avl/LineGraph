package co.iskv.crypto.presentation.currencies

import co.iskv.crypto.domain.CoinRepository
import co.iskv.crypto.domain.entites.CoinIconEntity
import co.iskv.crypto.navigation.FlowRouter
import co.iskv.crypto.navigation.screens.CurrencyDetailsScreen
import co.iskv.crypto.presentation.base.BasePresenter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class CurrenciesPresenter @Inject constructor(
    private val router: FlowRouter,
    private val coinRepository: CoinRepository
) : BasePresenter<CurrenciesView>() {

    private val iconSize = 20

    override fun onFirstViewAttach() {
        coinRepository.getCoinIcons(iconSize)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { viewState.showLoading() }
            .subscribe({
                viewState.showData(it)
            }, {
                viewState.showError()
            })
            .disposeOnDestroy()
    }

    fun onCoinClicked(coin: CoinIconEntity) {
        router.navigateTo(CurrencyDetailsScreen(coin.assetId))
    }

}