package com.horizam.ezlinq.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.horizam.ezlinq.Constants
import com.horizam.ezlinq.R
import com.horizam.ezlinq.networking.response.Platforms


class ProfileOneItemAdapter(
    var platforms: List<Platforms>
) : RecyclerView.Adapter<ProfileOneItemAdapter.Holder>() {

    init {
        platforms = ArrayList()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): Holder {
        var view: View? = null
        val inflater = LayoutInflater.from(viewGroup.context)

        if (i == 1) {
            view = inflater.inflate(R.layout.profile_one_last_item, viewGroup, false)
        } else {
            view = inflater.inflate(R.layout.list_item_links, viewGroup, false)
        }
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, i: Int) {
        var platform: Platforms = platforms[i]
        holder.tv_platform_name.text = platform.title
        Glide.with(holder.itemView.context).load("${Constants.BASE_URL}${platform.icon}")
            .into(holder.iv_link)
        holder.itemView.setOnClickListener {
        }


    }

    override fun getItemCount(): Int {
        return platforms.size
    }

    override fun getItemViewType(position: Int): Int {
        if (position == platforms.size - 1) {
            return 1
        } else {
            return 0
        }
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv_platform: ImageView = itemView.findViewById(R.id.iv_platform)
        var iv_link: ImageView = itemView.findViewById(R.id.iv_link)
        var tv_platform_name: TextView = itemView.findViewById(R.id.tv_platform_name)

    }
}