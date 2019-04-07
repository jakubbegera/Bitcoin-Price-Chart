package cz.begera.bitcoin_price_chart.injection

import cz.begera.bitcoin_price_chart.BitcoinPriceApplication
import cz.begera.bitcoin_price_chart.base.injection.qualifiers.ForApplication
import dagger.Module
import dagger.Provides

/**
 * Created by Jakub Begera (jakub@begera.cz) on 07/04/2019.
 */
@Module
class ApplicationModule {

    @ForApplication
    @Provides
    fun provideApplicationContext(app: BitcoinPriceApplication) = app.applicationContext

}