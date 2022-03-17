package co.iskv.crypto.ui.currencydetails

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import co.iskv.crypto.R
import co.iskv.crypto.databinding.FragmentCurrencyDetailsBinding
import co.iskv.crypto.di.CoinName
import co.iskv.crypto.presentation.currencydetails.CurrencyDetailsPresenter
import co.iskv.crypto.presentation.currencydetails.CurrencyDetailsView
import co.iskv.crypto.ui.base.BaseFragment
import co.iskv.crypto.widget.models.CoinModel
import co.iskv.crypto.widget.models.Period
import moxy.ktx.moxyPresenter
import toothpick.Scope
import toothpick.config.Module

class CurrencyDetailsFragment :
    BaseFragment<FragmentCurrencyDetailsBinding>(R.layout.fragment_currency_details),
    CurrencyDetailsView {

    companion object {
        private const val KEY_COIN_NAME = "coin"
        fun newInstance(coinName: String) = CurrencyDetailsFragment().apply {
            arguments = bundleOf(KEY_COIN_NAME to coinName)
        }
    }

    override val bindingProvider: (View) -> FragmentCurrencyDetailsBinding
        get() = FragmentCurrencyDetailsBinding::bind

    private val presenter by moxyPresenter { scope.getInstance(CurrencyDetailsPresenter::class.java) }

    private val coinName by lazy {
        requireArguments().getString(KEY_COIN_NAME)
    }

    override fun installModules(scope: Scope) {
        super.installModules(scope)
        scope.installModules(object : Module() {
            init {
                bind(String::class.java).withName(CoinName::class.java).toInstance(coinName)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.toolbar.title = resources.getString(R.string.title_currency_details, coinName)
        binding.toolbar.setNavigationOnClickListener { presenter.onBackPressed() }
        binding.lineGraph.setOnPeriodClickListener(presenter::onPeriodClicked)
    }

    override fun setGraphData(period: Period, data: CoinModel) {
        binding.error.root.isGone = true
        binding.lottieLoader.isGone = true
        binding.lineGraph.isVisible = true
        binding.lineGraph.setGraphData(period, data)
    }

    override fun showLoading() {
        binding.error.root.isGone = true
        binding.lineGraph.isGone = true
        binding.lottieLoader.isVisible = true
    }

    override fun showError(error: String) {
        binding.lineGraph.isGone = true
        binding.lottieLoader.isGone = false
        binding.error.root.isVisible = true
        binding.error.tvError.text = error
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }

}