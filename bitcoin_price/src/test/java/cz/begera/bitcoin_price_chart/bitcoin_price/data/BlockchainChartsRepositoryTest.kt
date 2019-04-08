package cz.begera.bitcoin_price_chart.bitcoin_price.data

import cz.begera.bitcoin_price_chart.base.data.store.ReactiveStore
import cz.begera.bitcoin_price_chart.bitcoin_price.BaseTest
import cz.begera.bitcoin_price_chart.bitcoin_price.blockchainChartRaw
import io.reactivex.Observable
import io.reactivex.Single
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import polanski.option.Option

/**
 * Created by Jakub Begera (jakub@begera.cz) on 08/04/2019.
 */
class BlockchainChartsRepositoryTest : BaseTest() {

    @Mock
    private lateinit var store: ReactiveStore<String, BlockchainChart>

    @Mock
    private lateinit var service: BlockchainChartsService

    @Mock
    private lateinit var mapper: BlockchainChartMapper

    private lateinit var repository: BlockchainChartsRepository

    @Before
    fun setUp() {
        repository = BlockchainChartsRepository(service, store, mapper)
    }

    @Test
    fun getBlockchainChartReturnsStoreObservable() {
        val storeObservable = Observable.empty<Option<BlockchainChart>>()
        ArrangeBuilder().withObservableFromStore(storeObservable)

        assertThat(repository.getChart(Timespan.DAYS30)).isEqualTo(storeObservable)
    }

    @Test
    fun fetchChartEmitsErrorWhenNetworkServiceErrors() {
        val throwable = Mockito.mock(Throwable::class.java)
        ArrangeBuilder().withErrorInBlockchainPriceFromService(throwable)

        repository.fetchChart(Timespan.DAYS30).test().assertError(throwable)
    }

    @Test
    @Throws(Exception::class)
    fun chartRawItemsFromServiceAreMapped() {
        val rawList = blockchainChartRaw
        ArrangeBuilder().withChartFromService(rawList)
            .withMappedChart(Mockito.mock(BlockchainChart::class.java))

        repository.fetchChart(Timespan.DAYS30).subscribe()

        Mockito.verify(mapper).apply(Pair(Timespan.DAYS30, blockchainChartRaw))
    }

    private inner class ArrangeBuilder {

        fun withObservableFromStore(observable: Observable<Option<BlockchainChart>>): ArrangeBuilder {
            Mockito.`when`(store.getSingular(Timespan.DAYS30.key)).thenReturn(observable)
            return this
        }

        fun withChartFromService(raw: BlockchainChartRaw): ArrangeBuilder {
            Mockito.`when`(service.getBitcoinMarketPrice(Timespan.DAYS30.key)).thenReturn(Single.just(raw))
            return this
        }

        fun withErrorInBlockchainPriceFromService(error: Throwable): ArrangeBuilder {
            Mockito.`when`(service.getBitcoinMarketPrice(Timespan.DAYS30.key)).thenReturn(Single.error(error))
            return this
        }

        @Throws(Exception::class)
        fun withMappedChart(chart: BlockchainChart): ArrangeBuilder {
            Mockito.`when`(
                mapper.apply(
                    Pair(
                        Timespan.DAYS30,
                        blockchainChartRaw
                    )
                )
            ).thenReturn(chart)
            return this
        }
    }
}