package co.iskv.crypto.di.module

import co.iskv.crypto.navigation.FlowRouter
import co.iskv.crypto.presentation.AppLauncher
import co.iskv.crypto.presentation.Launcher
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import toothpick.config.Module

class NavigationModule : Module() {

    init {
        val rootRouter = Router()
        val cicerone = Cicerone.create(FlowRouter(rootRouter))
        bind(FlowRouter::class.java).toInstance(cicerone.router)
        bind(NavigatorHolder::class.java).toInstance(cicerone.navigatorHolder)

        bind(AppLauncher::class.java).to(Launcher::class.java).singleton()
    }

}