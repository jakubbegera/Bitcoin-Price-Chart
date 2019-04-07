package cz.begera.bitcoin_price_chart.bitcoin_price.data

/**
 * Created by Jakub Begera (jakub@begera.cz) on 07/04/2019.
 */
data class BlockchainChartRaw(
    val status: String,
    val name: String,
    val unit: String,
    val period: String,
    val description: String,
    val values: List<ChartValueRaw>
)

data class ChartValueRaw(
    val x: Long,
    val y: Double
)