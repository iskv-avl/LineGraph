package co.iskv.crypto.ui.currencies

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import co.iskv.crypto.R
import co.iskv.crypto.databinding.FragmentCurrenciesBinding
import co.iskv.crypto.domain.entites.CoinIconEntity
import co.iskv.crypto.presentation.currencies.CurrenciesPresenter
import co.iskv.crypto.presentation.currencies.CurrenciesView
import co.iskv.crypto.ui.base.BaseFragment
import co.iskv.crypto.ui.currencies.adapter.CurrenciesController
import co.iskv.crypto.ui.currencies.adapter.CurrenciesDecorator
import moxy.ktx.moxyPresenter

class CurrenciesFragment : BaseFragment<FragmentCurrenciesBinding>(R.layout.fragment_currencies),
    CurrenciesView {

    companion object {
        fun newInstance() = CurrenciesFragment()
    }

    override val bindingProvider: (View) -> FragmentCurrenciesBinding
        get() = FragmentCurrenciesBinding::bind

    private val presenter by moxyPresenter { scope.getInstance(CurrenciesPresenter::class.java) }

    private val controller by lazy {
        CurrenciesController(presenter::onCoinClicked).apply { requestModelBuild() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvCurrencies.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setController(controller)
            addItemDecoration(CurrenciesDecorator(requireContext()))
        }
    }

    override fun showData(data: List<CoinIconEntity>) {
        controller.showLoader = false
        controller.showData = true
        controller.setData(data)
    }

    override fun showLoading() {
        controller.showLoader = true
        controller.showData = false
    }

    override fun showError() {
        controller.showLoader = false
        controller.showData = false
    }


}