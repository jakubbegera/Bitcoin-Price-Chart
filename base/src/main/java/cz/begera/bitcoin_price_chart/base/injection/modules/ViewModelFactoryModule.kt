package cz.begera.bitcoin_price_chart.base.injection.modules

import androidx.lifecycle.ViewModelProvider
import cz.begera.bitcoin_price_chart.base.injection.DaggerViewModelFactory
import dagger.Binds
import dagger.Module

/**
 * Created by Jakub Begera (jakub@begera.cz) on 06/04/2019.
 */
@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: DaggerViewModelFactory): ViewModelProvider.Factory
}