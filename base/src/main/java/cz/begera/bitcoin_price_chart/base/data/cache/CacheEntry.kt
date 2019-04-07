package cz.begera.bitcoin_price_chart.base.data.cache

/**
 * Cache entry that contains the object and the creation timestamp.
 */
data class CacheEntry<T>(val cachedObject: T, val creationTimestamp: Long)