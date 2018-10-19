package com.arctouch.codechallenge.view.home.detail

import android.support.v4.app.DialogFragment
import android.content.Context

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.MovieImageUrlBuilder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.detail_fragment.view.*

/**
 * The movie fragment detail displays the data for the movie selected on the movie list
 * */
class MovieDetailFragment: DialogFragment() {
    private val movieImageUrlBuilder = MovieImageUrlBuilder()

    companion object {
        const val SELECTED_MOVIE_KEY: String = "SelectedMovie"

        fun newInstance(movie: Movie): MovieDetailFragment {

            var args = Bundle()
            args.putSerializable(SELECTED_MOVIE_KEY, movie)
            var movieDetailFragment = MovieDetailFragment()
            movieDetailFragment.arguments = args

            return movieDetailFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.detail_fragment, container, false)

        var bundleInfo = arguments
        setupView(bundleInfo!!.get(SELECTED_MOVIE_KEY) as Movie, view)
        return view
    }

    /**
     *  Populates the layout with movie infos
     * */
    fun setupView(movie: Movie, view: View){

        Glide.with(view)
                .load(movie.posterPath?.let { movieImageUrlBuilder.buildPosterUrl(it) })
                .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder).centerCrop())
                .into(view.imgvMoviePoster)


        Glide.with(view)
                .load(movie.backdropPath?.let { movieImageUrlBuilder.buildPosterUrl(it) })
                .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                .into(view.imgvBackdrop)

        view.tvMovieTitle.text = movie.title
        view.tvGenreList.text = movie.genres?.joinToString(separator = ", ") { it.name }
        view.tvRelizeDate.text = movie.releaseDate
        view.tvOverview.text = movie.overview


    }
}