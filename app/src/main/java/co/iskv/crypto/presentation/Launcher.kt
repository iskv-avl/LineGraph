package co.iskv.crypto.presentation

import co.iskv.crypto.navigation.FlowRouter
import co.iskv.crypto.navigation.screens.CurrencyDetailsScreen
import co.iskv.crypto.navigation.screens.SplashScreen
import javax.inject.Inject

class Launcher @Inject constructor(
    private val router: FlowRouter
) : AppLauncher {

    override fun coldStart() {
        router.newRootScreen(SplashScreen)
    }

}