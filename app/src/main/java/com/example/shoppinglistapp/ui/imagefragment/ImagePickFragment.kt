package com.example.shoppinglistapp.ui.imagefragment

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.shoppinglistapp.R
import com.example.shoppinglistapp.databinding.FragmentImagePickBinding
import com.example.shoppinglistapp.utils.Constants.BUNDLE_KEY
import com.example.shoppinglistapp.utils.Constants.GRID_SPAN_COUNT
import com.example.shoppinglistapp.utils.Constants.REQUEST_KEY
import com.example.shoppinglistapp.utils.extensions.alert
import com.example.shoppinglistapp.utils.extensions.negativeButton
import com.example.shoppinglistapp.utils.extensions.positiveButton
import com.example.shoppinglistapp.utils.extensions.snackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class ImagePickFragment : Fragment(R.layout.fragment_image_pick) {

    private var _binding: FragmentImagePickBinding? = null
    private val binding get() = _binding!!

    private lateinit var imageAdapter: ImageAdapter

    private var readPermissionGranted = false
    private var writePermissionGranted = false
    private lateinit var permissionsLauncher: ActivityResultLauncher<Array<String>>

    private val viewModel by viewModels<ImageViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentImagePickBinding.bind(view)
        imageAdapter = ImageAdapter()

//        var job: Job? = null
//        binding.etSearch.doOnTextChanged { editable, _, _, _ ->
//            job?.cancel()
//            job = lifecycleScope.launch {
//                delay(SEARCH_TIME_DELAY)
//                editable?.let {
//                    if (editable.toString().isNotEmpty()) {
//                        viewModel.searchForImage(editable.toString())
//                    }
//                }
//            }
//        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (query.toString().isNotEmpty()) {
                        viewModel.searchForImage(query.toString())
                    }
                }
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                query?.let {
                    if (query.toString().isNotEmpty()) {
                        viewModel.searchForImage(query.toString())
                    }
                }
                return false
            }
        })

        imageAdapter.setOnItemClickListener { url ->

            alert {
                setTitle("What to do")
                setMessage("Set Image or Download Image...")

                positiveButton("Set") {
                    setFragmentResult(REQUEST_KEY, bundleOf(BUNDLE_KEY to url))
                    findNavController().popBackStack()
                }

                negativeButton("Download") {
                    if (writePermissionGranted) {
                        lifecycleScope.launch {
                            val bitmap: Bitmap = getBitmap(url)
                            viewModel.savePhotoToExternalStorage(
                                UUID.randomUUID().toString(),
                                bitmap
                            )
                            snackBar("Image Downloaded Successfully")
                        }
                    }
                }
            }
        }

        permissionsLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                readPermissionGranted =
                    permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: readPermissionGranted
                writePermissionGranted = permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE]
                    ?: writePermissionGranted
            }

        updateOrRequestPermissions()
        setupRecyclerView()
        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.images.collect { state ->
                when (state) {
                    is ImageViewModel.State.Success -> {
                        val urls = state.image?.hits?.map { it.previewURL }
                        imageAdapter.images = urls ?: listOf()
                        binding.progressBar.visibility = View.GONE
                    }

                    is ImageViewModel.State.Error -> {
                        snackBar(state.msg ?: "An unknown error occurred.")
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


    private fun updateOrRequestPermissions() {
        val hasReadPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val hasWritePermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val minSdk29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

        readPermissionGranted = hasReadPermission
        writePermissionGranted = hasWritePermission || minSdk29

        val permissionsToRequest = mutableListOf<String>()
        if (!writePermissionGranted) {
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!readPermissionGranted) {
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (permissionsToRequest.isNotEmpty()) {
            permissionsLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }

    private suspend fun getBitmap(url: String): Bitmap {
        val loader = ImageLoader(requireContext())
        val req = ImageRequest.Builder(requireContext())
            .data(url)
            .build()

        val result = (loader.execute(req) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}