package co.iskv.crypto.ui.currencies.adapter

import android.view.View
import android.view.ViewParent
import co.iskv.crypto.R
import co.iskv.crypto.databinding.ListItemCoinBinding
import co.iskv.crypto.domain.entites.CoinIconEntity
import com.airbnb.epoxy.*
import com.squareup.picasso.Picasso

@EpoxyModelClass
abstract class CoinItemEpoxyModel : EpoxyModelWithHolder<CoinItemHolder>() {

    @EpoxyAttribute
    lateinit var coin: CoinIconEntity

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var onCoinClickListener: (CoinIconEntity) -> Unit

    override fun bind(holder: CoinItemHolder) {
        with(holder.binding) {
            Picasso.get()
                .load(coin.url)
                .placeholder(R.drawable.ic_unknown_image)
                .into(ivCoin)
            tvCoinName.text = coin.assetId
            root.setOnClickListener { onCoinClickListener.invoke(coin) }
        }
    }

    override fun unbind(holder: CoinItemHolder) {
        holder.binding.root.setOnClickListener(null)
    }

    override fun createNewHolder(parent: ViewParent): CoinItemHolder = CoinItemHolder(parent)

    override fun getDefaultLayout(): Int = R.layout.list_item_coin

}

class CoinItemHolder(viewParent: ViewParent) : EpoxyHolder(viewParent) {

    lateinit var binding: ListItemCoinBinding

    override fun bindView(itemView: View) {
        binding = ListItemCoinBinding.bind(itemView)
    }

}