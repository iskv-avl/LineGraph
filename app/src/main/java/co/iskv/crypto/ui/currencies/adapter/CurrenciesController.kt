package co.iskv.crypto.ui.currencies.adapter

import co.iskv.crypto.domain.entites.CoinIconEntity
import co.iskv.crypto.ui.global.SimpleLoaderEpoxyModel_
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.EpoxyController

class CurrenciesController(
    private val onCoinItemClickListener: (CoinIconEntity) -> Unit
) : EpoxyController() {

    private val coins = mutableListOf<CoinIconEntity>()

    @AutoModel
    lateinit var loader: SimpleLoaderEpoxyModel_

    var showLoader = false
        set(value) {
            field = value
            requestModelBuild()
        }

    var showData = false
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        loader.addIf(showLoader, this)

        if (!showData) return
        coins.forEachIndexed { index, coinIconEntity ->
            CoinItemEpoxyModel_()
                .id("coin_$index")
                .coin(coinIconEntity)
                .onCoinClickListener(onCoinItemClickListener)
                .addTo(this)
        }
    }

    fun setData(coins: List<CoinIconEntity>) {
        this.coins.clear()
        this.coins.addAll(coins)
        requestModelBuild()
    }

}