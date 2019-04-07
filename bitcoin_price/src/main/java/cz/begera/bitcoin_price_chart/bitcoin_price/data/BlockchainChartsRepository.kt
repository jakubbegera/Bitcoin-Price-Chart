package cz.begera.bitcoin_price_chart.bitcoin_price.data

import cz.begera.bitcoin_price_chart.base.data.store.ReactiveStore
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import polanski.option.Option
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Jakub Begera (jakub@begera.cz) on 07/04/2019.
 */
@Singleton
class BlockchainChartsRepository @Inject constructor(
    private val service: BlockchainChartsService,
    private val store: ReactiveStore<String, BlockchainChart>,
    private val mapper: BlockchainChartMapper
) {

    fun getChart(timespan: Timespan): Observable<Option<BlockchainChart>> {
        return store.getSingular(timespan.key)
    }

    fun fetchChart(timespan: Timespan): Completable {
        return service.getBitcoinMarketPrice(timespan.key)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { t -> Pair(timespan, t) }
            .map(mapper)
            .doOnSuccess { store.storeSingular(it) }
            .ignoreElement()
    }
}