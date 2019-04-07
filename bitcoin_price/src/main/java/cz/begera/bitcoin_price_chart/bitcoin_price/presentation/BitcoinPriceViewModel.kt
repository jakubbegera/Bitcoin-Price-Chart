package cz.begera.bitcoin_price_chart.bitcoin_price.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cz.begera.bitcoin_price_chart.bitcoin_price.data.Timespan
import cz.begera.bitcoin_price_chart.bitcoin_price.domain.RetrieveBitcoinPrice
import io.reactivex.disposables.CompositeDisposable
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

    val creditListLiveData = MutableLiveData<BitcoinPriceModel>()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun bindToBitcoinPrice(timespan: Timespan) {
        compositeDisposable.clear()
        creditListLiveData.postValue(BitcoinPriceModel.Loading)
        retrieveBitcoinPrice.getBehaviorStream(timespan)
            .observeOn(Schedulers.computation())
            .subscribe(
                {
                    creditListLiveData.postValue(BitcoinPriceModel.Data(it))
                },
                { e ->
                    Timber.e(e, "Error updating credit list live data")
                    creditListLiveData.postValue(BitcoinPriceModel.Error)
                }
            )
            .apply { compositeDisposable.add(this) }
    }
}