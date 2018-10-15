package com.arctouch.codechallenge.view.home

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.base.BaseActivity
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.viewModel.MovieListViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.home_activity.*

class HomeActivity : AppCompatActivity() {
    private lateinit var viewModel: MovieListViewModel

    private lateinit var  adapter: HomeAdapter

    private var recyclerState: Parcelable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        viewModel = ViewModelProviders.of(this).get(MovieListViewModel::class.java)
        adapter = HomeAdapter()

        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter
        loadList()


//        api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, 1, TmdbApi.DEFAULT_REGION)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe {
//                val moviesWithGenres = it.results.map { movie ->
//                    movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
//                }
//                recyclerView.adapter = HomeAdapter(moviesWithGenres)
//                progressBar.visibility = View.GONE
//            }
    }

    private fun loadList(){
        val disposable = viewModel.movieList
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ list ->
                                    progressBar.visibility = View.GONE
                            adapter.submitList(list)
                            if (recyclerState != null) {
                                recyclerView.layoutManager?.onRestoreInstanceState(recyclerState)
                                recyclerState = null
                            }
                        },
                        { e ->
                            Log.e("ArchTouch", "Error", e)
                        })
    }

}
