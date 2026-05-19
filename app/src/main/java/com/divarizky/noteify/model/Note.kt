package com.divarizky.noteify.model

import com.google.gson.annotations.SerializedName

data class Note(
    @SerializedName("userId")
    val userId: Int,

    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("completed")
    val completed: Boolean
)
