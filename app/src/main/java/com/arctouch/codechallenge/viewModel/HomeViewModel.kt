package com.arctouch.codechallenge.viewModel

import android.arch.lifecycle.ViewModel
import android.arch.paging.PagedList
import android.arch.paging.RxPagedListBuilder
import android.util.Log
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.data.dataSource.UpcomingDataSourceFactory
import com.arctouch.codechallenge.model.GenreResponse
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.API_KEY
import com.arctouch.codechallenge.util.DEFAULT_LANGUAGE
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers

class HomeViewModel: ViewModel() {
//    var genres: Observable<List<GenreResponse>>

    var movieList: Observable<PagedList<Movie>>

    private val compositeDisposable = CompositeDisposable()

    private val upcomingDataSourceFactory: UpcomingDataSourceFactory

    private val pageSize = 10


    init {
        upcomingDataSourceFactory = UpcomingDataSourceFactory(compositeDisposable, TmdbApi.getService())

        val conf = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setPrefetchDistance(8)
                .setEnablePlaceholders(true)
                .build()
        movieList = RxPagedListBuilder(upcomingDataSourceFactory, conf)
                .setFetchScheduler(Schedulers.io())
                .buildObservable()
    }

    private fun loadGenres() {
        TmdbApi.getService().genres(API_KEY, DEFAULT_LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe ({
                    Cache.cacheGenres(it.genres)
                    Log.d("the genres", "${it.genres}")
                }, { error ->
                    Log.d("the genres ZEBA", "${error.message}")
                })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}