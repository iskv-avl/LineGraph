package co.iskv.crypto.navigation

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import co.iskv.crypto.di.DI
import co.iskv.crypto.presentation.AppLauncher
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.cicerone.commands.Command
import toothpick.Toothpick

open class ExtendedSupportAppNavigator(
    fa: FragmentActivity,
    private val fm: FragmentManager,
    private val containerId: Int
) : SupportAppNavigator(fa, fm, containerId) {

    constructor(fa: FragmentActivity, containerId: Int): this(fa, fa.supportFragmentManager, containerId)

    override fun applyCommand(command: Command) {
        super.applyCommand(command)
        if (command is ForwardAdd) {
            fragmentForwardAdd(command)
        }
    }

    private fun fragmentForwardAdd(command: ForwardAdd) {
        val screen = command.screen as SupportAppScreen
        val fragment = createFragment(screen)

        val fragmentTransaction = fm.beginTransaction()

        setupFragmentTransaction(
            command,
            fm.findFragmentById(containerId),
            fragment,
            fragmentTransaction
        )

        fragmentTransaction
            .add(containerId, fragment!!)
            .addToBackStack(screen.screenKey)
            .commit()

        applyCommands(emptyArray())
    }

}