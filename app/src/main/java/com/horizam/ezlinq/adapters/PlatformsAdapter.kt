package com.horizam.ezlinq.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.horizam.ezlinq.App
import com.horizam.ezlinq.R
import com.horizam.ezlinq.networking.response.Categories
import com.horizam.ezlinq.networking.response.Platforms


class PlatformsAdapter(
    var categories: List<Categories>,
    var context: Context
) : RecyclerView.Adapter<PlatformsAdapter.Holder>() {

    lateinit var platforms: List<Platforms>

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): Holder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val view: View = inflater.inflate(R.layout.platform_item_recyclerview, viewGroup, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, i: Int) {
        var category: Categories = categories[i]

        holder.textView.text = category.name
        platforms = category.platforms
        holder.recyclerView.setHasFixedSize(true)
        holder.adapter = SocialMediaChildAdapter(platforms, context)
        holder.recyclerView.adapter = holder.adapter
        holder.adapter.notifyDataSetChanged()


    }

    override fun getItemCount(): Int {
        //return mList.size
        return categories.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView
        var recyclerView: RecyclerView
        var adapter: SocialMediaChildAdapter

        init {
            textView = itemView.findViewById(R.id.tv_add_social_media)
            recyclerView = itemView.findViewById(R.id.recyclerview_social_media)
            platforms = ArrayList()
            recyclerView.layoutManager =
                GridLayoutManager(App.getAppContext(), 3, RecyclerView.VERTICAL, false)
            adapter = SocialMediaChildAdapter(platforms, context)
            recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()

        }
    }

    fun renewlist(mCategories: List<Categories>) {
        categories = mCategories
        notifyDataSetChanged()
    }

}