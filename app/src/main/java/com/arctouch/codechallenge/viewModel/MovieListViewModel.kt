package com.arctouch.codechallenge.viewModel

import android.arch.lifecycle.ViewModel
import android.arch.paging.PagedList
import android.arch.paging.RxPagedListBuilder
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.data.dataSource.UpcomingDataSourceFactory
import com.arctouch.codechallenge.model.Movie
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieListViewModel: ViewModel() {
    var movieList: Observable<PagedList<Movie>>

    private val compositeDisposable = CompositeDisposable()

    private val upcomingDataSourceFactory: UpcomingDataSourceFactory

    private val pageSize = 13


    init {
        upcomingDataSourceFactory = UpcomingDataSourceFactory(compositeDisposable, TmdbApi.getService())

        val conf = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setPrefetchDistance(10)
                .setEnablePlaceholders(false)
                .build()

        movieList = RxPagedListBuilder(upcomingDataSourceFactory, conf)
                .setFetchScheduler(Schedulers.io())
                .buildObservable()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}