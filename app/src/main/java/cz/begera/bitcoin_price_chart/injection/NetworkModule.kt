package cz.begera.bitcoin_price_chart.injection

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import cz.begera.bitcoin_price_chart.base.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
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

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class AppInterceptor

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class NetworkInterceptor

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
        @AppInterceptor appInterceptor: Set<@JvmSuppressWildcards Interceptor>,
        @NetworkInterceptor networkInterceptor: Set<@JvmSuppressWildcards Interceptor>
    ): OkHttpClient {
        val okBuilder = OkHttpClient.Builder()
        okBuilder.interceptors().addAll(appInterceptor)
        okBuilder.networkInterceptors().addAll(networkInterceptor)
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

}