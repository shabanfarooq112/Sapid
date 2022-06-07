package com.horizam.ezlinq.adapters

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.horizam.ezlinq.App
import com.horizam.ezlinq.Constants
import com.horizam.ezlinq.Constants.Companion.ADD_LINKS
import com.horizam.ezlinq.R
import com.horizam.ezlinq.activities.MainActivity
import com.horizam.ezlinq.activities.PrefManager
import com.horizam.ezlinq.fragments.BottomSheetPlatform
import com.horizam.ezlinq.networking.ApiListener
import com.horizam.ezlinq.networking.NetworkingModel
import com.horizam.ezlinq.networking.request.SwapPlatformRequest
import com.horizam.ezlinq.networking.response.GeneralResponseWithStatusAndMessage
import com.horizam.ezlinq.networking.response.UserPlatforms
import com.horizam.ezlinq.utils.ItemMoveCallback
import com.horizam.ezlinq.utils.SwapPlatform
import org.greenrobot.eventbus.EventBus
import java.util.*
import kotlin.collections.ArrayList


class ProfilePlatformsAdapter(

    var platforms: MutableList<UserPlatforms>,
    var context: Context,
    var forOtherUser: Boolean = false
) : RecyclerView.Adapter<ProfilePlatformsAdapter.Holder>(),
    ItemMoveCallback.ItemTouchHelperContract {

    private var prefManager: PrefManager = PrefManager(context)
    private lateinit var networkingModel: NetworkingModel
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): Holder {

        val inflater = LayoutInflater.from(viewGroup.context)
        val view: View = inflater.inflate(R.layout.list_item_links_platform, viewGroup, false)
        return Holder(view)

    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: Holder, position: Int) {

        val platform: UserPlatforms = platforms[position]

        holder.tv_platform_name.text = platform.title
        val imageUrl:String="${Constants.BASE_URL}${platform.icon}"
        Log.d("image@URL123", imageUrl)
        Glide.with(context)
            .load(imageUrl)
            .override(800,400)
            .error(R.drawable.facebook)
            .into(holder.iv_link)


        holder.itemView.setOnClickListener {
            Constants.PLATFORMS_PATH = platform.path
            Constants.PLATFORMS_TITLE = platform.title!!
            Constants.PLATFORMS_ICON = platform.icon!!
            Constants.PLATFORMS_ID = platform.id!!
            Constants.PLATFORMS_HINT = platform.placeholderEn
            Constants.PLATFORMS_DESCRIPTION=platform.descriptionEn



            Constants.BASE_LINK_FOR_PLATFORM_PATH = platform.baseURL

            if (forOtherUser) {
                openUrlInApp(platform.title!!)

            } else {
                val BottomSheetItem = BottomSheetPlatform()
                BottomSheetItem.show(
                    (context as MainActivity).supportFragmentManager,
                    "exampleBottomSheet"
                )
            }

        }


        if (prefManager.getDirectState() == 0 || position == 0) {
            holder.card_platform_holder.alpha = 1f
        } else {
            holder.card_platform_holder.alpha = 0.3f

        }


    }

    private fun openUrlInApp(title: String) {
        when (title) {
            Constants.PLATFORM_TEXT -> {
                openMessages()

            }
            Constants.PLATFORM_ADDRESS -> {
                openMap()
            }
            Constants.PLATFORM_KONTAKT -> {
                openContactlist(Constants.PLATFORMS_PATH!!)
            }
            Constants.PLATFORM_EMAIL -> {
                composeEmail(Constants.PLATFORMS_PATH!!)

            }

            Constants.PLATFORM_WEBSITE -> {

                try {
                    if (!URLUtil.isValidUrl(Constants.PLATFORMS_PATH!!.toLowerCase(Locale.ROOT))) {
                        Toast.makeText(
                            context,
                            context.resources.getString(R.string.this_is_not_a_valid_link),
                            Toast.LENGTH_LONG
                        )
                            .show()
                    } else {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(Constants.PLATFORMS_PATH!!.toLowerCase(Locale.ROOT))
                        context.startActivity(intent)
                    }
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(
                        context,
                        context.resources.getString(R.string.you_dont_have_browser),
                        Toast.LENGTH_LONG
                    )
                        .show()

                }


            }
            else -> {
                if (!Constants.BASE_LINK_FOR_PLATFORM_PATH.isNullOrEmpty()) {
                    val browserIntent =
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("${Constants.BASE_LINK_FOR_PLATFORM_PATH}${Constants.PLATFORMS_PATH}")
                        )
                    context.startActivity(browserIntent)
                }else{
                    var url= Constants.PLATFORMS_PATH
                    if (!url!!.startsWith("https://") && !url.startsWith("http://")) {
                        url = "http://${Constants.PLATFORMS_PATH}"
                    }
                    val openUrlIntent =
                        Intent(Intent.ACTION_VIEW, Uri.parse(url))
                  context.startActivity(openUrlIntent)
                }
            }
        }
    }


    override fun getItemCount(): Int {
        return platforms.size
    }


    fun renewlist(platFormsFromCategories: MutableList<UserPlatforms>) {

        platforms = getPlatformsDirect(platFormsFromCategories)
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

    override fun onRowMoved(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(platforms, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(platforms, i, i - 1)
            }
        }

        notifyItemMoved(fromPosition, toPosition)

        var swapPlatformList:ArrayList<SwapPlatform> = ArrayList()
        swapPlatformList.clear()

        Log.d("!@#onRowClear1adap", toPosition.toString())
        Log.d("!@#onRowClear1adap", fromPosition.toString())

        val list = platforms
        list.forEachIndexed { index, platforms ->
            platforms.platformOrder = index
        }
        list.forEach {
            val swapPlatform: SwapPlatform =
                SwapPlatform(it.id!!, it.platformOrder!!)
            swapPlatformList.add(swapPlatform)
        }
        excUserSwapPlatforms(swapPlatformList)

    }

    override fun onRowSelected(myViewHolder: ProfilePlatformsAdapter.Holder?) {

    }

    override fun onRowClear(myViewHolder: ProfilePlatformsAdapter.Holder?) {
        if (prefManager.getDirectState() == 1 && myViewHolder!!.adapterPosition == 0) {

            var selectedPlatform: UserPlatforms = platforms[myViewHolder.adapterPosition]
            if (selectedPlatform.title != ADD_LINKS) {
                EventBus.getDefault().post(platforms[myViewHolder.adapterPosition])
            }
        }


        notifyDataSetChanged()


    }

    private fun getPlatformsDirect(
        mPlatform: MutableList<UserPlatforms>
    ): MutableList<UserPlatforms> {

//        for (platform in 0 until mPlatform.size) {
//            if (mPlatform[platform].direct == 1) {
//                val a = mPlatform[0]
//                mPlatform[0] = mPlatform[platform]
//                mPlatform[platform] = a
//            }
//        }

        return mPlatform
    }

    private fun openMessages() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("smsto:" + Uri.encode(Constants.PLATFORMS_PATH))
        context.startActivity(intent)

    }


    fun composeEmail(
        addresses: String
    ) {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$addresses")
        }
        context.startActivity(Intent.createChooser(emailIntent, "Metrotap"))
    }

    private fun openContactlist(platformsPath: String) {
        val intent = Intent(Intent.ACTION_INSERT)
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE)
        val data: ArrayList<ContentValues> = ArrayList()

        val row = ContentValues()
        row.put(
            ContactsContract.Data.MIMETYPE,
            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
        )
        row.put(ContactsContract.CommonDataKinds.Phone.NUMBER, platformsPath)
        row.put(
            ContactsContract.CommonDataKinds.Phone.TYPE,
            ContactsContract.CommonDataKinds.Phone.TYPE_WORK
        )
        data.add(row)


        intent.putExtra(ContactsContract.Intents.Insert.NAME, "username")
        intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, data)
        context.startActivity(intent)


    }
    private fun excUserSwapPlatforms(swapPlatform:List<SwapPlatform>){
        networkingModel = NetworkingModel()
        val swapPlatformRequest = SwapPlatformRequest(swapPlatform)
        networkingModel.exeUserPLatformSwap(swapPlatformRequest,
        object :ApiListener<GeneralResponseWithStatusAndMessage>{
            override fun onSuccess(body: GeneralResponseWithStatusAndMessage?) {
                body.also {
                    if (it!!.status==200){
                        Toast.makeText(App.getAppContext(), it.message, Toast.LENGTH_SHORT)
                            .show()
                    }else{
                        Toast.makeText(App.getAppContext(), it.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            override fun onFailure(error: Throwable) {
                Toast.makeText(App.getAppContext(), error.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }

    private fun openMap() {

        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://maps.google.co.in/maps?q=${Constants.PLATFORMS_PATH}")
        )

        context.startActivity(intent)

    }

}