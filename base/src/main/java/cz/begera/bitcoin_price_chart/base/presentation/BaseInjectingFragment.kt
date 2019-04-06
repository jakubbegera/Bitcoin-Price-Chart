package cz.begera.bitcoin_price_chart.base.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

/**
 * Created by Jakub Begera (jakub@begera.cz) on 06/04/2019.
 */
abstract class BaseInjectingFragment : Fragment() {

    @get:LayoutRes
    protected abstract val layoutId: Int

    override fun onAttach(context: Context) {
        onInject()
        super.onAttach(context)
    }

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(layoutId, container, false)

    abstract fun onInject()
}
