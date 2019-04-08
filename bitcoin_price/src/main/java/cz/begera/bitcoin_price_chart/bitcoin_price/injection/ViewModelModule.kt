package cz.begera.bitcoin_price_chart.bitcoin_price.injection

import androidx.lifecycle.ViewModel
import cz.begera.bitcoin_price_chart.base.injection.ViewModelKey
import cz.begera.bitcoin_price_chart.bitcoin_price.presentation.BitcoinPriceViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Jakub Begera (jakub@begera.cz) on 06/04/2019.
 */
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(BitcoinPriceViewModel::class)
    @Suppress("unused")
    abstract fun bindBitcoinPriceViewModel(viewModel: BitcoinPriceViewModel): ViewModel

}