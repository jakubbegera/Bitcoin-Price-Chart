package cz.begera.bitcoin_price_chart.injection

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import javax.inject.Singleton

/**
 * Created by Jakub Begera (jakub@begera.cz) on 07/04/2019.
 */
@Module
class InstrumentationModule {

    @Provides
    @NetworkModule.NetworkInterceptor
    @IntoSet
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message -> Timber.d(message) }
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }
}