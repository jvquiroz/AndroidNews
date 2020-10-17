package com.reign.androidnews.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.reign.androidnews.data.NewsDao
import com.reign.androidnews.models.NewsItemDBModel

@Database(entities = [NewsItemDBModel::class], version = 1)
abstract class NewsDatabase : RoomDatabase(){
    abstract fun newsDao(): NewsDao
}