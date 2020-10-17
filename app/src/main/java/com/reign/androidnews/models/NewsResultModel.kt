package com.reign.androidnews.models

import com.reign.androidnews.models.NewsItemAPIModel

data class NewsResultModel (
    val hits: List<NewsItemAPIModel>
)