package com.example.salesforce.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.salesforce.databinding.MovieDetailsDialogBinding
import com.example.salesforce.util.SalesforceConstants.UNKNOWN
import com.example.salesforce.viewmodel.SalesForceViewModel
import com.squareup.picasso.Picasso

class MovieDetailsUI : DialogFragment() {

    private lateinit var binding: MovieDetailsDialogBinding
    private var viewModel: SalesForceViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        binding = MovieDetailsDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel = activity?.run {
            ViewModelProvider(this)[SalesForceViewModel::class.java]
        }
        binding.apply {
            movieDetailsTitle.text = viewModel?.selectedMovieData?.value?.Title ?: UNKNOWN
            movieDetailsDirector.text = viewModel?.selectedMovieData?.value?.Director ?: UNKNOWN
            movieDetailsGenre.text = viewModel?.selectedMovieData?.value?.Genre ?: UNKNOWN
            movieDetailsPlot.text = viewModel?.selectedMovieData?.value?.Plot ?: UNKNOWN
            movieDetailsRuntime.text = viewModel?.selectedMovieData?.value?.Runtime ?: UNKNOWN
            Picasso.with(activity)
                .load(viewModel?.selectedMovieData?.value?.Poster)
                .into(movieDetailsPoster)
        }
    }
}