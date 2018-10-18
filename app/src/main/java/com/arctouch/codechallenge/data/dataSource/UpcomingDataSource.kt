package com.arctouch.codechallenge.data.dataSource

import android.arch.paging.PageKeyedDataSource
import android.util.Log
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.API_KEY
import com.arctouch.codechallenge.util.DEFAULT_LANGUAGE
import com.arctouch.codechallenge.util.DEFAULT_REGION
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class UpcomingDataSource(private val tmdbApi: TmdbApi,
                         private val compositeDisposable: CompositeDisposable):

PageKeyedDataSource<Int, Movie>() {
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Movie>) {
        val numberOfItems = params.requestedLoadSize
        createObservable(1, 2, numberOfItems, callback, null)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        val page = params.key
        val numberOfItems = params.requestedLoadSize
        createObservable(page, page + 1, numberOfItems, null, callback)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        val page = params.key
        val numberOfItems = params.requestedLoadSize
        createObservable(page, page - 1, numberOfItems, null, callback)
    }

    private fun createObservable(requestedPage: Int,
                                 adjacentPage: Int,
                                 requestedLoadSize: Int,
                                 initialCallback: LoadInitialCallback<Int, Movie>?,
                                 callback: LoadCallback<Int, Movie>?) {
        compositeDisposable.add(
                tmdbApi.upcomingMovies(API_KEY, DEFAULT_LANGUAGE, requestedPage.toLong(), DEFAULT_REGION)
                        .subscribe(
                                { response ->
                                    Log.d("CodeChallenge", "Loading page: $requestedPage")
                                    initialCallback?.onResult(response.results, null, adjacentPage)
                                    callback?.onResult(response.results, adjacentPage)
                                },
                                { t ->
                                    Log.d("CodeChallenge", "Error loading page: $requestedPage", t)
                                }
                        )
        )
    }
}