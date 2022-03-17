package co.iskv.crypto.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import co.iskv.crypto.R
import co.iskv.crypto.di.DI
import co.iskv.crypto.navigation.ExtendedSupportAppNavigator
import co.iskv.crypto.presentation.AppLauncher
import co.iskv.crypto.ui.base.BackPressListener
import moxy.MvpAppCompatActivity
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.commands.Command
import toothpick.Toothpick

class AppActivity : MvpAppCompatActivity(), BackPressListener {

    private val navigatorHolder by inject<NavigatorHolder>()

    private val appLauncher by inject<AppLauncher>()

    private val contentView = R.layout.activity_app
    private val containerId = R.id.flAppContainer

    private val navigator: Navigator by lazy {
        object : ExtendedSupportAppNavigator(this, containerId) {
            override fun setupFragmentTransaction(
                command: Command?,
                currentFragment: Fragment?,
                nextFragment: Fragment?,
                fragmentTransaction: FragmentTransaction
            ) {
                fragmentTransaction.setReorderingAllowed(true)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Toothpick.inject(this, Toothpick.openScope(DI.APP_SCOPE))
        super.onCreate(savedInstanceState)
        setContentView(contentView)
        if (savedInstanceState == null) {
            appLauncher.coldStart()
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onBackPressed() {
        (supportFragmentManager.findFragmentById(containerId) as? BackPressListener)?.onBackPressed()
    }

    private inline fun <reified T> inject(): Lazy<T> =
        lazy { Toothpick.openScope(DI.APP_SCOPE).getInstance(T::class.java) }

}