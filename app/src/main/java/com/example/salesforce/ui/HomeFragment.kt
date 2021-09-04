package com.example.salesforce.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.salesforce.adapters.MovieAdapter
import com.example.salesforce.base.SalesForceBaseFragment
import com.example.salesforce.databinding.FragmentHomeBinding
import com.example.salesforce.db.entity.Search
import com.example.salesforce.util.SalesforceConstants.DETAILS_FRAGMENT
import com.example.salesforce.viewmodel.SalesForceViewModel


class HomeFragment : SalesForceBaseFragment() {

    private lateinit var homeFragmentBinding: FragmentHomeBinding
    private var viewModel: SalesForceViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeFragmentBinding = FragmentHomeBinding.inflate(layoutInflater)
        return homeFragmentBinding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = activity?.run {
            ViewModelProvider(this)[SalesForceViewModel::class.java]
        }
        homeFragmentBinding.searchBar.apply {
            requestFocus()
            setText(viewModel?.currentSearch)
            setOnTouchListener { _, event ->
                when {
                    drawableClicked(event) -> {
                        activity?.let { hideKeyboard(it) }
                        if (this.text.toString().isBlank()) {
                            Toast.makeText(activity, "Invalid Entry", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel?.searchForTitle(this.text.toString().trim())
                        }
                        true
                    }
                    else -> false
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel?.searchData?.observe(viewLifecycleOwner, { recentSearch ->
            recentSearch?.run {
                attachAdapterAndDisplayItems(this.Search)
            }
        })
        viewModel?.selectedMovieData?.observe(viewLifecycleOwner, {
            it?.run {
                displayMovieDetails()
            }
        })
    }

    private fun attachAdapterAndDisplayItems(search: List<Search>?) {
        homeFragmentBinding.movieListView.apply {
            adapter = MovieAdapter(search, null, context) { identifier, id ->
                when (identifier) {
                    1 -> fetchMovieDetails(id, true)
                    -1 -> removeMovieFromFavorites(id)
                    else -> fetchMovieDetails(id, false)
                }
            }
        }
    }

    private fun displayMovieDetails() {
        activity?.supportFragmentManager?.let {
            MovieDetailsUI().show(it, DETAILS_FRAGMENT)
        }
    }

    private fun removeMovieFromFavorites(id: String) = viewModel?.removeMovieFromFavorites(id)

    private fun fetchMovieDetails(id: String, b: Boolean) = viewModel?.fetchMovieDetails(id, b)


    private fun hideKeyboard(activity: Activity) {
        val view = activity.currentFocus ?: View(activity)
        (activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel?.clearLiveDataObjects()
    }

    private fun drawableClicked(event: MotionEvent): Boolean =
        event.action == 1 && event.rawX >= homeFragmentBinding.searchBar.right -
                homeFragmentBinding.searchBar.compoundDrawables[2].bounds.width()


    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }
}