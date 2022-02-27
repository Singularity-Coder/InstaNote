package com.task.noteapp.imageselection.viewmodel

import com.task.noteapp.helpers.Resource
import com.task.noteapp.helpers.api.ImageSearchService
import com.task.noteapp.imageselection.model.ImageResponse
import javax.inject.Inject

class ImageSelectionRepository @Inject constructor(private val imageSearchService: ImageSearchService) {

    suspend fun searchImage(imageString: String): Resource<ImageResponse> {
        return try {
            val response = imageSearchService.imageSearch(imageString)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Error", null)
            } else {
                Resource.error("Error", null)
            }
        } catch (e: Exception) {
            Resource.error("No data!", null)
        }
    }
}