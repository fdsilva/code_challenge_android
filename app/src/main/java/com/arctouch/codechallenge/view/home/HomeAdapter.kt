package com.arctouch.codechallenge.view.home

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.MovieImageUrlBuilder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.movie_item.view.*

/**
 * The Home adapter was changed to handle a pagedList
 * */
class HomeAdapter: PagedListAdapter<Movie, HomeAdapter.ViewHolder>(movieDiffUtil) {

    /** A Subscriber for the movie selection*/
    private val clickMovieSubject = PublishSubject.create<Movie>()

    /** A Click Event for the movie selection*/
    val clickEvent: Observable<Movie> = clickMovieSubject

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var movie = getItem(position)
        holder.bind(movie!!)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val movieImageUrlBuilder = MovieImageUrlBuilder()

        init {
            itemView.setOnClickListener{
                getItem(layoutPosition)?.let { it1 -> clickMovieSubject.onNext(it1) }
            }
        }

        fun bind(movie: Movie) {
            movie!!.genres = Cache.genres.filter { movie!!.genreIds?.contains(it.id) == true }
            itemView.titleTextView.text = movie.title
            itemView.genresTextView.text = movie.genres?.joinToString(separator = ", ") { it.name }
            itemView.releaseDateTextView.text = movie.releaseDate

            Glide.with(itemView)
                .load(movie.posterPath?.let { movieImageUrlBuilder.buildPosterUrl(it) })
                .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                .into(itemView.posterImageView)
        }
    }

    companion object {
        val movieDiffUtil = object: DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie?, newItem: Movie?): Boolean {
                return oldItem!!.id == newItem!!.id
            }

            override fun areContentsTheSame(oldItem: Movie?, newItem: Movie?): Boolean {
                return oldItem == newItem
            }

        }
    }
}
