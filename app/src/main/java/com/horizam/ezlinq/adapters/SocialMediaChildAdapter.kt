package com.horizam.ezlinq.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.horizam.ezlinq.App
import com.horizam.ezlinq.Constants.Companion.BASE_URL
import com.horizam.ezlinq.Constants.Companion.HINT
import com.horizam.ezlinq.Constants.Companion.ICON
import com.horizam.ezlinq.Constants.Companion.PATH
import com.horizam.ezlinq.Constants.Companion.PLATFORM_ID
import com.horizam.ezlinq.Constants.Companion.TITLE
import com.horizam.ezlinq.R
import com.horizam.ezlinq.activities.ItemActivity
import com.horizam.ezlinq.networking.response.Platforms


class SocialMediaChildAdapter(
    var platforms: List<Platforms>,
    var context: Context
) : RecyclerView.Adapter<SocialMediaChildAdapter.Holder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): Holder {
        val inflater = LayoutInflater.from(viewGroup.context)

        val view: View = inflater.inflate(R.layout.list_item_links, viewGroup, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, i: Int) {
        var platform: Platforms = platforms[i]

        holder.tv_platform_name.text = platform.title

        /*Glide.with(context).load("${BASE_URL}${platform.icon}")
            .into(holder.iv_link)*/
        Glide.with(context)
            .load("${BASE_URL}${platform.icon}")
//            .override(800,400)
            .error(R.drawable.facebook)
            .into(holder.iv_link)

        holder.itemView.setOnClickListener {
//            if (platform.pro == 1) {
//                Toast.makeText(context, "Pro Feature Coming Soon", Toast.LENGTH_SHORT).show()
//            } else {
//                passData(platform.title, platform.icon, it.context, platform.path, platform.id)
//            }
            passData(
                platform.title,
                platform.icon,
                it.context,
                platform.id,
                platform.placeholderEn,
                platform.path
            )
        }

        changeBorderOfSavedPlatForm(platform.status, holder)

//        proPlatForm(holder.iv_platform, platform.pro)

    }

    private fun changeBorderOfSavedPlatForm(saved: Int, holder: Holder) {
        if (saved == 0) {
            holder.card_platform_holder.strokeWidth = 0
            holder.iv_saved_green_tick.visibility = View.GONE
        } else {
            holder.card_platform_holder.strokeWidth = 4
            holder.iv_saved_green_tick.visibility = View.VISIBLE

        }

    }


//    private fun proPlatForm(ivPlatform: ImageView, pro: Int) {
//        if (pro == 0) {
//            ivPlatform.visibility = View.GONE
//        } else {
//            ivPlatform.visibility = View.VISIBLE
//        }
//    }

    override fun getItemCount(): Int {
        return platforms.size
    }

    private fun passData(
        title: String,
        icon: String,
        context: Context,
        id: Int,
        placeholderEn: String?,
        path: String
    ) {
        val intent = Intent(App.getAppContext(), ItemActivity::class.java)
        intent.putExtra(TITLE, title)
        intent.putExtra(ICON, icon)
        intent.putExtra(PLATFORM_ID, id)
        intent.putExtra(HINT, placeholderEn)
        intent.putExtra(PATH , path)

        context.startActivity(intent)
    }

    fun renewlist(platFormsFromCategories: MutableList<Platforms>) {
        platforms = platFormsFromCategories
        notifyDataSetChanged()

    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv_platform: ImageView = itemView.findViewById(R.id.iv_platform)
        var iv_link: ImageView = itemView.findViewById(R.id.iv_link_et)
        var iv_saved_green_tick: ImageView = itemView.findViewById(R.id.iv_saved_green_tick)
        var tv_platform_name: TextView = itemView.findViewById(R.id.tv_platform_name)
        var card_platform_holder: MaterialCardView =
            itemView.findViewById(R.id.card_platform_holder)


    }

}