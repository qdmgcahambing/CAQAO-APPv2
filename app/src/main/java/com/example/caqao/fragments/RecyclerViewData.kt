package com.example.caqao.fragments

data class RecyclerViewData(val question : String,
                            val answer: String,
                            var isExpanded : Boolean = false)