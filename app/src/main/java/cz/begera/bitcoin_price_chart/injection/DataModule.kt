package cz.begera.bitcoin_price_chart.injection

import cz.begera.bitcoin_price_chart.bitcoin_price.injection.BitcoinPriceDataModule
import dagger.Module

/**
 * Created by Jakub Begera (jakub@begera.cz) on 07/04/2019.
 */
@Module(includes = [BitcoinPriceDataModule::class])
class DataModule