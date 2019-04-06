package cz.begera.bitcoin_price_chart.base.injection.modules

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import cz.begera.bitcoin_price_chart.base.injection.qualifiers.ForActivity
import dagger.Module
import dagger.Provides

/**
 * Created by Jakub Begera (jakub@begera.cz) on 06/04/2019.
 */
@Module
class ActivityModule(private val activity: AppCompatActivity) {

    @ForActivity
    @Provides
    internal fun provideContext(): Context {
        return activity
    }

    @Provides
    internal fun provideFragmentManager(activity: AppCompatActivity): FragmentManager {
        return activity.supportFragmentManager
    }

}