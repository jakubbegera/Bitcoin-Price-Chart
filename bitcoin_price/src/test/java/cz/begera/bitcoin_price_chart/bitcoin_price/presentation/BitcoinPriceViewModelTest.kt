package cz.begera.bitcoin_price_chart.bitcoin_price.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import cz.begera.bitcoin_price_chart.bitcoin_price.BaseTest
import cz.begera.bitcoin_price_chart.bitcoin_price.blockchainChart
import cz.begera.bitcoin_price_chart.bitcoin_price.data.BlockchainChart
import cz.begera.bitcoin_price_chart.bitcoin_price.data.Timespan
import cz.begera.bitcoin_price_chart.bitcoin_price.domain.RetrieveBitcoinPrice
import io.reactivex.subjects.BehaviorSubject
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

/**
 * Created by Jakub Begera (jakub@begera.cz) on 08/04/2019.
 */
class BitcoinPriceViewModelTest : BaseTest() {

    @get:Rule
    val archComponentsRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var interactor: RetrieveBitcoinPrice

    private lateinit var viewModel: BitcoinPriceViewModel

    private lateinit var arrangeBuilder: ArrangeBuilder

    @Before
    fun setUp() {
        arrangeBuilder = ArrangeBuilder()
        viewModel = BitcoinPriceViewModel(interactor)
    }

    @Test
    fun itemsGoIntoLiveDataWhenInteractorEmitsChart() {
        val chart = blockchainChart

        arrangeBuilder.interactorEmitsChart(chart)

        viewModel.bindToBitcoinPrice(Timespan.DAYS30)

        assertThat((viewModel.liveData.value as? BitcoinPriceModel.Data)?.data).isEqualTo(chart)
    }

    private inner class ArrangeBuilder {

        internal var interactorSubject = BehaviorSubject.create<BlockchainChart>()

        init {
            Mockito.`when`(interactor.getBehaviorStream(Timespan.DAYS30)).thenReturn(interactorSubject)
        }

        fun interactorEmitsChart(chart: BlockchainChart): ArrangeBuilder {
            interactorSubject.onNext(chart)
            return this
        }

    }
}