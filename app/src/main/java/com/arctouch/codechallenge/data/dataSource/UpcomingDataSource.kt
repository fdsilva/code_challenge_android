package com.arctouch.codechallenge.data.dataSource

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource
import android.util.Log
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.data.RequestStatus
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.model.UpcomingMoviesResponse
import com.arctouch.codechallenge.util.API_KEY
import com.arctouch.codechallenge.util.DEFAULT_LANGUAGE
import com.arctouch.codechallenge.util.DEFAULT_REGION
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.functions.Action


class UpcomingDataSource(private val tmdbApi: TmdbApi,
                         private val compositeDisposable: CompositeDisposable):
PageKeyedDataSource<Long, Movie>() {
    val networkState = MutableLiveData<RequestStatus>()
    val initialLoad = MutableLiveData<RequestStatus>()

    private var retry: Completable? = null

    fun retry() {
        if(retry !=null) {
            compositeDisposable.add(retry!!
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ }, { throwable -> Log.e("Error on retry",throwable.message) }))
        }
    }

    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Long, Movie>) {
        networkState.postValue(RequestStatus.LOADING)
        initialLoad.postValue(RequestStatus.LOADING)

        compositeDisposable.add(tmdbApi.upcomingMovies(API_KEY, DEFAULT_LANGUAGE, 1, DEFAULT_REGION)
                .subscribe({ response ->
                    setRetry(null)
                    networkState.postValue(RequestStatus.LOADED)
                    initialLoad.postValue(RequestStatus.LOADED)
                    callback?.onResult(response.results, null, 2)
                }, {throwable ->

                    setRetry(Action { loadInitial(params, callback) })
                    val error = RequestStatus.error(throwable.message)

                    networkState.postValue(error)
                    initialLoad.postValue(error)
                })
        )
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, Movie>) {
        var page = params.key

        networkState.postValue(RequestStatus.LOADING)
        initialLoad.postValue(RequestStatus.LOADING)

        compositeDisposable.add(tmdbApi.upcomingMovies(API_KEY, DEFAULT_LANGUAGE, page, DEFAULT_REGION)
                .subscribe({ response ->
                    setRetry(null)
                    networkState.postValue(RequestStatus.LOADED)
                    initialLoad.postValue(RequestStatus.LOADED)
                    callback?.onResult(response.results, page + 1)
                }, {throwable ->

                    setRetry(Action { loadAfter(params, callback) })
                    val error = RequestStatus.error(throwable.message)

                    networkState.postValue(error)
                    initialLoad.postValue(error)
                })
        )
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, Movie>) {

    }

    private fun setRetry(action: Action?) {
        if (action == null) {
            this.retry = null
        } else {
            this.retry = Completable.fromAction(action)
        }
    }
}