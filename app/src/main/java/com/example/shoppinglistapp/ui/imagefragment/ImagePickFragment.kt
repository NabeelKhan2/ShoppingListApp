package com.example.shoppinglistapp.ui.imagefragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shoppinglistapp.R
import com.example.shoppinglistapp.databinding.FragmentImagePickBinding
import com.example.shoppinglistapp.utils.Constants.BUNDLE_KEY
import com.example.shoppinglistapp.utils.Constants.GRID_SPAN_COUNT
import com.example.shoppinglistapp.utils.Constants.REQUEST_KEY
import com.example.shoppinglistapp.utils.Constants.SEARCH_TIME_DELAY
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ImagePickFragment : Fragment(R.layout.fragment_image_pick) {

    private var _binding: FragmentImagePickBinding? = null
    private val binding get() = _binding!!

    private lateinit var imageAdapter: ImageAdapter

    private val viewModel by viewModels<ImageViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentImagePickBinding.bind(view)

        imageAdapter = ImageAdapter()

        var job: Job? = null
        binding.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = lifecycleScope.launch {
                delay(SEARCH_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchForImage(editable.toString())
                    }
                }
            }
        }

        imageAdapter.setOnItemClickListener {
            setFragmentResult(REQUEST_KEY, bundleOf(BUNDLE_KEY to it))
            findNavController().popBackStack()
        }

        setupRecyclerView()
        subscribeToObservers()

    }

    private fun subscribeToObservers() {

        lifecycleScope.launchWhenStarted {

            viewModel.images.collect {
                when (it) {
                    is ImageViewModel.State.Success -> {
                        val urls = it.image?.hits?.map { imageResult -> imageResult.previewURL }
                        imageAdapter.images = urls ?: listOf()
                        binding.progressBar.visibility = View.GONE
                    }

                    is ImageViewModel.State.Error -> {

                        Snackbar.make(
                            binding.root,
                            it.msg ?: "An unknown error occurred.",
                            Snackbar.LENGTH_LONG
                        ).show()
                        binding.progressBar.visibility = View.GONE

                    }
                    is ImageViewModel.State.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    else -> {
                    }
                }
            }
        }


    }

    private fun setupRecyclerView() {
        binding.rvImages.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(requireContext(), GRID_SPAN_COUNT)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}