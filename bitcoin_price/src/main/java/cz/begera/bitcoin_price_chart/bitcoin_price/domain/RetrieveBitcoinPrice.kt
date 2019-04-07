package cz.begera.bitcoin_price_chart.bitcoin_price.domain

import cz.begera.bitcoin_price_chart.base.common.rx.UnwrapOptionTransformer
import cz.begera.bitcoin_price_chart.base.domain.ReactiveInteractor
import cz.begera.bitcoin_price_chart.bitcoin_price.data.BlockchainChart
import cz.begera.bitcoin_price_chart.bitcoin_price.data.BlockchainChartsRepository
import cz.begera.bitcoin_price_chart.bitcoin_price.data.Timespan
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.Single.just
import polanski.option.Option
import javax.inject.Inject

/**
 * Created by Jakub Begera (jakub@begera.cz) on 07/04/2019.
 */
class RetrieveBitcoinPrice @Inject constructor(
    private val repository: BlockchainChartsRepository
) : ReactiveInteractor.RetrieveInteractor<Timespan, BlockchainChart> {

    override fun getBehaviorStream(params: Timespan): Observable<BlockchainChart> {
        return repository.getChart(params)
            // fetch if emitted value is none
            .flatMapSingle { t -> fetchWhenNoneAndThenDrafts(params, t) }
            // unwrap if some, filter if none
            .compose(UnwrapOptionTransformer.create())

    }

    private fun fetchWhenNoneAndThenDrafts(
        timespan: Timespan,
        chart: Option<BlockchainChart>
    ): Single<Option<BlockchainChart>> {
        return fetchWhenNone(timespan, chart).andThen(just<Option<BlockchainChart>>(chart))
    }

    private fun fetchWhenNone(timespan: Timespan, chart: Option<BlockchainChart>): Completable {
        return if (chart.isNone)
            repository.fetchChart(timespan)
        else
            Completable.complete()
    }
}