package cz.begera.bitcoin_price_chart.base.data.store

import cz.begera.bitcoin_price_chart.base.BaseTest
import cz.begera.bitcoin_price_chart.base.data.cache.Cache
import io.reactivex.Maybe
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import polanski.option.Option.none
import polanski.option.Option.ofObj
import java.util.*

class MemoryReactiveStoreTest : BaseTest() {


    @Mock
    private lateinit var cache: Cache<String, TestObject>

    private lateinit var reactiveStore: MemoryReactiveStore<String, TestObject>

    @Before
    fun setUp() {
        reactiveStore = MemoryReactiveStore({ it.id }, cache)
    }

    @Test
    fun noneIsEmittedWhenCacheIsEmpty() {
        ArrangeBuilder().withEmptyCache()

        reactiveStore.getSingular("ID1").test().assertValue(none())
        reactiveStore.getAll().test().assertValue(none())
    }

    @Test
    fun lastStoredObjectIsEmittedAfterSubscription() {
        val model = TestObject("ID1")
        ArrangeBuilder().withCachedObject(model)
            .withCachedObjectList(listOf(model))

        reactiveStore.storeSingular(model)
        reactiveStore.getSingular("ID1").test().assertValue(ofObj(model))
    }

    @Test
    fun singularStreamEmitsWhenSingleObjectIsStored() {
        val model = TestObject("ID1")
        ArrangeBuilder().withEmptyCache()

        val to = reactiveStore.getSingular("ID1").test()
        reactiveStore.storeSingular(model)

        to.assertValueAt(1) { testObjectOption -> testObjectOption == ofObj(model) }
    }

    @Test
    fun allStreamEmitsWhenSingleObjectIsStored() {
        val list = createTestObjectList()
        ArrangeBuilder().withCachedObjectList(list)

        val to = reactiveStore.getAll().test()
        reactiveStore.storeSingular(TestObject(""))

        Mockito.verify(cache, Mockito.times(2)).getAll()
        to.assertValueAt(1) { listOption -> listOption == ofObj(list) }
    }

    @Test
    fun singularStreamEmitsWhenObjectListIsStored() {
        val model = TestObject("ID1")
        ArrangeBuilder().withCachedObject(model)

        val to = reactiveStore.getSingular("ID1").test()
        reactiveStore.storeAll(createTestObjectList())

        to.assertValueAt(1) { testObjectOption -> testObjectOption == ofObj(model) }
    }

    @Test
    fun allStreamEmitsWhenObjectListIsStored() {
        val list = createTestObjectList()
        ArrangeBuilder().withCachedObjectList(list)

        val to = reactiveStore.getAll().test()
        reactiveStore.storeAll(list)

        to.assertValueAt(1) { listOption -> listOption == ofObj(list) }
    }

    @Test
    fun singularStreamEmitsWhenObjectListIsReplaced() {
        val model = TestObject("ID1")
        ArrangeBuilder().withCachedObject(model)

        val to = reactiveStore.getSingular("ID1").test()
        reactiveStore.replaceAll(createTestObjectList())

        to.assertValueAt(1) { testObjectOption -> testObjectOption == ofObj(model) }
    }

    @Test
    fun allStreamEmitsWhenObjectListIsReplaced() {
        val list = createTestObjectList()
        ArrangeBuilder().withCachedObjectList(list)

        val to = reactiveStore.getAll().test()
        reactiveStore.replaceAll(list)

        to.assertValueAt(1) { listOption -> listOption == ofObj(list) }
    }

    @Test
    fun objectIsStoredInCache() {
        val model = TestObject("ID1")
        ArrangeBuilder().withCachedObjectList(listOf(model))

        reactiveStore.storeSingular(model)

        Mockito.verify(cache).putSingular(model)
    }

    @Test
    fun objectListIsStoredInCache() {
        val list = createTestObjectList()

        reactiveStore.storeAll(list)

        Mockito.verify(cache).putAll(list)
    }

    @Test
    fun cacheIsClearedInReplaceAll() {
        val list = createTestObjectList()

        reactiveStore.replaceAll(list)

        Mockito.verify(cache).clear()
    }

    private fun createTestObjectList(): List<TestObject> {
        return object : ArrayList<TestObject>() {
            init {
                add(TestObject("ID1"))
                add(TestObject("ID2"))
                add(TestObject("ID3"))
            }
        }
    }

    private data class TestObject(val id: String)

    private inner class ArrangeBuilder {

        fun withCachedObject(`object`: TestObject): ArrangeBuilder {
            Mockito.`when`(cache.getSingular(`object`.id)).thenReturn(Maybe.just(`object`))
            return this
        }

        fun withCachedObjectList(objectList: List<TestObject>): ArrangeBuilder {
            Mockito.`when`(cache.getAll()).thenReturn(Maybe.just(objectList))
            return this
        }

        fun withEmptyCache(): ArrangeBuilder {
            Mockito.`when`(cache.getSingular(Mockito.anyString())).thenReturn(Maybe.empty())
            Mockito.`when`(cache.getAll()).thenReturn(Maybe.empty())
            return this
        }
    }

}