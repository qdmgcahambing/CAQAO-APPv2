package com.example.caqao.network

import com.squareup.moshi.Json

data class CacaoDetection(
     val id: Int,
    @Json(name = "img_src_url") val imgSrcUrl: String?,
    val veryDarkBrown: Int,
    val brown: Int,
    val partlyPurple: Int,
    val totalPurple: Int,
    val g1: Int,
    val g2: Int,
    val g3: Int,
    val g4: Int,
    val mouldy: Int,
    val insectInfested: Int,
    val slaty: Int,
    val germinated: Int,
    val beanGrade: String,
    val date: String?,
    )