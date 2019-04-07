package cz.begera.bitcoin_price_chart.bitcoin_price.data

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Jakub Begera (jakub@begera.cz) on 06/04/2019.
 */
interface BlockchainChartsService {

    @GET("charts/market-price")
    fun getBitcoinMarketPrice(
        @Query("timespan") timespan: String,
        @Query("sampled") sampled: Boolean = true,
        @Query("format") format: String = "json"
    ): Single<BlockchainChartRaw>

}