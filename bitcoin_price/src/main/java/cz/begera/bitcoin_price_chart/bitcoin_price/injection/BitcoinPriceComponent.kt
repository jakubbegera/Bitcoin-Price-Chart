package cz.begera.bitcoin_price_chart.bitcoin_price.injection

import cz.begera.bitcoin_price_chart.base.injection.scopes.FragmentScope
import cz.begera.bitcoin_price_chart.bitcoin_price.presentation.BitcoinPriceFragment
import dagger.Subcomponent

/**
 * Created by Jakub Begera (jakub@begera.cz) on 06/04/2019.
 */
@FragmentScope
@Subcomponent(modules = [BitcoinPriceModule::class])
interface BitcoinPriceComponent {

    fun inject(fragment: BitcoinPriceFragment)

    interface BitcoinPriceComponentCreator {

        fun createBitcoinPriceComponent(): BitcoinPriceComponent
    }
}