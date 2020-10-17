package com.reign.androidnews.data

import androidx.room.*
import com.reign.androidnews.models.NewsItemDBModel

@Dao
interface NewsDao {
    @Query("SELECT * FROM news WHERE shouldBeShown = 1 order by createdAt desc")
    fun getVisibleNews() : List<NewsItemDBModel>

    @Query("SELECT * FROM news WHERE shouldBeShown = 0 order by createdAt desc")
    fun getHiddenNews() : List<NewsItemDBModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNews(newsItems: List<NewsItemDBModel>)

    @Update
    fun updateNewsItem(newsItem: NewsItemDBModel)
}