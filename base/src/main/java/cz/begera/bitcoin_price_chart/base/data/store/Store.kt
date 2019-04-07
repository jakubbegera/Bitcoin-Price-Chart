package cz.begera.bitcoin_price_chart.base.data.store

import cz.begera.bitcoin_price_chart.base.data.store.Store.DiskStore
import cz.begera.bitcoin_price_chart.base.data.store.Store.MemoryStore
import io.reactivex.Maybe

/**
 * Interface for any type of store. Don't implement this directly,
 * use [MemoryStore] or [DiskStore] so it is more
 * descriptive.
 */
interface Store<Key, Value> {

    fun getAll(): Maybe<List<Value>>

    fun putSingular(value: Value)

    fun putAll(valueList: List<Value>)

    fun clear()

    fun getSingular(key: Key): Maybe<Value>

    /**
     * More descriptive interface for memory based stores.
     */
    interface MemoryStore<Key, Value> : Store<Key, Value>

    /**
     * More descriptive interface for disk based stores.
     */
    interface DiskStore<Key, Value> : Store<Key, Value>
}
