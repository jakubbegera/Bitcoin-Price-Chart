package cz.begera.bitcoin_price_chart.bitcoin_price.presentation

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import cz.begera.bitcoin_price_chart.base.extensions.getBaseInjectingActivity
import cz.begera.bitcoin_price_chart.base.extensions.gone
import cz.begera.bitcoin_price_chart.base.extensions.visible
import cz.begera.bitcoin_price_chart.base.presentation.BaseInjectingFragment
import cz.begera.bitcoin_price_chart.bitcoin_price.R
import cz.begera.bitcoin_price_chart.bitcoin_price.data.Timespan
import cz.begera.bitcoin_price_chart.bitcoin_price.injection.BitcoinPriceComponent
import kotlinx.android.synthetic.main.fragment_bitcoin_price.*
import timber.log.Timber
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, this.viewModeFactory).get(BitcoinPriceViewModel::class.java)
        viewModel.creditListLiveData.observe(this, Observer { this.renderModel(it) })

        chips_timespan.setOnCheckedChangeListener { _, resID ->
            val timespan = when (resID) {
                R.id.chip_timespan_30days -> Timespan.DAYS30
                R.id.chip_timespan_60days -> Timespan.DAYS60
                R.id.chip_timespan_180days -> Timespan.DAYS180
                R.id.chip_timespan_1year -> Timespan.YEAR1
                R.id.chip_timespan_2years -> Timespan.YEAR2
                R.id.chip_timespan_all -> Timespan.ALL_TIME
                else -> {
                    Timber.e("Timespan chip button id $resID not recognized.")
                    return@setOnCheckedChangeListener
                }
            }
            viewModel.bindToBitcoinPrice(timespan)
        }

        chips_timespan.check(R.id.chip_timespan_30days)
    }

    private fun renderModel(model: BitcoinPriceModel) {
        when (model) {
            is BitcoinPriceModel.Loading -> {
                txv_error.gone()
                txv_data.gone()
                prb_loading.visible()

            }
            is BitcoinPriceModel.Error -> {
                txv_error.text = getString(R.string.chart_loading_error)
                txv_data.gone()
                prb_loading.gone()
                txv_error.visible()
            }
            is BitcoinPriceModel.Data -> {
                txv_data.text = model.data.toString()
                txv_error.gone()
                prb_loading.gone()
                txv_data.visible()
            }
        }
    }
}