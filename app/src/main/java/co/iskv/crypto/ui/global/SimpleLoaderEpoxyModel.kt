package co.iskv.crypto.ui.global

import android.view.View
import android.view.ViewParent
import co.iskv.crypto.R
import co.iskv.crypto.databinding.ListItemSimpleLoaderBinding
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass
abstract class SimpleLoaderEpoxyModel : EpoxyModelWithHolder<SimpleLoaderViewHolder>() {

    override fun getDefaultLayout(): Int = R.layout.list_item_simple_loader

    override fun createNewHolder(parent: ViewParent): SimpleLoaderViewHolder =
        SimpleLoaderViewHolder(parent)

}

class SimpleLoaderViewHolder(viewParent: ViewParent) : EpoxyHolder(viewParent) {

    lateinit var binding: ListItemSimpleLoaderBinding

    override fun bindView(itemView: View) {
        binding = ListItemSimpleLoaderBinding.bind((itemView))
    }

}
