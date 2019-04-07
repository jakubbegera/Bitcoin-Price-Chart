package cz.begera.bitcoin_price_chart.bitcoin_price.injection

import cz.begera.bitcoin_price_chart.base.common.providers.TimestampProvider
import cz.begera.bitcoin_price_chart.base.data.cache.Cache
import cz.begera.bitcoin_price_chart.base.data.store.MemoryReactiveStore
import cz.begera.bitcoin_price_chart.base.data.store.ReactiveStore
import cz.begera.bitcoin_price_chart.base.data.store.Store
import cz.begera.bitcoin_price_chart.bitcoin_price.data.BlockchainChart
import cz.begera.bitcoin_price_chart.bitcoin_price.data.BlockchainChartsService
import cz.begera.bitcoin_price_chart.bitcoin_price.data.blockchainChartIdExtractor
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by Jakub Begera (jakub@begera.cz) on 06/04/2019.
 */
@Module
class BitcoinPriceDataModule {

    companion object {
        private const val CACHE_MAX_AGE = (5 * 60 * 1000).toLong() // 5 minutes
    }

    @Provides
    @Singleton
    fun provideBlockchainChartsService(retrofit: Retrofit): BlockchainChartsService =
        retrofit.create(BlockchainChartsService::class.java)

    @Provides
    @Singleton
    fun provideCache(timestampProvider: TimestampProvider): Store.MemoryStore<String, BlockchainChart> {
        return Cache(timestampProvider, blockchainChartIdExtractor, CACHE_MAX_AGE)
    }

    @Provides
    @Singleton
    fun provideReactiveStore(cache: Store.MemoryStore<String, BlockchainChart>): ReactiveStore<String, BlockchainChart> {
        return MemoryReactiveStore({ value -> blockchainChartIdExtractor.apply(value) }, cache)
    }

}