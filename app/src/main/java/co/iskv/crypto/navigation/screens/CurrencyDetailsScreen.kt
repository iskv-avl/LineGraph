package co.iskv.crypto.navigation.screens

import androidx.fragment.app.Fragment
import co.iskv.crypto.ui.currencydetails.CurrencyDetailsFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

data class CurrencyDetailsScreen(val coinName: String) : SupportAppScreen() {
    override fun getFragment(): Fragment = CurrencyDetailsFragment.newInstance(coinName)
}