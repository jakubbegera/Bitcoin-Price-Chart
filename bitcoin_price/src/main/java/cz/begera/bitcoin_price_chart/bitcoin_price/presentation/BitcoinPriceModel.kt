package cz.begera.bitcoin_price_chart.bitcoin_price.presentation

import cz.begera.bitcoin_price_chart.bitcoin_price.data.BlockchainChart

/**
 * Created by Jakub Begera (jakub@begera.cz) on 07/04/2019.
 */
sealed class BitcoinPriceModel {
    object Loading : BitcoinPriceModel()
    object Error : BitcoinPriceModel()
    class Data(
        val data: BlockchainChart,
        val lowestPrice: Double,
        val currentPrice: Double,
        val highersPrice: Double
    ) : BitcoinPriceModel()
}