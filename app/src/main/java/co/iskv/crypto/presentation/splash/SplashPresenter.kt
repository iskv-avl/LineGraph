package co.iskv.crypto.presentation.splash

import co.iskv.crypto.navigation.FlowRouter
import co.iskv.crypto.navigation.screens.CurrenciesScreen
import co.iskv.crypto.presentation.base.BasePresenter
import javax.inject.Inject

class SplashPresenter @Inject constructor(
    private val router: FlowRouter
) : BasePresenter<SplashView>() {

    fun onMenuOpen() {
        router.newRootScreen(CurrenciesScreen)
    }

}