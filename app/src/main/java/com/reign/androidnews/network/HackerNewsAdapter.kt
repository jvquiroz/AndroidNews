package com.reign.androidnews.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object HackerNewsAdapter {
    val apiClient: HackerNewsAPI = Retrofit.Builder()
        .baseUrl("https://hn.algolia.com/api/v1/")
        .client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(HackerNewsAPI::class.java)
}