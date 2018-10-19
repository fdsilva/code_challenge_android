package com.arctouch.codechallenge.viewModel

import android.arch.lifecycle.ViewModel
import android.arch.paging.PagedList
import android.arch.paging.RxPagedListBuilder
import android.content.Intent
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.data.dataSource.UpcomingDataSourceFactory
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.API_KEY
import com.arctouch.codechallenge.util.DEFAULT_LANGUAGE
import com.arctouch.codechallenge.view.home.HomeActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * The home view model is responsible to handle datasource requests
 * notify the view when data is fetched and cache the genres list
 * which was previously created in the room splash Screen
 * */
class HomeViewModel: ViewModel() {
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

    fun getGenres(){
        TmdbApi.getService().genres(API_KEY, DEFAULT_LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Cache.cacheGenres(it.genres)
                }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}