package com.arctouch.codechallenge.view.home

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.viewModel.HomeViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.home_activity.*

class HomeActivity : AppCompatActivity() {
    private lateinit var viewModel: HomeViewModel

    private lateinit var  adapter: HomeAdapter

    private var recyclerState: Parcelable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        adapter = HomeAdapter()

        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter
        loadList()

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
                }, { e ->
                    Log.e("ArchTouch", "Error", e)
                })
    }
}
