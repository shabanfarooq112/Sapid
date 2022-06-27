package com.horizam.sapid.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.horizam.sapid.R
import com.horizam.sapid.networking.response.SearchData
import de.hdodenhof.circleimageview.CircleImageView


class FeatureListAdapter(
    var featureDataList: List<SearchData>,
    var context: Context
) : RecyclerView.Adapter<FeatureListAdapter.Holder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): Holder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val view: View = inflater.inflate(R.layout.search_list_item_featured, viewGroup, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, i: Int) {
//        val searchData: SearchData = featureDataList[i]
//        holder.itemView.setOnClickListener {
//            val intent = Intent(App.getAppContext(), MainActivity::class.java)
//            intent.putExtra(Constants.SEARCH_INTENT, searchData.username)
//            Constants.otherUserName = searchData.username
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            App.getAppContext()!!.startActivity(intent)
//        }
//        holder.tv_name.text = searchData.username
//        if(searchData.verified.equals("1")){
//            holder.iv_blue_ticks.visibility=View.VISIBLE
//        }else{
//            holder.iv_blue_ticks.visibility=View.GONE
//        }
//
//
//        Glide.with(context).load("${BASE_URL}${searchData.photo}")
//            .error(R.drawable.img_orignal_logo)
//            .placeholder(R.drawable.img_orignal_logo)
//            .into(holder.civ_user_pic)
    }

    override fun getItemCount(): Int {
        //return mList.size
        return featureDataList.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var civ_user_pic: CircleImageView = itemView.findViewById(R.id.civ_user_pic)
        var tv_name: TextView = itemView.findViewById(R.id.tv_name)
        var iv_blue_ticks: ImageView=itemView.findViewById(R.id.iv_blue_tick_search)
        var iv_feature: ImageView=itemView.findViewById(R.id.iv_featured)


    }

    fun renewlist(mSearchData: List<SearchData>) {
        featureDataList = mSearchData
        notifyDataSetChanged()
    }
}