package com.task.noteapp.imageselection.view

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.task.noteapp.R
import com.task.noteapp.databinding.FragmentImageSelectionBinding
import com.task.noteapp.helpers.Status
import com.task.noteapp.MainActivity
import com.task.noteapp.helpers.Resource
import com.task.noteapp.helpers.gone
import com.task.noteapp.helpers.visible
import com.task.noteapp.imageselection.model.ImageResponse
import com.task.noteapp.imageselection.model.ImageResult
import com.task.noteapp.imageselection.viewmodel.ImageSelectionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ImageSelectionFragment : Fragment(R.layout.fragment_image_selection) {

    private lateinit var nnContext: Context
    private lateinit var nnActivity: MainActivity
    private lateinit var binding: FragmentImageSelectionBinding

    private val viewModel: ImageSelectionViewModel by activityViewModels()

    @Inject
    lateinit var imageSelectionAdapter: ImageSelectionAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        nnContext = context
        nnActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentImageSelectionBinding.bind(view)
        setUpRecyclerView()
        subscribeToObservers()
        setUpUserActionListeners()
        viewModel.searchForImage("Notes")
    }

    private fun setUpRecyclerView() {
        binding.rvImages.apply {
            adapter = imageSelectionAdapter
            layoutManager = GridLayoutManager(nnContext, 3, GridLayoutManager.VERTICAL, false)
        }
    }

    private fun subscribeToObservers() {
        viewModel.imageList.observe(viewLifecycleOwner) { it: Resource<ImageResponse>? ->
            it ?: return@observe
            when (it.status) {
                Status.SUCCESS -> {
                    val urls = it.data?.hits?.map { imageResult: ImageResult -> imageResult.previewURL }
                    imageSelectionAdapter.imageUrlList = urls ?: emptyList()
                    binding.progressBar.gone()
                    if (urls.isNullOrEmpty()) binding.lottieEmpty.visible() else binding.lottieEmpty.gone()
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message ?: "Error", Toast.LENGTH_LONG).show()
                    binding.progressBar.gone()
                }
                Status.LOADING -> binding.progressBar.visible()
            }
        }
    }

    private fun setUpUserActionListeners() {
        var job: Job? = null
        binding.etSearchImage.editText?.addTextChangedListener { it: Editable? ->
            it ?: return@addTextChangedListener
            job?.cancel()
            job = lifecycleScope.launch {
                delay(1000)
                if (it.toString().isNotEmpty()) {
                    viewModel.searchForImage(it.toString())
                }
            }
        }

        imageSelectionAdapter.setOnImageClickListener { imageUrl: String ->
            nnActivity.supportFragmentManager.popBackStack()
            viewModel.setSelectedImage(imageUrl)
        }
    }
}