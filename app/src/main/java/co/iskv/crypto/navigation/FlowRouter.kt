package co.iskv.crypto.navigation

import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppScreen

open class FlowRouter(private val parentRouter: Router) : Router() {

    fun replaceFlow(screen: SupportAppScreen) {
        parentRouter.replaceScreen(screen)
    }

    fun startFlow(screen: SupportAppScreen) {
        parentRouter.navigateTo(screen)
    }

    fun newRootFlow(screen: SupportAppScreen) {
        parentRouter.newRootScreen(screen)
    }

    fun addFlow(screen: SupportAppScreen) {
        (parentRouter as? FlowRouter)?.add(screen)
    }

    fun finishFlow() {
        parentRouter.exit()
    }

    fun add(screen: SupportAppScreen) {
        executeCommands(ForwardAdd(screen))
    }

}