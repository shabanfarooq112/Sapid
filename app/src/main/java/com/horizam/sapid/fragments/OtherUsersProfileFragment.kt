package com.horizam.sapid.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.horizam.sapid.App
import com.horizam.sapid.Constants
import com.horizam.sapid.R
import com.horizam.sapid.R.color.black_200
import com.horizam.sapid.R.color.white
import com.horizam.sapid.activities.PrefManager
import com.horizam.sapid.adapters.ProfilePlatformsAdapter
import com.horizam.sapid.activities.QrscanActivity
import com.horizam.sapid.callbacks.DrawerHandler
import com.horizam.sapid.databinding.FragmentTiklProfileBinding


import com.horizam.sapid.networking.ApiListener
import com.horizam.sapid.networking.NetworkingModel
import com.horizam.sapid.networking.response.*
import java.util.*
import kotlin.collections.ArrayList


class OtherUsersProfileFragment : Fragment() {

    private lateinit var binding: FragmentTiklProfileBinding
    private lateinit var prefManager: PrefManager
    private lateinit var networkingModel: NetworkingModel
    private lateinit var adapter: ProfilePlatformsAdapter
    private lateinit var drawerHandlerCallback: DrawerHandler
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var platforms: MutableList<UserPlatforms>
    var search = 0
    var connect_user_id = 0
    var connected: Int = 0;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTiklProfileBinding.inflate(inflater, container, false)
        inIt()


        excUserProfile()
        clickListeners()
//        setRecyclerview(it.profile.userPlatforms)
        return binding.root
    }

    private fun inIt() {
        prefManager = PrefManager(this.requireContext())
        swipeRefreshLayout = binding.layoutRefresh
        swipeRefreshLayout.setOnRefreshListener {
            excUserProfile()
        }
    }

    private fun clickListeners() {
        binding.tvConnectBtn.setOnClickListener {
            exeConnectuser()
        }
        binding.ivQrcodetwo.setOnClickListener(View.OnClickListener {
            if (search == 0) {
                val intent = Intent(activity, QrscanActivity::class.java)
                intent.putExtra(Constants.OTHER_PROFILE_QR, 1)
                startActivity(intent)
            } else {
                val intent = Intent(activity, QrscanActivity::class.java)
                intent.putExtra(Constants.OTHER_PROFILE_QR, 2)
                startActivity(intent)
            }
        })
        binding.navHomeSecond.setOnClickListener {
            drawerHandlerCallback.openDrawer()
        }
    }

    private fun exeConnectuser() {
        networkingModel = NetworkingModel()
        networkingModel.exeConnectUserApi(connect_user_id,
            object : ApiListener<GeneralResponseWithStatusAndMessage> {
                override fun onSuccess(body: GeneralResponseWithStatusAndMessage?) {

                    excUserProfile()
//
                    connected =
                        if (body!!.message.toLowerCase(Locale.ROOT) == "connection removed successfully") {
                            0
                        } else {
                            1
                        }
                    changeConnectedBtnState()

                }

                override fun onFailure(error: Throwable) {
                    Toast.makeText(context, "Some Error Occur", Toast.LENGTH_SHORT).show()
                    Log.e("error", error.message.toString());

                }

            })
    }
    private fun showMainLoader() {
        binding.progressBarMain.visibility = View.VISIBLE
    }

    private fun hideMainLoader() {
        binding.progressBarMain.visibility = View.GONE
    }

    private fun excUserProfile() {
        showMainLoader()
        if (Constants.otherUserName.equals("") && prefManager.getUserTiklData()!!
                .isNotEmpty()
        ) {

            val strs = prefManager.getUserTiklData()!!.split("/").toTypedArray()
            val userApikey = strs[strs.size - 1]
            val strs1 = userApikey.split("?").toTypedArray()
            val userApikey1 = strs1[0]
            networkingModel = NetworkingModel()
            networkingModel.exeOtherUserprofileApi(userApikey1, object :
                ApiListener<UserResponse> {
                override fun onSuccess(body: UserResponse?) {
                    swipeRefreshLayout.isRefreshing = false
                    hideMainLoader()
                    body.also {
                        if (it!!.status == 200) {
                            connect_user_id = it.profile!!.id!!
                            connected = it.profile!!.connected!!
                            setRecyclerview(it.profile!!.userPlatforms)
                            setProifiledata(
                                it.profile!!.firstName,
                                it.profile!!.username,
                                it.profile!!.bio,
                                it.profile!!.private!!,
//                                null,
                                it.profile!!.photo,
                                it.profile!!.tiks!!,
                                it.profile!!.tiks!!
                            )

                        } else {
                            Toast.makeText(App.getAppContext(), it.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }

                override fun onFailure(error: Throwable) {
                    Toast.makeText(App.getAppContext(), "$error", Toast.LENGTH_SHORT).show()
                }
            }, "gotap")
        } else {
//            if(requireArguments().containsKey("url")){
//                val url = requireArguments().get("url").toString()
//                val strs = url.split("/").toTypedArray()
//                Constants.otherUserName = strs.get(3)
//            }
            val userApikey = Constants.otherUserName
            Constants.otherUserName = ""
            networkingModel = NetworkingModel()
            networkingModel.exeOtherUserprofileApi(userApikey, object :
                ApiListener<UserResponse> {
                override fun onSuccess(body: UserResponse?) {
                    hideMainLoader()
                    swipeRefreshLayout.isRefreshing = false
                    body.also {
                        if (it!!.status == 200) {
                            connect_user_id = it.profile!!.id!!
                            connected = it.profile!!.connected!!
                            changeConnectedBtnState()
                            setRecyclerview(it.profile!!.userPlatforms)
                            setsearchProifiledata(
                                it.profile!!.firstName,
                                it.profile!!.username,
                                it.profile!!.bio,
                                it.profile!!.private!!,
//                                it.data.categories,
                                it.profile!!.photo!!,
                                it.profile!!.tiks!!
//                                it.profile.verified
                            )
                        } else {
                            Toast.makeText(App.getAppContext(), it.message, Toast.LENGTH_SHORT)
                                .show()
                            Log.d("@specific123", it.message!!)
                        }
                    }
                }

                override fun onFailure(error: Throwable) {
                    Toast.makeText(App.getAppContext(), "$error", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }


    private fun changeConnectedBtnState() {
        if (connected == 1) {
            binding.tvConnectBtn.text = resources.getString(R.string.str_disconnect)
            binding.tvConnectBtn.backgroundTintList =
                ContextCompat.getColorStateList(App.getAppContext()!!, white)
            binding.tvConnectBtn.setTextColor(resources.getColor(R.color.black_200));

        } else {
            binding.tvConnectBtn.text = resources.getString(R.string.str_connect)
            binding.tvConnectBtn.backgroundTintList =
                ContextCompat.getColorStateList(App.getAppContext()!!, black_200)
            binding.tvConnectBtn.setTextColor(resources.getColor(R.color.white));
        }
    }

    private fun setProifiledata(
        name: String?,
        username: String?,
        bio: String?,
        private: Int,
//        categories: List<Categories>,
        photo: String?,
        tiks: Int,
        verified: Int
    ) {
        binding.tvNameOther.text=name
        binding.tvUsernameOther.text = username
        binding.tvBio.text = bio
        Glide.with(this)
            .load("${Constants.BASE_URL}$photo")
            .error(R.drawable.ic_logo)
            .placeholder(R.drawable.img_placeholder_profile)
            .into(binding.civUserPic)
        if(private==1){
            binding.tvBotomUserProfile.text = ""
        }else{
            binding.tvBotomUserProfile.text = "Tap : ${tiks.toString()}"
        }
        if(verified== 1){
            binding.ivBlueTick.visibility= View.VISIBLE
        }else{
            binding.ivBlueTick.visibility= View.GONE
        }
        if (private == 1 && connected == 0) {
            binding.tvNotPublic.visibility = View.VISIBLE
            binding.rvPlatforms.visibility = View.GONE
        } else {
            binding.rvPlatforms.visibility = View.VISIBLE
            binding.tvNotPublic.visibility = View.GONE
            changeConnectedBtnState()

//            adapter.renewlist(
//                getPlatFormsFromCategories(categories)
//            )
        }
    }

    private fun setsearchProifiledata(
        name: String?,
        username: String?,
        bio: String?,
        private: Int,
//        categories: List<Categories>,
        photo: String?,
        tiks: Int
//        verified: Int
    ) {

        search = 1
        binding.tvNameOther.text=name
        binding.tvUsernameOther.text = username
        binding.tvBio.text = bio?:""
        Glide.with(this)
            .load("${Constants.BASE_URL}$photo")
            .placeholder(R.drawable.ic_logo)
            .into(binding.civUserPic)
        if(private==1){
            binding.tvBotomUserProfile.text = ""
        }else{
            binding.tvBotomUserProfile.text = "Tap : ${tiks.toString()}"
        }
//        if(verified== 1){
//            binding.ivBlueTick.visibility= View.VISIBLE
//        }else{
//            binding.ivBlueTick.visibility= View.GONE
//        }
        if (private == 1 && connected == 0) {
            binding.tvNotPublic.visibility = View.VISIBLE
            binding.rvPlatforms.visibility = View.GONE
        } else {
            binding.rvPlatforms.visibility = View.VISIBLE
            binding.tvNotPublic.visibility = View.GONE
            changeConnectedBtnState()
//            adapter.renewlist(
//                getPlatFormsFromCategories(categories)
//            )
        }



//        binding.tvUserName.text = username
//        binding.tvBio.text = bio ?: ""
//        binding.tvBotomUserProfile.text = "Knack : $tiks"
//        Glide.with(this)
//            .load("${Constants.BASE_URL}$photo")
//            .placeholder(R.drawable.img_orignal_logo)
//            .into(binding.civUserPic)
//        adapter.renewlist(
//            getPlatFormsFromCategories(categories)
//        )


    }

//    private fun getPlatFormsFromCategories(mcategories: List<Categories>): MutableList<Platforms> {
//        platforms.clear()
//        for (category in mcategories) {
//            for (platform in category.platforms) {
//                if (platform.saved == 1) {
//                    platforms.add(platform)
//
//                    if (platform.direct == 1) {
//
//                        when (platform.title) {
//                            Constants.PLATFORM_TEXT -> {
//                                openMessages()
//
//                            }
//                            Constants.PLATFORM_ADDRESS -> {
//                                openMap()
//                            }
//                            Constants.PLATFORM_EMAIL -> {
//                                composeEmail(Constants.PLATFORMS_PATH)
//
//                            }
//                            else -> {
//                               if (!platform.baseURL.isNullOrEmpty()){
//                                   val browserIntent =
//                                       Intent(
//                                           Intent.ACTION_VIEW,
//                                           Uri.parse("${platform.baseURL}${platform.path}")
//                                       )
//                                   startActivity(browserIntent)
//                               }
//                            }
//                        }
//
//                        requireActivity().finish()
//                    }
//                }
//            }
//        }
//
//        return platforms
//    }

    private fun setRecyclerview(userPlatforms: List<UserPlatforms>) {
        platforms = ArrayList()
        binding.rvPlatforms.setHasFixedSize(true)
        binding.rvPlatforms.setNestedScrollingEnabled(false);
        binding.rvPlatforms.layoutManager =
            GridLayoutManager(App.getAppContext(), 2, RecyclerView.VERTICAL, false)
        adapter = ProfilePlatformsAdapter(userPlatforms.toMutableList(), requireContext(), true)
        binding.rvPlatforms.adapter = adapter
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        drawerHandlerCallback = context as DrawerHandler
    }

    private fun openMessages() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("smsto:" + Uri.encode(Constants.PLATFORMS_PATH))
        startActivity(intent)
    }

    fun composeEmail(
        addresses: String
    ) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses)
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun openMap() {

        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://maps.google.co.in/maps?q=${Constants.PLATFORMS_PATH}")
        )

        startActivity(intent)

    }
}
