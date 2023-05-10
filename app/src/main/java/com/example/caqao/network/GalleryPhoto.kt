package com.example.caqao.network

import com.squareup.moshi.Json

data class GalleryPhoto (
    val id: String,
    @Json(name = "img_src_url") val imgSrcUrl: String
)
