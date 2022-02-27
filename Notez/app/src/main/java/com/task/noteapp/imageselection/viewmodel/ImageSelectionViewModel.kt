package com.task.noteapp.imageselection.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.noteapp.imageselection.model.ImageResponse
import com.task.noteapp.helpers.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageSelectionViewModel @Inject constructor(private val repository: ImageSelectionRepository) : ViewModel() {

    private val images = MutableLiveData<Resource<ImageResponse>>()
    val imageList: LiveData<Resource<ImageResponse>>
        get() = images

    private val selectedImage = MutableLiveData<String?>()
    val selectedImageUrl: LiveData<String?>
        get() = selectedImage

    fun setSelectedImage(url: String?) = selectedImage.postValue(url)

    fun searchForImage(searchString: String) {
        if (searchString.isBlank()) return
        images.value = Resource.loading(null)
        viewModelScope.launch {
            val response = repository.searchImage(searchString)
            images.value = response
        }
    }
}