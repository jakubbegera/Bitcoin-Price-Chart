package cz.begera.bitcoin_price_chart.bitcoin_price.data

import cz.begera.bitcoin_price_chart.bitcoin_price.BaseTest
import cz.begera.bitcoin_price_chart.bitcoin_price.blockchainChartRaw
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Created by Jakub Begera (jakub@begera.cz) on 08/04/2019.
 */
class BlockchainChartMapperTest : BaseTest() {

    lateinit var mapper: BlockchainChartMapper

    @Before
    fun setUp() {
        mapper = BlockchainChartMapper()
    }

    @Test
    fun apply() {
        mapper.apply(Pair(
            Timespan.DAYS30,
            blockchainChartRaw
        )).apply {
            Assert.assertEquals("USD", unit)
            Assert.assertEquals(100, values.size)
            Assert.assertEquals(990.0, values.last().y, 0.001)
        }
    }
}