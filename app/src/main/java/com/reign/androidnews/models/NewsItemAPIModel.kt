package com.reign.androidnews.models

import com.google.gson.annotations.SerializedName

data class NewsItemAPIModel (
    @SerializedName("story_id")
    val id: Int,
    val title: String? = null,
    val author: String? = null,
    val url: String? = null,
    @SerializedName("story_title")
    val storyTitle: String? = null,
    @SerializedName("story_url")
    val storyURL: String? = null,
    @SerializedName("created_at_i")
    val createdAt: String
)
