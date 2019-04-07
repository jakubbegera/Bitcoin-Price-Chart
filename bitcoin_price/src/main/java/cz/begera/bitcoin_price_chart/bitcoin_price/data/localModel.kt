package cz.begera.bitcoin_price_chart.bitcoin_price.data

import io.reactivex.functions.Function

/**
 * Created by Jakub Begera (jakub@begera.cz) on 07/04/2019.
 */
data class BlockchainChart(
    val timespan: String,
    val unit: String,
    val values: List<ChartValue>
)

data class ChartValue(
    val x: Long,
    val y: Double
)

val blockchainChartIdExtractor = Function<BlockchainChart, String> {t -> t.timespan }

enum class Timespan(val key: String) {
    DAYS30("30days"),
    DAYS60("60days"),
    DAYS180("180days"),
    YEAR1("1year"),
    YEAR2("2year"),
    ALL_TIME("all")
}