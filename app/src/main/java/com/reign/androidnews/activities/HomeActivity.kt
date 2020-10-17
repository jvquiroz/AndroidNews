package com.reign.androidnews.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.reign.androidnews.*
import com.reign.androidnews.adapters.NewsAdapter
import com.reign.androidnews.data.NewsDatabase
import com.reign.androidnews.models.NewsItemAPIModel
import com.reign.androidnews.models.NewsItemDBModel
import com.reign.androidnews.network.HackerNewsAdapter
import com.reign.androidnews.utils.SwipeHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class HomeActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private lateinit var db: NewsDatabase
    private lateinit var viewAdapter: NewsAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewAdapter = NewsAdapter { item ->
            val newsDetailsIntent = Intent(
                this,
                NewsDetailsActivity::class.java
            )
            val url: String? = item.url ?: item.storyURL
            url?.let {
                newsDetailsIntent.putExtra(
                    NewsDetailsActivity.NEWS_STORY_URL,
                    it
                )
                this@HomeActivity.startActivity(newsDetailsIntent)
            }
        }

        //RecyclerView set up
        findViewById<RecyclerView>(R.id.rvNews).apply {
            setHasFixedSize(true)
            layoutManager =  LinearLayoutManager(this@HomeActivity)
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
            adapter = viewAdapter

            val swipeHelper = object: SwipeHelper() {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val newsItem = this@HomeActivity.viewAdapter.getItemAt(viewHolder.adapterPosition)
                    newsItem?.let {
                        val item = NewsItemDBModel(
                            it.id,
                            it.title,
                            it.author,
                            it.url,
                            it.storyURL,
                            it.storyURL,
                            it.createdAt,
                            false
                        )
                        updateNewsItem(item)
                    }
                    this@HomeActivity.viewAdapter.removeAt(viewHolder.adapterPosition)
                }
            }

            ItemTouchHelper(swipeHelper).attachToRecyclerView(this)
        }

        swipeRefreshLayout = findViewById(R.id.srlNews)
        swipeRefreshLayout.setOnRefreshListener { fetchData() }

        db = Room.databaseBuilder(
            applicationContext,
            NewsDatabase::class.java, "news-database"
        ).build()

        fetchData()
    }

    private fun updateNewsItem(item: NewsItemDBModel) {
        launch(Dispatchers.IO) {
            db.newsDao().updateNewsItem(item)
        }
    }

    /**
     * Fetches data fist from the API, then from the local database.
     */
    private fun fetchData() {
        swipeRefreshLayout.isRefreshing = true
        launch(Dispatchers.IO) {
            var result = ArrayList(getNewsFromAPI())
            if (result == null) {
                result = ArrayList(getNewsFromDB())
            } else {
                val newsFromDB = db.newsDao().getHiddenNews()
                val newsToHide = ArrayList<NewsItemAPIModel>()
                for (newsItemAPI in result) {
                    for (newsItemBD in newsFromDB) {
                        if (newsItemAPI.id == newsItemBD.id) {
                            newsToHide.add(newsItemAPI)
                            break
                        }
                    }
                }
                result.removeAll(newsToHide)

                val newsFromAPI: List<NewsItemDBModel> = result.map {
                    NewsItemDBModel(
                        it.id,
                        it.title,
                        it.author,
                        it.url,
                        it.storyURL,
                        it.storyURL,
                        it.createdAt
                    )
                }
                db.newsDao().insertNews(newsFromAPI)
            }
            result?.let {
                launch(Dispatchers.Main) {
                    viewAdapter.setNews(it)
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        }
    }

    /**
     * Request data from Hacker news API and filter those with invalid identifier.
     */
    private suspend fun getNewsFromAPI() : List<NewsItemAPIModel>? {
        return try {
            val response = HackerNewsAdapter.apiClient.getNews()
            if (response.isSuccessful && response.body() != null) {
                response.body()!!.hits.filter { it.id > 0 }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Request visible news from Data base and transform those items from NewsItemDBModel to NewsItemAPIModel
     */
    private fun getNewsFromDB() : List<NewsItemAPIModel>? {
        return try {
            val result = db.newsDao().getVisibleNews()
            return result.map {
                NewsItemAPIModel(
                    it.id,
                    it.title,
                    it.author,
                    it.url,
                    it.storyURL,
                    it.storyURL,
                    it.createdAt
                )
            }
        } catch (e: Exception) {
            null
        }
    }
}
