package cz.begera.bitcoin_price_chart.bitcoin_price.data

import io.reactivex.functions.Function
import javax.inject.Inject

/**
 * Created by Jakub Begera (jakub@begera.cz) on 07/04/2019.
 */
class BlockchainChartMapper @Inject constructor() : Function<Pair<Timespan, BlockchainChartRaw>, BlockchainChart> {

    override fun apply(raw: Pair<Timespan, BlockchainChartRaw>): BlockchainChart {
        return BlockchainChart(
            raw.first.key,
            raw.second.unit,
            raw.second.values.map {
                ChartValue(
                    it.x,
                    it.y
                )
            }
        )
    }

}