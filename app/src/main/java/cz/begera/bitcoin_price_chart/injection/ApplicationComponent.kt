package cz.begera.bitcoin_price_chart.injection

import cz.begera.bitcoin_price_chart.BitcoinPriceApplication
import cz.begera.bitcoin_price_chart.base.injection.modules.ActivityModule
import cz.begera.bitcoin_price_chart.base.injection.modules.ViewModelFactoryModule
import cz.begera.bitcoin_price_chart.presentation.home.HomeActivityComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Jakub Begera (jakub@begera.cz) on 06/04/2019.
 */
@Singleton
@Component(modules = [ApplicationModule::class, ViewModelFactoryModule::class, NetworkModule::class, DataModule::class])
interface ApplicationComponent {

    fun inject(bitcoinPriceApplication: BitcoinPriceApplication)

    fun createHomeActivityComponent(module: ActivityModule): HomeActivityComponent

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: BitcoinPriceApplication): Builder

        fun build(): ApplicationComponent
    }

}