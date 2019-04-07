package cz.begera.bitcoin_price_chart.base.data.store

import io.reactivex.Observable
import polanski.option.Option

/**
 * Interface for any kind of reactive store.
 */
interface ReactiveStore<Key, Value> {

    fun storeSingular(model: Value)

    fun storeAll(modelList: List<Value>)

    fun replaceAll(modelList: List<Value>)

    fun getSingular(key: Key): Observable<Option<Value>>

    fun getAll(): Observable<Option<List<Value>>>

}