package com.sid.assignment.model

import com.google.gson.annotations.SerializedName

data class CatFactsListResponse(
    @SerializedName("text")
    var text: String,

    @SerializedName("updatedAt")
    var updatedAt: String
)

