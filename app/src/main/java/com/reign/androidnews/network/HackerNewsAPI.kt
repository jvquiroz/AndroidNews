package com.reign.androidnews.network

import com.reign.androidnews.models.NewsResultModel
import retrofit2.Response
import retrofit2.http.GET

interface HackerNewsAPI {
    @GET("search_by_date?query=android")
    suspend fun getNews(): Response<NewsResultModel>
}