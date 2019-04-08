package cz.begera.bitcoin_price_chart.base.data.cache

import cz.begera.bitcoin_price_chart.base.BaseTest
import cz.begera.bitcoin_price_chart.base.common.providers.TimestampProvider
import io.reactivex.functions.Function
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

/**
 * Created by Jakub Begera (jakub@begera.cz) on 08/04/2019.
 */
class CacheTest : BaseTest() {

    private val timeoutMs: Long = 1000

    @Mock
    private lateinit var timestampProvider: TimestampProvider

    private lateinit var cache: Cache<String, TestObject>

    @Before
    fun setUp() {
        cache = Cache(timestampProvider, Function { t -> t.id }, timeoutMs)
    }

    @Test
    fun getSingularCompletesWithNoEmissionsWhenCacheIsEmpty() {
        cache.getSingular("KEY").test().assertNoValues()
    }

    @Test
    fun getAllCompletesWithNoEmissionsWhenCacheIsEmpty() {
        cache.getAll().test().assertNoValues()
    }

    @Test
    fun storedObjectIsEmittedWhenItHasNotExpired() {
        val `object` = TestObject("1")
        val arrangeBuilder = ArrangeBuilder().withCurrentMoment(1)

        cache.putSingular(`object`)

        arrangeBuilder.withCurrentMoment(timeoutMs - 500)
        cache.getSingular(`object`.id).test().assertValue(`object`)
    }

    @Test
    fun storedObjectIsNotEmittedWhenItHasExpired() {
        val `object` = TestObject("1")
        val arrangeBuilder = ArrangeBuilder().withCurrentMoment(1)

        cache.putSingular(`object`)

        arrangeBuilder.withCurrentMoment(timeoutMs + 500)
        cache.getSingular(`object`.id).test().assertNoValues()
    }

    @Test
    fun storedObjectsAreEmittedWhenTheyHaveNotExpired() {
        val listToStore = object : ArrayList<TestObject>() {
            init {
                add(TestObject("1"))
                add(TestObject("2"))
                add(TestObject("3"))
            }
        }
        val arrangeBuilder = ArrangeBuilder()

        arrangeBuilder.withCurrentMoment(1)
        cache.putAll(listToStore)

        arrangeBuilder.withCurrentMoment(timeoutMs - 500)
        cache.getAll().test().assertValue(listToStore)
    }

    @Test
    fun storedObjectsAreNotEmittedWhenTheyHaveExpired() {
        val listToStore = object : ArrayList<TestObject>() {
            init {
                add(TestObject("1"))
                add(TestObject("2"))
                add(TestObject("3"))
            }
        }
        val arrangeBuilder = ArrangeBuilder()

        arrangeBuilder.withCurrentMoment(1)
        cache.putAll(listToStore)

        arrangeBuilder.withCurrentMoment(timeoutMs + 500)
        cache.getAll().test().assertNoValues()
    }

    @Test
    fun getAllEmitsListWithAllNonExpiredValues() {
        val object1 = TestObject("1")
        val object2 = TestObject("2")
        val object3 = TestObject("3")
        val arrangeBuilder = ArrangeBuilder()

        arrangeBuilder.withCurrentMoment(1)
        cache.putSingular(object1)

        arrangeBuilder.withCurrentMoment(100)
        cache.putSingular(object2)

        arrangeBuilder.withCurrentMoment(500)
        cache.putSingular(object3)

        arrangeBuilder.withCurrentMoment(timeoutMs + 50)
        val expected = object : ArrayList<TestObject>() {
            init {
                add(object2)
                add(object3)
            }
        }
        cache.getAll().test().assertValue(expected)
    }

    @Test
    fun clearRemovesAllTheItemsFromTheCache() {
        val listToStore = object : ArrayList<TestObject>() {
            init {
                add(TestObject("1"))
                add(TestObject("2"))
                add(TestObject("3"))
            }
        }
        ArrangeBuilder().withCurrentMoment(1)
        cache.putAll(listToStore)

        cache.clear()

        cache.getAll().test().assertNoValues()
        cache.getSingular("1").test().assertNoValues()
        cache.getSingular("2").test().assertNoValues()
        cache.getSingular("3").test().assertNoValues()
    }

    data class TestObject(val id: String)

    private inner class ArrangeBuilder {

        fun withCurrentMoment(currentMoment: Long): ArrangeBuilder {
            Mockito.`when`(timestampProvider.currentTimeMillis()).thenReturn(currentMoment)
            return this
        }
    }

}