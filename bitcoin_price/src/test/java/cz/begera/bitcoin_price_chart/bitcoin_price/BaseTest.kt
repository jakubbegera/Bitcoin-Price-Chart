package cz.begera.bitcoin_price_chart.bitcoin_price

import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.StrictStubs::class)
abstract class BaseTest {

    @get:Rule
    val rule = MockitoJUnit.rule()!!

    @get:Rule
    val overrideSchedulersRule = RxSchedulerOverrideRule()
}
