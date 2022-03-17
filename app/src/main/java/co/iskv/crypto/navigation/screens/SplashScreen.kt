package co.iskv.crypto.navigation.screens

import androidx.fragment.app.Fragment
import co.iskv.crypto.ui.splash.SplashFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object SplashScreen : SupportAppScreen() {
    override fun getFragment(): Fragment = SplashFragment.newInstance()
}