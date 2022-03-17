package co.iskv.crypto

import android.app.Application
import co.iskv.crypto.di.DI
import co.iskv.crypto.di.module.*
import toothpick.Toothpick

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initAppScope()
    }

    private fun initAppScope() {
        Toothpick.openScope(DI.APP_SCOPE).installModules(
            AppModule(this),
            NavigationModule(),
            NetworkModule(BuildConfig.END_POINT),
            ApiModule(),
            RepositoryModule()
        )
    }

}