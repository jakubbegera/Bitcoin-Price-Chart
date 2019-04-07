package cz.begera.bitcoin_price_chart.base.common.rx

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import polanski.option.Option
import polanski.option.OptionUnsafe

/**
 * Filters out all Option of NONE if any, but if Some, then unwraps and returns the value.
 */
class UnwrapOptionTransformer<T> : ObservableTransformer<Option<T>, T> {

    override fun apply(upstream: Observable<Option<T>>): ObservableSource<T> {
        return upstream.filter { it.isSome }
            .map { OptionUnsafe.getUnsafe(it) }
    }

    companion object {

        fun <T> create(): UnwrapOptionTransformer<T> {
            return UnwrapOptionTransformer()
        }
    }
}
