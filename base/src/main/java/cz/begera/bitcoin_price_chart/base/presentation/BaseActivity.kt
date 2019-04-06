package cz.begera.bitcoin_price_chart.base.presentation

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by Jakub Begera (jakub@begera.cz) on 06/04/2019.
 */
abstract class BaseActivity : AppCompatActivity() {

    @get:LayoutRes
    protected abstract val layoutId:  Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
    }


}