package cz.begera.bitcoin_price_chart.bitcoin_price.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cz.begera.bitcoin_price_chart.bitcoin_price.data.BlockchainChart
import cz.begera.bitcoin_price_chart.bitcoin_price.data.Timespan
import cz.begera.bitcoin_price_chart.bitcoin_price.domain.RetrieveBitcoinPrice
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Jakub Begera (jakub@begera.cz) on 06/04/2019.
 */
class BitcoinPriceViewModel @Inject constructor(
    private val retrieveBitcoinPrice: RetrieveBitcoinPrice
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val creditListLiveData = MutableLiveData<BlockchainChart>()

    init {
        compositeDisposable.add(bindToBitcoinPrice())
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    private fun bindToBitcoinPrice(): Disposable {
        return retrieveBitcoinPrice.getBehaviorStream(Timespan.DAYS30)
            .observeOn(Schedulers.computation())
            .subscribe(
                {
                    creditListLiveData.postValue(it)
                },
                { e ->
                    Timber.e(e, "Error updating credit list live data")
                }
            )
    }
}