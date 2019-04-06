package cz.begera.bitcoin_price_chart.extensions

import cz.begera.bitcoin_price_chart.BitcoinPriceApplication
import cz.begera.bitcoin_price_chart.base.presentation.BaseActivity

/**
 * Created by Jakub Begera (jakub@begera.cz) on 06/04/2019.
 */

fun BaseActivity.appComponent() = (application as? BitcoinPriceApplication)?.component
    ?: throw RuntimeException("Unable to get BitcoinPriceApplication.")