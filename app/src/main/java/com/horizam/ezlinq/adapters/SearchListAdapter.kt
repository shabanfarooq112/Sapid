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
import com.horizam.ezlinq.App
import com.horizam.ezlinq.Constants
import com.horizam.ezlinq.Constants.Companion.BASE_URL
import com.horizam.ezlinq.R
import com.horizam.ezlinq.activities.MainActivity
import com.horizam.ezlinq.networking.response.ConnectedUser
import de.hdodenhof.circleimageview.CircleImageView


class SearchListAdapter(
    var connectedUserList: List<ConnectedUser>,
    var context: Context
) : RecyclerView.Adapter<SearchListAdapter.Holder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): Holder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val view: View = inflater.inflate(R.layout.search_list_item, viewGroup, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, i: Int) {
        val searchData: ConnectedUser = connectedUserList[i]
        holder.itemView.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(Constants.SEARCH_INTENT, searchData.username)
            Constants.otherUserName = searchData.username
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            App.getAppContext()!!.startActivity(intent)
        }
        holder.tv_name.text = searchData.username
//        holder.tv_date.text = searchData.updated_at
        if(searchData.verified.equals("1")){
            holder.iv_blue_ticks.visibility=View.VISIBLE
        }else{
            holder.iv_blue_ticks.visibility=View.GONE
        }

        holder.tv_views.text = "${context.resources.getString(R.string.views)} ${searchData.tiks}"

        Glide.with(context).load("${BASE_URL}${searchData.photo}")
            .error(R.drawable.ic_logo)
            .placeholder(R.drawable.ic_logo)
            .into(holder.civ_user_pic)
    }

    override fun getItemCount(): Int {
        //return mList.size
        return connectedUserList.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var civ_user_pic: CircleImageView = itemView.findViewById(R.id.civ_user_pic)
        var tv_name: TextView = itemView.findViewById(R.id.tv_name)
        var tv_date: TextView = itemView.findViewById(R.id.tv_date)
        var tv_views: TextView = itemView.findViewById(R.id.tv_views)
        var iv_blue_ticks: ImageView=itemView.findViewById(R.id.iv_blue_tick_search)
       // var iv_feature: ImageView=itemView.findViewById(R.id.iv_featured)


    }

    fun renewlist(connectedUser: List<ConnectedUser>) {
        connectedUserList = connectedUser
        notifyDataSetChanged()
    }
}