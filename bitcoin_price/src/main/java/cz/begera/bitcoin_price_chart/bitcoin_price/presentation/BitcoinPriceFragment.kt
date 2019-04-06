package cz.begera.bitcoin_price_chart.bitcoin_price.presentation

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import cz.begera.bitcoin_price_chart.base.extensions.getBaseInjectingActivity
import cz.begera.bitcoin_price_chart.base.presentation.BaseInjectingFragment
import cz.begera.bitcoin_price_chart.bitcoin_price.R
import cz.begera.bitcoin_price_chart.bitcoin_price.injection.BitcoinPriceComponent
import javax.inject.Inject

/**
 * Created by Jakub Begera (jakub@begera.cz) on 06/04/2019.
 */
class BitcoinPriceFragment : BaseInjectingFragment() {

    @Inject
    lateinit var viewModeFactory: ViewModelProvider.Factory
    lateinit var viewModel: BitcoinPriceViewModel

    override val layoutId = R.layout.fragment_bitcoin_price

    override fun onInject() {
        getBaseInjectingActivity<BitcoinPriceComponent.BitcoinPriceComponentCreator>()
            .component
            .createBitcoinPriceComponent()
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this, this.viewModeFactory).get(BitcoinPriceViewModel::class.java)
        super.onCreate(savedInstanceState)
    }
}