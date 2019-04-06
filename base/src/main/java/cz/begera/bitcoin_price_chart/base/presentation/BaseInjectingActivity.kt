package cz.begera.bitcoin_price_chart.base.presentation

import android.os.Bundle

/**
 * Created by Jakub Begera (jakub@begera.cz) on 06/04/2019.
 */
abstract class BaseInjectingActivity<Component : Any> : BaseActivity() {

    lateinit var component: Component

    override fun onCreate(savedInstanceState: Bundle?) {
        component = createComponent()
        onInject(component)

        super.onCreate(savedInstanceState)
    }

    protected abstract fun onInject(component: Component)

    protected abstract fun createComponent(): Component
}