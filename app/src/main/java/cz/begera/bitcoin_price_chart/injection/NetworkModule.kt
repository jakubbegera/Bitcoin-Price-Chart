package cz.begera.bitcoin_price_chart.injection

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import cz.begera.bitcoin_price_chart.base.BuildConfig
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
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Created by Jakub Begera (jakub@begera.cz) on 07/04/2019.
 */
@Module(includes = [InstrumentationModule::class])
class NetworkModule {

    companion object {
        private const val API_URL = "API_URL"
    }

    private val CACHE_MAX_AGE = (5 * 60 * 1000).toLong() // 5 minutes

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    internal annotation class AppInterceptor

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    internal annotation class NetworkInterceptor

    @Provides
    @Named(API_URL)
    fun provideBaseUrl(): String {
        return BuildConfig.BLOCKCHAIN_ENDPOINT_URL
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val builder = GsonBuilder()
        return builder.create()
    }

    @Provides
    @Singleton
    fun provideApiOkHttpClient(
//        @AppInterceptor appInterceptor: Set<Interceptor>,
//        @NetworkInterceptor networkInterceptor: Set<Interceptor>
    ): OkHttpClient {
        val okBuilder = OkHttpClient.Builder()
//        okBuilder.interceptors().addAll(appInterceptor)
//        okBuilder.networkInterceptors().addAll(networkInterceptor)
        return okBuilder.build()
    }

    @Provides
    @Singleton
    fun provideBlockchainAPI(@Named(API_URL) baseUrl: String, gson: Gson, client: OkHttpClient): Retrofit {
        return Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .baseUrl(baseUrl)
            .build()
    }

    // TODO move following i

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