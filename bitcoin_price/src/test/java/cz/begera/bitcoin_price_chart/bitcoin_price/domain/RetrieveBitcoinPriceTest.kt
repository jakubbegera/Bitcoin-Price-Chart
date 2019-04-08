package cz.begera.bitcoin_price_chart.bitcoin_price.domain

import cz.begera.bitcoin_price_chart.bitcoin_price.BaseTest
import cz.begera.bitcoin_price_chart.bitcoin_price.blockchainChart
import cz.begera.bitcoin_price_chart.bitcoin_price.data.BlockchainChart
import cz.begera.bitcoin_price_chart.bitcoin_price.data.BlockchainChartsRepository
import cz.begera.bitcoin_price_chart.bitcoin_price.data.Timespan
import io.reactivex.Completable
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.BehaviorSubject
import org.junit.Before
import org.junit.Test

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import polanski.option.Option

/**
 * Created by Jakub Begera (jakub@begera.cz) on 08/04/2019.
 */
class RetrieveBitcoinPriceTest : BaseTest() {


    @Mock
    private lateinit var repository: BlockchainChartsRepository

    private lateinit var interactor: RetrieveBitcoinPrice

    private lateinit var arrangeBuilder: ArrangeBuilder

    private lateinit var ts: TestObserver<BlockchainChart>

    @Before
    fun setUp() {
        interactor = RetrieveBitcoinPrice(repository)
        arrangeBuilder = ArrangeBuilder()
        ts = TestObserver()
    }

    @Test
    fun chartsFromRepoAreUnwrappedAndPassedOn() {
        // Arrange
        val chart = blockchainChart
        interactor.getBehaviorStream(Timespan.DAYS30).subscribe(ts)

        // Act
        arrangeBuilder.emitChartFromRepo(Option.ofObj(chart))

        // Assert
        ts.assertNotTerminated()
        ts.assertValue(chart)
    }

    @Test
    fun whenRepoIsEmptyFetchAndNoEmissions() {
        // Arrange
        arrangeBuilder.emitChartFromRepo(Option.none())
            .withSuccessfulFetch()

        // Act
        interactor.getBehaviorStream(Timespan.DAYS30).subscribe(ts)

        // Assert
        verify(repository).fetchChart(Timespan.DAYS30)
        ts.assertNoValues()
        ts.assertNotTerminated()
    }

    @Test
    fun propagateFetchError() {
        // Arrange
        val throwable = Mockito.mock(Throwable::class.java)
        arrangeBuilder.emitChartFromRepo(Option.none())
            .withFetchError(throwable)

        // Act
        interactor.getBehaviorStream(Timespan.DAYS30).subscribe(ts)

        // Assert
        verify(repository).fetchChart(Timespan.DAYS30)
        ts.assertNoValues()
        ts.assertError(throwable)
    }

    private inner class ArrangeBuilder {

        private val repoChartStream = BehaviorSubject.create<Option<BlockchainChart>>()

        init {
            Mockito.`when`(repository.getChart(Timespan.DAYS30)).thenReturn(repoChartStream)
        }

        fun emitChartFromRepo(option: Option<BlockchainChart>): ArrangeBuilder {
            repoChartStream.onNext(option)
            return this
        }

        fun withSuccessfulFetch(): ArrangeBuilder {
            Mockito.`when`(repository.fetchChart(Timespan.DAYS30)).thenReturn(Completable.complete())
            return this
        }

        fun withFetchError(throwable: Throwable): ArrangeBuilder {
            Mockito.`when`(repository.fetchChart(Timespan.DAYS30)).thenReturn(Completable.error(throwable))
            return this
        }
    }
}