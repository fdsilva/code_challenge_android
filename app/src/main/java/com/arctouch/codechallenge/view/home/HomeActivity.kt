package com.arctouch.codechallenge.view.home

import android.app.DialogFragment
import android.app.FragmentManager
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.model.Genre
import com.arctouch.codechallenge.view.home.detail.MovieDetailFragment
import com.arctouch.codechallenge.viewModel.HomeViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.home_activity.*

class HomeActivity : AppCompatActivity() {
    private lateinit var viewModel: HomeViewModel

    private lateinit var  adapter: HomeAdapter

    private var recyclerState: Parcelable? = null

    private var itemSelectedSubscribe: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.home_activity)

        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        viewModel.getGenres()
        adapter = HomeAdapter()

        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter
        loadList()
        subscribeForCLickEvent()

    }

    /**
     * Loads the movie list
     * */
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

    /**
     * Captures the envet os click on a list item and present the detail fragment
     * */
    fun subscribeForCLickEvent(){
        itemSelectedSubscribe = adapter.clickEvent.subscribe {
            val movieDetailFragment = MovieDetailFragment.newInstance(it)
            movieDetailFragment.setStyle(DialogFragment.STYLE_NO_TITLE,
                    android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
            movieDetailFragment.show(supportFragmentManager, "Sample Fragment")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        itemSelectedSubscribe?.dispose()
    }
}
