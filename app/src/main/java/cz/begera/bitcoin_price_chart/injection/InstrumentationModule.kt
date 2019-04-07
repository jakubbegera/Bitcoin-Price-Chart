package cz.begera.bitcoin_price_chart.injection

import dagger.Module
import dagger.Provides
import dagger.multibindings.ElementsIntoSet
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import javax.inject.Singleton

/**
 * Created by Jakub Begera (jakub@begera.cz) on 07/04/2019.
 */
@Module
class InstrumentationModule {

    @Provides
    @Singleton
    @NetworkModule.NetworkInterceptor
    @ElementsIntoSet
    internal fun provideNetworkInterceptors(): Set<Interceptor> {
        return setOf(
            HttpLoggingInterceptor { message -> Timber.d(message) }
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        )
    }

    @Provides
    @Singleton
    @NetworkModule.AppInterceptor
    @ElementsIntoSet
    internal fun provideAppInterceptors(): Set<Interceptor> {
        return emptySet()
    }
}