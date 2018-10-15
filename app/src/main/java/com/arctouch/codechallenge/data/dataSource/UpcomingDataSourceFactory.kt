package com.arctouch.codechallenge.data.dataSource

import android.arch.paging.DataSource
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.model.Movie
import io.reactivex.disposables.CompositeDisposable


class UpcomingDataSourceFactory(private val compisiteDisposable: CompositeDisposable,
                                private val tmdbAPI: TmdbApi): DataSource.Factory<Int, Movie>(){

    override fun create(): DataSource<Int, Movie> {
        return UpcomingDataSource(tmdbAPI, compisiteDisposable)
    }
}