package cz.begera.bitcoin_price_chart.base.common.providers

import javax.inject.Inject

/**
 * Class to be able to test timestamp related features. Inject this instead of using System.currentTimeMillis()
 */
class TimestampProvider @Inject constructor() {

    fun currentTimeMillis(): Long {
        return System.currentTimeMillis()
    }
}
