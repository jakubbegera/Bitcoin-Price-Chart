package cz.begera.bitcoin_price_chart.presentation.home

import cz.begera.bitcoin_price_chart.base.injection.modules.ActivityModule
import cz.begera.bitcoin_price_chart.base.injection.scopes.ActivityScope
import cz.begera.bitcoin_price_chart.bitcoin_price.injection.BitcoinPriceComponent
import dagger.Subcomponent

/**
 * Created by Jakub Begera (jakub@begera.cz) on 06/04/2019.
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface HomeActivityComponent : BitcoinPriceComponent.BitcoinPriceComponentCreator {
    fun inject(homeActivity: HomeActivity)
}