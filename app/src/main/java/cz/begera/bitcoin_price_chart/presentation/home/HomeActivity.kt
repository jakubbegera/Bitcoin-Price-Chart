package cz.begera.bitcoin_price_chart.presentation.home

import android.os.Bundle
import cz.begera.bitcoin_price_chart.R
import cz.begera.bitcoin_price_chart.base.injection.modules.ActivityModule
import cz.begera.bitcoin_price_chart.base.presentation.BaseInjectingActivity
import cz.begera.bitcoin_price_chart.bitcoin_price.presentation.BitcoinPriceFragment
import cz.begera.bitcoin_price_chart.extensions.appComponent

/**
 * Created by Jakub Begera (jakub@begera.cz) on 06/04/2019.
 */
class HomeActivity : BaseInjectingActivity<HomeActivityComponent>() {

    override val layoutId = R.layout.activity_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            showBitcoinPriceFragment()
        }
    }

    override fun onInject(component: HomeActivityComponent) {
        component.inject(this)
    }

    override fun createComponent() = appComponent().createHomeActivityComponent(ActivityModule(this))

    private fun showBitcoinPriceFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.frame, BitcoinPriceFragment())
            .commit()
    }

}