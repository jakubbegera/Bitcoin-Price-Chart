package cz.begera.bitcoin_price_chart.base.common.rx

import cz.begera.bitcoin_price_chart.base.BaseTest
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import polanski.option.Option

class UnwrapOptionTransformerTest : BaseTest() {

    private var transformer: UnwrapOptionTransformer<Any>? = null

    @Before
    fun setUp() {
        transformer = UnwrapOptionTransformer()
    }

    @Test
    fun transformerFiltersOutNone() {
        val source = Observable.just(Option.none<Any>())

        val ts = TestObserver<Any>()
        transformer!!.apply(source).subscribe(ts)

        ts.assertNoValues()
    }

    @Test
    fun transformerUnwrapsSome() {
        val `object` = Any()
        val source = Observable.just(Option.ofObj(`object`))

        val ts = TestObserver<Any>()
        transformer!!.apply(source).subscribe(ts)
        ts.assertValue(`object`)
    }
}