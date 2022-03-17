package co.iskv.crypto.navigation.screens

import androidx.fragment.app.Fragment
import co.iskv.crypto.ui.currencies.CurrenciesFragment

import ru.terrakok.cicerone.android.support.SupportAppScreen

object CurrenciesScreen : SupportAppScreen() {
    override fun getFragment(): Fragment = CurrenciesFragment.newInstance()
}