 package com.horizam.sapid.fragments;

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.horizam.sapid.App
import com.horizam.sapid.Constants
import com.horizam.sapid.Constants.Companion.BASE_URL
import com.horizam.sapid.Constants.Companion.USER_BIO
import com.horizam.sapid.Constants.Companion.USER_NAME
import com.horizam.sapid.R
import com.horizam.sapid.activities.*
import com.horizam.sapid.adapters.ProfilePlatformsAdapter
import com.horizam.sapid.callbacks.DrawerHandler
import com.horizam.sapid.databinding.FragmentProfile2Binding

import com.horizam.sapid.event_bus_events.BackFromBottomSheetHome
import com.horizam.sapid.networking.ApiListener
import com.horizam.sapid.networking.NetworkingModel
import com.horizam.sapid.networking.request.DirectOnOffRequest
import com.horizam.sapid.networking.response.*
import com.horizam.sapid.utils.ItemMoveCallback
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.io.FileWriter
import java.io.IOException
import kotlin.collections.ArrayList


class HomePlatformsFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var binding: FragmentProfile2Binding
    private lateinit var drawerHandlerCallback: DrawerHandler
    private lateinit var adapter: ProfilePlatformsAdapter
    private lateinit var networkingModel: NetworkingModel
    private lateinit var prefManager: PrefManager
    private lateinit var touchHelper: ItemTouchHelper
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var platforms: MutableList<UserPlatforms>
    lateinit var categories: List<Categories>
    var direct: Int = 0
    lateinit var userData: UserProfileRespose;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfile2Binding.inflate(inflater, container, false)
        inIt()
        clickListeners()
        setRecyclerview()
        exeUserprofileApi()
        return binding.root
    }

    private fun inIt() {
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        swipeRefreshLayout = binding.layoutRefresh
        swipeRefreshLayout.setOnRefreshListener {
            exeUserprofileApi()
        }
    }




    private fun exeUserprofileApi() {
        showMainLoader()
        networkingModel = NetworkingModel()
        prefManager = PrefManager(this.requireContext())
        val thisUser = prefManager.getUserId().toString()
        if (thisUser != null) {
            networkingModel.exeUserprofileApi(thisUser, object : ApiListener<UserResponse> {
                override fun onSuccess(body: UserResponse?) {
                    swipeRefreshLayout.isRefreshing = false
                                    hideMainLoader()
                    body.also {
                        if (it != null && it.status == 200) {
                            Constants.userResponse = it
                            if(it.profile!!.userPlatforms != null){
                                platforms = it.profile!!.userPlatforms as MutableList<UserPlatforms>
                            }
                            if(adapter != null){
                                adapter.renewlist(platforms)
                            }
//                            categories = it.data.categories
//                            userData = it
                            setProfiledata(
                                it.profile!!.firstName,
                                it.profile!!.username,
                                it.profile!!.bio,
                                it.profile!!.private!!,
                                it.profile!!.photo,
                                it.profile!!.tiks!!
//                                it.data.categories!!,
//                                getPlatformsDirect(it.data.categories),

//                                it.data.user.verified
                            )
                        } else {
                            if (it!!.message == "Profile not found") {
                                Toast.makeText(
                                    App.getAppContext(),
                                    "${it.message} Your Username maybe changed",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()

                                val intent =
                                    Intent(App.getAppContext(), WelcomeActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                prefManager.clearAll()
                                activity!!.finish()


                            } else {
//                                Toast.makeText(
//                                    activity,
//                                    it.message,
//                                    Toast.LENGTH_SHORT
//                                ).show()
                            }
                        }
                    }
                }

                override fun onFailure(error: Throwable) {
                    //                hideMainLoader()
                    Toast.makeText(App.getAppContext(), "$error", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun setProfiledata(
        name: String?,
        userName: String?,
        bioString: String?,
        private: Int,
        photo: String?,
//        mcategories: List<Categories>,
        tiks: Int,
//        verified: Int
    ) {

//        adapter.renewlist(
//            getPlatFormsFromCategories(mcategories)
//        )
        USER_NAME = userName ?: ""

        USER_BIO = bioString ?: ""
        binding.tvNameProfile1.text=name
        binding.tvUsernameProfile1.text = USER_NAME
        binding.tvBioProfile1.text = USER_BIO
        binding.tvBotomUserProfile.text = "Tap : ${tiks.toString()}"
//        changeDirectButtonState(mdirect)
//        prefManager.setDirectState(mdirect)
//        if(verified== 1){
//            binding.ivBlueTick.visibility= View.VISIBLE
//        }else{
//            binding.ivBlueTick.visibility= View.GONE
//        }

        Glide.with(this)
            .load("$BASE_URL$photo")
            .error(R.drawable.ic_logo_drawable)
            .placeholder(R.drawable.ic_logo_drawable)
            .into(binding.civUserPicProfileOne)
    }
    private fun showMainLoader() {
        binding.progressBarMain.visibility = View.VISIBLE
    }

    private fun hideMainLoader() {
        binding.progressBarMain.visibility = View.GONE
    }

    private fun changeDirectButtonState(mDirect: Int) {
        if (mDirect == 0) {
            binding.tvTikleGuideProfile1.text= resources.getString(R.string.your_tikl_opens_directly_to_your_first_link)

            binding.btnDirectOnOff.text = resources.getString(R.string.direct_off)
            binding.btnDirectOnOff.backgroundTintList =
                ContextCompat.getColorStateList(App.getAppContext()!!, R.color.white)
            binding.btnDirectOnOff.setTextColor(
                ContextCompat.getColor(
                    App.getAppContext()!!,
                    R.color.app_color
                )
            )
            direct = 1
        } else {
            binding.tvTikleGuideProfile1.text= resources.getString(R.string.direct_on_str)
            binding.btnDirectOnOff.text = resources.getString(R.string.direct_on)
            binding.btnDirectOnOff.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.app_color)
            binding.btnDirectOnOff.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            direct = 0
        }

    }

    private fun getPlatformsDirect(
        mcategories: List<Categories>
    ): Int {
        platforms.clear()
        for (category in mcategories) {
            for (platform in category.platforms) {
//                if (platform.direct == 1) {
//                    platforms.add(platform)
//                    return 1
//                }
            }
        }
        return 0
    }

//    private fun getPlatFormsFromCategories(mcategories: List<Categories>): MutableList<Platforms> {
//
//        platforms.clear()
//        for (category in mcategories) {
//            for (platform in category.platforms) {
////                if (platform.saved == 1) {
////                    platforms.add(platform)
////                }
//            }
//        }
//
////         platforms.sortByDescending {
////            it.platform_order
////        }
//        platforms.reverse()
////        val bookPriceComparator: Comparator<Platforms> =
////            Comparator<Platforms> { b1, b2 -> b1.platform_order.compareTo(b2.platform_order) }
////        Collections.sort(platforms, bookPriceComparator)
////        Toast.makeText(requireContext(),"${platforms[0].platform_order}",Toast.LENGTH_SHORT).show()
//        return platforms
//
//    }

    private fun clickListeners() {
        binding.ivChangeFragmentBack.setOnClickListener { navController.navigate(R.id.home_fragment) }
        binding.ivQrcodetwo.setOnClickListener(View.OnClickListener {
            startActivity(Intent(activity, QrCameraCodeScannerActivity::class.java))
        })
        binding.navHomeSecond.setOnClickListener {
            drawerHandlerCallback.openDrawer()
        }
        binding.civUserPicProfileOne.setOnClickListener {
            startActivity(Intent(activity, EditProfileActivity::class.java))
        }
        binding.btnVcfProfile.setOnClickListener {
            //makeVFCofProfile()
            startActivity(Intent(activity, EditProfileActivity::class.java))
        }
        binding.btnDirectOnOff.setOnClickListener {
            if (platforms.size > 0) {
                exeChangeDirectButtonState()
            } else {
                Toast.makeText(
                    context,
                    getString(R.string.Please_Select_at_least_One_Platform_to_select_Enable_Direct),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    private fun makeVFCofProfile() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(requireContext(), READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED || checkSelfPermission(
                    requireContext(),
                    WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED
            ) {
                //permission denied
                val permissions = arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE);
                //show popup to request runtime permission
                requestPermissions(permissions, Constants.PERMISSION_CODE);
            } else {
                writeVFC()

            }
        } else {
            writeVFC()

        }

    }

    private fun writeVFC() {

        try {
            val vdfdirectory = File(
                "/storage/emulated/0/"
            )

            // have the object build the directory structure, if needed.
            if (!vdfdirectory.exists()) {
                vdfdirectory.mkdirs();
            }

            var vcfFile =
                File(vdfdirectory, "${userData.data.user.username}.vcf");

            var fw: FileWriter? = null;
            fw = FileWriter(vcfFile);
            fw.write("BEGIN:VCARD\r\n");
            fw.write("VERSION:3.0\r\n");
            // fw.write("N:" + p.getSurname() + ";" + p.getFirstName() + "\r\n");
            fw.write("FN: ${userData.data.user.username} \r\n");
            //  fw.write("ORG:" + p.getCompanyName() + "\r\n");
            //  fw.write("TITLE:" + p.getTitle() + "\r\n");
            fw.write("TEL;TYPE=WORK,VOICE: ${userData.data.user.phone} \r\n");
            //   fw.write("TEL;TYPE=HOME,VOICE:" + p.getHomePhone() + "\r\n");
            //   fw.write("ADR;TYPE=WORK:;;" + p.getStreet() + ";" + p.getCity() + ";" + p.getState() + ";" + p.getPostcode() + ";" + p.getCountry() + "\r\n");
            fw.write("EMAIL;TYPE=PREF,INTERNET: ${userData.data.user.email} \r\n");
            fw.write("END:VCARD\r\n");
            fw.close();

            /* Intent i = new Intent(); //this will import vcf in contact list
             i.setAction(android.content.Intent.ACTION_VIEW);
             i.setDataAndType(Uri.fromFile(vcfFile), "text/x-vcard");
             startActivity(i);*/
            val dialog = AlertDialog.Builder(context)
            dialog.setIcon(R.drawable.ic_logo)
            //performing positive action
            dialog.setTitle(resources.getString(R.string.create_vcf))
            dialog.setMessage("${resources.getString(R.string.vfc_created_at)}${vcfFile.absolutePath}")

            dialog.setNegativeButton("Ok") { dialogInterface: DialogInterface, _: Int -> dialogInterface.dismiss() }

            dialog.show()

        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("!@#", e.message!!);
        }
    }


    private fun exeChangeDirectButtonState() {
        var directOnOffRequest =
            DirectOnOffRequest(platforms[0].id!!, direct,platforms[0].path!!)
        changeDirectButton(directOnOffRequest)
    }


    private fun changeDirectButton(directOnOffRequest: DirectOnOffRequest) {
        showLoadingOnButton()

        networkingModel.exeDirectOnOff(
            directOnOffRequest,
            object : ApiListener<UserResponse> {
                override fun onSuccess(body: UserResponse?) {
                    stopLoadingOnButton(direct)
                    if (body != null) {

                        if (body.status == 200) {
                            changeDirectButtonState(
                                body.profile!!.direct!!
                            )
                            prefManager.setDirectState(body.profile!!.direct!!)

                            exeUserprofileApi()

                        }
                    }
                }

                override fun onFailure(error: Throwable) {
                    stopLoadingOnButton(direct - 1)

                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT)
                        .show()
                }


            })
    }

    private fun stopLoadingOnButton(onOff: Int) {

        val status = if (onOff == 0) {
            resources.getString(R.string.direct_off)
        } else {
            resources.getString(R.string.direct_on)


        }
        if (onOff == 0) {
            binding.tvTikleGuideProfile1.text= resources.getString(R.string.your_tikl_opens_directly_to_your_first_link)

            binding.btnDirectOnOff.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.white)
            binding.btnDirectOnOff.setTextColor(
                ContextCompat.getColorStateList(requireContext(), R.color.app_color)
            )
        } else {
            binding.tvTikleGuideProfile1.text= resources.getString(R.string.direct_on_str)

            binding.btnDirectOnOff.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.app_color)
            binding.btnDirectOnOff.setTextColor(
                ContextCompat.getColorStateList(requireContext(), R.color.white)
            )
        }

        binding.btnDirectOnOff.isClickable = true
        binding.btnDirectOnOff.text = status
        binding.progressDirectOnOff.visibility = View.GONE

    }

    private fun showLoadingOnButton() {
        binding.btnDirectOnOff.backgroundTintList =
            ContextCompat.getColorStateList(requireContext(), R.color.app_color)
        binding.btnDirectOnOff.isClickable = false
        binding.btnDirectOnOff.text = ""
        binding.progressDirectOnOff.visibility = View.VISIBLE


    }

    private fun setRecyclerview() {

        platforms = ArrayList();

        binding.rvPlatforms.setHasFixedSize(true)
        binding.rvPlatforms.isNestedScrollingEnabled = false;

        binding.rvPlatforms.layoutManager =
            GridLayoutManager(App.getAppContext(), 2, RecyclerView.VERTICAL, false)
        adapter = ProfilePlatformsAdapter(platforms, requireContext())

        val callback: ItemTouchHelper.Callback = ItemMoveCallback(adapter)
        touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.rvPlatforms)
        binding.rvPlatforms.adapter = adapter

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        drawerHandlerCallback = context as DrawerHandler
    }

    companion object {
        var myContext: Context? = null
        fun getContext(): Context {
            return myContext!!
        }
    }

    override fun onStart() {

        EventBus.getDefault().register(this)
        super.onStart()

    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(BackFromBottomSheetHome: BackFromBottomSheetHome) {
        exeUserprofileApi()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(platform: Platforms) {
//        var addPlatformRequest =
//            AddPlatformRequest(platform.id, platform.path, platform.label, 1)
//        changeDirectButton(addPlatformRequest)
    }

    override fun onResume() {
        super.onResume()
        exeUserprofileApi()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            Constants.PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    writeVFC()
                } else {
                    //permission from popup denied
                    Toast.makeText(
                        context,
                        resources.getString(R.string.permission_denied),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }

}

