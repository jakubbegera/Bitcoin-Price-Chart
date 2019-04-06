package cz.begera.bitcoin_price_chart.base.extensions

import cz.begera.bitcoin_price_chart.base.presentation.BaseInjectingActivity
import cz.begera.bitcoin_price_chart.base.presentation.BaseInjectingFragment

/**
 * Created by Jakub Begera (jakub@begera.cz) on 06/04/2019.
 */

@Suppress("UNCHECKED_CAST")
fun <Component: Any>BaseInjectingFragment.getBaseInjectingActivity() = requireActivity() as BaseInjectingActivity<Component>