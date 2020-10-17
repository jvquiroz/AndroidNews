package com.reign.androidnews.adapters

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.reign.androidnews.models.NewsItemAPIModel
import com.reign.androidnews.R
import java.util.*
import kotlin.collections.ArrayList

/**
 * Handles News presentation
 * @param listener click listener for items
 */
class NewsAdapter (private val listener: (NewsItemAPIModel) -> Unit) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private lateinit var news: ArrayList<NewsItemAPIModel>

    fun setNews(news: List<NewsItemAPIModel>) {
        this.news = ArrayList(news)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val contactView = inflater.inflate(R.layout.news_item, parent, false)
        return ViewHolder(contactView)
    }

    override fun getItemCount(): Int {
        return if (::news.isInitialized) news.size else 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = news[position]
        val remainingTime = DateUtils.getRelativeTimeSpanString(item.createdAt.toLong(), Date().time / 1000, DateUtils.SECOND_IN_MILLIS)
        holder.titleTV.text = item.title ?: item.storyTitle
        holder.footTV.text = "${item.author} - $remainingTime"
        holder.itemView.setOnClickListener { listener(item) }
    }

    fun getItemAt(adapterPosition: Int): NewsItemAPIModel? {
        return news.let {
            if (adapterPosition < it.size) it[adapterPosition] else null
        }
    }

    fun removeAt(adapterPosition: Int) {
        news.let {
            it.removeAt(adapterPosition)
            notifyItemRemoved(adapterPosition)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTV: TextView = itemView.findViewById(R.id.tvTitle)
        val footTV: TextView = itemView.findViewById(R.id.tvFoot)
    }

}