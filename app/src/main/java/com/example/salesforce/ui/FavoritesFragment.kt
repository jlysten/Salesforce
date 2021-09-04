package com.example.salesforce.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.salesforce.adapters.MovieAdapter
import com.example.salesforce.base.SalesForceBaseFragment
import com.example.salesforce.databinding.FragmentFavoritesBinding
import com.example.salesforce.db.entity.Favorites
import com.example.salesforce.viewmodel.SalesForceViewModel

class FavoritesFragment : SalesForceBaseFragment() {

    private lateinit var favoritesBinding: FragmentFavoritesBinding
    private var viewModel: SalesForceViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        favoritesBinding = FragmentFavoritesBinding.inflate(layoutInflater)
        return favoritesBinding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = activity?.run {
            ViewModelProvider(this)[SalesForceViewModel::class.java]
        }
        viewModel?.apply {
            fetchFavorites()
            favorites?.observe(viewLifecycleOwner, {
                it?.let {
                    attachAdapterAndDisplayItems(it)
                }
            })
        }
    }

    private fun attachAdapterAndDisplayItems(favorites: List<Favorites>) {
        favoritesBinding.movieListView.apply {
            adapter = MovieAdapter(null, favorites, context) { _, _ ->
                //do nothing
            }
            this.layoutManager = LinearLayoutManager(activity)
        }
    }

    companion object {
        fun newInstance(): FavoritesFragment = FavoritesFragment()
    }
}