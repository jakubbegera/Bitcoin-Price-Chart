package cz.begera.bitcoin_price_chart.base.data.cache

import cz.begera.bitcoin_price_chart.base.common.providers.TimestampProvider
import cz.begera.bitcoin_price_chart.base.data.store.Store
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.ConcurrentHashMap

/**
 * Generic memory cache with timeout for the entries.
 */
class Cache<Key, Value>(
    private val timestampProvider: TimestampProvider,
    private val extractKeyFromModel: Function<Value, Key>,
    private val itemLifespanMs: Long?
) : Store.MemoryStore<Key, Value> {

    private val cache = ConcurrentHashMap<Key, CacheEntry<Value>>()

    override fun getAll(): Maybe<List<Value>> {
        return Observable.fromIterable(cache.values)
            .filter(this::notExpired)
            .map { cacheEntry: CacheEntry<Value> -> cacheEntry.cachedObject }
            .toList()
            .filter { it.isNotEmpty() }
            .subscribeOn(Schedulers.computation())
    }

    override fun putSingular(value: Value) {
        Single.fromCallable { extractKeyFromModel.apply(value) }
            .subscribeOn(Schedulers.computation())
            .subscribe { key -> cache[key] =
                CacheEntry(value, timestampProvider.currentTimeMillis())
            }
    }

    override fun putAll(valueList: List<Value>) {
        Observable.fromIterable<Value>(valueList)
            .toMap(
                extractKeyFromModel,
                Function<Value, CacheEntry<Value>> { value: Value ->
                    CacheEntry(
                        value,
                        timestampProvider.currentTimeMillis()
                    )
                })
            .subscribeOn(Schedulers.computation())
            .subscribe(cache::putAll)
    }

    override fun clear() {
        cache.clear()
    }

    override fun getSingular(key: Key): Maybe<Value> {
        return Maybe.fromCallable { cache.containsKey(key) }
            .filter { isPresent -> isPresent }
            .map { cache[key] }
            .filter(this::notExpired)
            .map { cacheEntry: CacheEntry<Value> -> cacheEntry.cachedObject }
            .subscribeOn(Schedulers.computation())
    }

    private fun notExpired(cacheEntry: CacheEntry<Value>): Boolean {
        return itemLifespanMs?.let { lifespanMs ->
            cacheEntry.creationTimestamp + lifespanMs > timestampProvider.currentTimeMillis()
        } ?: true
    }
}
