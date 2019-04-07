package cz.begera.bitcoin_price_chart

import android.app.Application
import cz.begera.bitcoin_price_chart.injection.ApplicationComponent
import cz.begera.bitcoin_price_chart.injection.DaggerApplicationComponent
import timber.log.Timber

/**
 * Created by Jakub Begera (jakub@begera.cz) on 06/04/2019.
 */
class BitcoinPriceApplication : Application() {

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.builder().application(this).build()
    }

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            // setup release timber tree
        }
    }
}