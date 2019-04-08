package cz.begera.bitcoin_price_chart.bitcoin_price

import cz.begera.bitcoin_price_chart.bitcoin_price.data.*

/**
 * Created by Jakub Begera (jakub@begera.cz) on 08/04/2019.
 */

val blockchainChartRaw = BlockchainChartRaw(
    "ok",
    "name",
    "USD",
    "day",
    "dsc",
    (0 until 100).map {
        ChartValueRaw(it.toLong(), it.toDouble().times(10))
    }
)

val blockchainChart = BlockchainChart(
    Timespan.DAYS30.key,
    "USD",
    (0 until 100).map {
        ChartValue(it.toLong(), it.toDouble().times(10))
    }
)