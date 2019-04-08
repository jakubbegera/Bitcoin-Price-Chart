package cz.begera.bitcoin_price_chart.base.common.providers

import cz.begera.bitcoin_price_chart.base.BaseTest
import org.junit.Before
import org.junit.Test

class TimestampProviderTest : BaseTest() {

    private lateinit var provider: TimestampProvider

    @Before
    fun setUp() {
        provider = TimestampProvider()
    }

    @Test
    fun currentTimeMillis() {
        assert(Math.abs(System.currentTimeMillis() - provider.currentTimeMillis()) < 10L)
    }

}