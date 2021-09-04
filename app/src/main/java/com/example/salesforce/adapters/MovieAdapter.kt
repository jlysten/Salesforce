package com.example.salesforce.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.salesforce.R
import com.example.salesforce.databinding.MovieItemBinding
import com.example.salesforce.db.entity.Favorites
import com.example.salesforce.db.entity.Search
import com.example.salesforce.di.SalesforceComponentHolder
import com.example.salesforce.repository.MovieRepository
import com.example.salesforce.util.SalesforceConstants.EMPTY
import com.squareup.picasso.Picasso
import javax.inject.Inject

class MovieAdapter(
    private val list: List<Search>? = null,
    private val favoritesList: List<Favorites>? = null,
    private val context: Context,
    val listener: (Int, String) -> Unit
) :
    RecyclerView.Adapter<MovieAdapter.MoviesViewHolder>() {

    @Inject
    lateinit var movieRepository: MovieRepository

    init {
        SalesforceComponentHolder.component?.inject(this)
    }

    private var imageCheckPosition: ArrayList<Pair<Int?, Boolean?>>? = arrayListOf()

    class MoviesViewHolder(val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder =
        MoviesViewHolder(
            MovieItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).apply {
                hideFavoritesIconForFavoritesTab(this.favoritesIcon)
            }
        )

    private fun hideFavoritesIconForFavoritesTab(favoritesIcon: AppCompatImageView) =
        favoritesList?.let {
            favoritesIcon.visibility = View.GONE
        }


    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.binding.let { binding ->
            if (getAllFavoriteMovieIDs().contains(list?.get(position)?.imdbID ?: EMPTY)) {
                imageCheckPosition?.add(Pair(position, true))
                binding.favoritesIcon.setImageResource(R.drawable.ic_favorite_24)
            }
            binding.movieName.text =
                list?.get(position)?.Title ?: favoritesList?.get(position)?.Title
            binding.movieDirector.text =
                list?.get(position)?.Title ?: favoritesList?.get(position)?.Title
            binding.movieYear.text = list?.get(position)?.Year ?: favoritesList?.get(position)?.Year
            binding.movieSummary.text =
                list?.get(position)?.imdbID ?: favoritesList?.get(position)?.imdbID
            Picasso.with(context)
                .load(list?.get(position)?.Poster ?: favoritesList?.get(position)?.Poster)
                .into(binding.movieImage)
            binding.root.setOnClickListener {
                listener(
                    0,
                    binding.movieSummary.text.toString()
                )
            }
            binding.favoritesIcon.setOnClickListener {
                if (imageCheckPosition?.contains(Pair(position, true)) == true) {
                    listener(
                        -1,
                        binding.movieSummary.text.toString()
                    )
                    binding.favoritesIcon.setImageResource(R.drawable.ic_favorite_unchecked_24)
                } else {
                    imageCheckPosition?.add(Pair(position, true))
                    listener(
                        1,
                        binding.movieSummary.text.toString()
                    )
                    binding.favoritesIcon.setImageResource(R.drawable.ic_favorite_24)
                }

            }
        }
    }

    private fun getAllFavoriteMovieIDs(): List<String> = movieRepository.getAllFavoriteIDs()

    override fun getItemCount(): Int = (list?.size ?: favoritesList?.size ?: 0)
}