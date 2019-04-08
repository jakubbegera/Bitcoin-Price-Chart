package cz.begera.bitcoin_price_chart.utils

import android.util.Log
import com.crashlytics.android.Crashlytics
import timber.log.Timber

/**
 * Created by Jakub Begera (jakub@begera.cz) on 08/04/2019.
 */
class ReleaseTimberTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        logNormalMessage(priority, tag, message, t)
    }

    private fun logNormalMessage(priority: Int, tag: String?, message: String, t: Throwable?) {
        when (priority) {
            in Log.VERBOSE..Log.INFO -> return // we doesn't want to collect everything
            else -> {
                if (t != null) {
                    Crashlytics.logException(t)
                } else {
                    Crashlytics.log(priority, tag, message)
                }
            }
        }
    }

}