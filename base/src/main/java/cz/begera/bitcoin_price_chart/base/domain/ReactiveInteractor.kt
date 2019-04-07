package cz.begera.bitcoin_price_chart.base.domain

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Interfaces for Interactors. This interfaces represent use cases
 * (this means any use case in the application should implement this contract).
 */
interface ReactiveInteractor {

    /**
     * Sends changes to data layer.
     * It returns a [Single] that will emit the result of the send operation.
     *
     * @param <Result> the type of the send operation result.
     * @param <Params> required parameters for the send.
    </Params></Result> */
    interface SendInteractor<Params, Result> : ReactiveInteractor {

        fun getSingle(params: Params?): Single<Result>
    }

    /**
     * Retrieves changes from the data layer.
     * It returns an [Flowable] that emits updates for the retrieved object. The returned [Flowable] will never complete, but it can
     * error if there are any problems performing the required actions to serve the data.
     *
     * @param <Object> the type of the retrieved object.
     * @param <Params> required parameters for the retrieve operation.
    </Params></Object> */
    interface RetrieveInteractor<Params, Object> : ReactiveInteractor {

        fun getBehaviorStream(params: Params): Observable<Object>
    }

    /**
     * The request interactor is used to request some result once. The returned observable is a single, emits once and then completes or errors.
     *
     * @param <Params> the type of the returned data.
     * @param <Result> required parameters for the request.
    </Result></Params> */
    interface RequestInteractor<Params, Result> : ReactiveInteractor {

        fun getSingle(params: Params): Single<Result>
    }

    /**
     * The delete interactor is used to delete entities from data layer. The response for the delete operation comes as onNext
     * event in the returned observable.
     *
     * @param <Result> the type of the delete response.
     * @param <Params>   required parameters for the delete.
    </Params></Result> */
    interface DeleteInteractor<Params, Result> : ReactiveInteractor {

        fun getSingle(params: Params): Single<Result>
    }

    /**
     * The refresh interactor is used to refresh the reactive store with new data. Typically calling this interactor will trigger events in its
     * get interactor counterpart. The returned observable will complete when the refresh is finished or error if there was any problem in the process.
     *
     * @param <Params> required parameters for the refresh.
    </Params> */
    interface RefreshInteractor<Params> : ReactiveInteractor {

        fun getRefreshSingle(params: Params): Completable
    }
}