package com.singularitycoder.instanote.imageselection.model

data class ImageResponse(val hits: List<ImageResult>)

data class ImageResult(val previewURL: String)