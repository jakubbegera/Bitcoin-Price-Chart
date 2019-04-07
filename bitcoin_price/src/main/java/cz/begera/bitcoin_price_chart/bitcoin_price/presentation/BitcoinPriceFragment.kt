package cz.begera.bitcoin_price_chart.bitcoin_price.presentation

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import cz.begera.bitcoin_price_chart.base.extensions.getBaseInjectingActivity
import cz.begera.bitcoin_price_chart.base.extensions.gone
import cz.begera.bitcoin_price_chart.base.extensions.visible
import cz.begera.bitcoin_price_chart.base.presentation.BaseInjectingFragment
import cz.begera.bitcoin_price_chart.bitcoin_price.R
import cz.begera.bitcoin_price_chart.bitcoin_price.data.BlockchainChart
import cz.begera.bitcoin_price_chart.bitcoin_price.data.Timespan
import cz.begera.bitcoin_price_chart.bitcoin_price.injection.BitcoinPriceComponent
import kotlinx.android.synthetic.main.fragment_bitcoin_price.*
import timber.log.Timber
import javax.inject.Inject


/**
 * Created by Jakub Begera (jakub@begera.cz) on 06/04/2019.
 */
class BitcoinPriceFragment : BaseInjectingFragment() {

    @Inject
    lateinit var viewModeFactory: ViewModelProvider.Factory
    lateinit var viewModel: BitcoinPriceViewModel

    override val layoutId = R.layout.fragment_bitcoin_price

    override fun onInject() {
        getBaseInjectingActivity<BitcoinPriceComponent.BitcoinPriceComponentCreator>()
            .component
            .createBitcoinPriceComponent()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chart.description.isEnabled = false

        // enable touch gestures
        chart.setTouchEnabled(true)

        // enable scaling and dragging
        chart.isDragEnabled = true
        chart.setScaleEnabled(false)

        chart.setDrawGridBackground(false)

        val x = chart.xAxis
        x.isEnabled = false

        chart.axisLeft.isEnabled = false
        chart.axisRight.isEnabled = false
        chart.legend.isEnabled = false


        chart.invalidate()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, this.viewModeFactory).get(BitcoinPriceViewModel::class.java)
        viewModel.creditListLiveData.observe(this, Observer { this.renderModel(it) })

        chips_timespan.setOnCheckedChangeListener { _, resID ->
            val timespan = when (resID) {
                R.id.chip_timespan_30days -> Timespan.DAYS30
                R.id.chip_timespan_60days -> Timespan.DAYS60
                R.id.chip_timespan_180days -> Timespan.DAYS180
                R.id.chip_timespan_1year -> Timespan.YEAR1
                R.id.chip_timespan_2years -> Timespan.YEAR2
                R.id.chip_timespan_all -> Timespan.ALL_TIME
                else -> {
                    Timber.e("Timespan chip button id $resID not recognized.")
                    return@setOnCheckedChangeListener
                }
            }
            viewModel.bindToBitcoinPrice(timespan)
        }

        chips_timespan.check(R.id.chip_timespan_30days)
    }

    private fun renderModel(model: BitcoinPriceModel) {
        when (model) {
            is BitcoinPriceModel.Loading -> {
                txv_error.gone()
                chart.gone()
                prb_loading.visible()

            }
            is BitcoinPriceModel.Error -> {
                txv_error.text = getString(R.string.chart_loading_error)
                chart.gone()
                prb_loading.gone()
                txv_error.visible()
            }
            is BitcoinPriceModel.Data -> {
                txv_error.gone()
                prb_loading.gone()
                chart.visible()

                renderChart(model.data)
            }
        }
    }

    private fun renderChart(data: BlockchainChart) {
        val entries = data.values.map { Entry(it.x.toFloat(), it.y.toFloat()) }

        // set data
        chart.data?.takeIf { it.dataSetCount != 0 }?.getDataSetByIndex(0)?.let { it as? LineDataSet }?.let { dataSet ->
            dataSet.values = entries
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()

        } ?: let {

            val lineDataSet = LineDataSet(entries, "price")
            lineDataSet.apply {
                mode = LineDataSet.Mode.CUBIC_BEZIER
                color = ContextCompat.getColor(chart.context, R.color.color_primary)
                lineWidth = 3f

                setDrawFilled(true)
                setDrawCircles(false)


                // set the filled area
                setDrawFilled(true)
                fillFormatter = IFillFormatter { _, _ -> chart.axisLeft.axisMinimum }
                val drawable = ContextCompat.getDrawable(chart.context, R.drawable.chart_bg)
                fillDrawable = drawable


            }

            val lineData = LineData(lineDataSet)
            lineData.apply {
                setDrawValues(false)
            }
            chart.data = lineData
        }

        chart.animateXY(300, 300, Easing.EaseInSine)

    }
}