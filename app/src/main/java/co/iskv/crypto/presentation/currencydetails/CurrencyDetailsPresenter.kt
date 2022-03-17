package co.iskv.crypto.presentation.currencydetails

import android.content.Context
import android.util.Log
import co.iskv.crypto.R
import co.iskv.crypto.di.CoinName
import co.iskv.crypto.domain.CoinRepository
import co.iskv.crypto.domain.entites.CoinDetailsEntity
import co.iskv.crypto.domain.errors.NotEnoughPointsError
import co.iskv.crypto.navigation.FlowRouter
import co.iskv.crypto.presentation.base.BasePresenter
import co.iskv.crypto.presentation.mapper.CoinModelMapper
import co.iskv.crypto.widget.models.CoinModel
import co.iskv.crypto.widget.models.Period
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import org.joda.time.DateTime
import javax.inject.Inject

class CurrencyDetailsPresenter @Inject constructor(
    private val router: FlowRouter,
    private val coinRepository: CoinRepository,
    private val coinModelMapper: CoinModelMapper,
    private val context: Context,
    @CoinName private val coinName: String
) : BasePresenter<CurrencyDetailsView>() {

    companion object {
        private const val legendLineQuantity = 6
        private const val pointsQuantity = 30
        private const val exchangeIdSpy = "KRAKENFTS_PERP_BTC_USD"
    }

    private val datePattern = "YYYY-MM-dd"

    private val currentDate: String
        get() = DateTime.now().toString(datePattern)

    private var coinDataList: MutableList<CoinModel?> = MutableList(Period.values().size) { null }

    private val request: (Period) -> Single<CoinModel> = { selectedPeriod ->
        coinRepository.getCoinDetails(
            exchangeIdSpy,
            selectedPeriod,
            currentDate,
            pointsQuantity
        ).map { coinModelMapper.map(coinName, selectedPeriod, legendLineQuantity, it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { viewState.showLoading() }
    }

    override fun onFirstViewAttach() {
        refreshData()
    }

    fun onPeriodClicked(period: Period) {
        if (coinDataList[period.ordinal] != null) {
            viewState.setGraphData(period, coinDataList[period.ordinal]!!)
        } else {
            request(period)
                .subscribe({
                    coinDataList.set(period.ordinal, it)
                    viewState.setGraphData(period, it)
                }, {
                    errorHandler(it)
                })
                .disposeOnDestroy()
        }
    }

    fun onBackPressed() {
        router.exit()
    }

    private fun refreshData(period: Period = Period.DAY) {
        request(period)
            .subscribe({
                viewState.setGraphData(period, it)
                coinDataList = MutableList(Period.values().size) { null }
                coinDataList[period.ordinal] = it
            }, {
                errorHandler(it)
            })
            .disposeOnDestroy()
    }

    private fun errorHandler(error: Throwable) {
        if (error is NotEnoughPointsError) {
            viewState.showError(context.getString(R.string.error_not_enough))
        } else {
            viewState.showError(context.getString(R.string.title_error))
        }
    }

}