package com.reign.androidnews.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "news")
data class NewsItemDBModel (
    @PrimaryKey
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
    val createdAt: String,
    var shouldBeShown: Boolean = true
)