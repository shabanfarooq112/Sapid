package com.horizam.sapid.activities

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.tech.NfcF
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.horizam.sapid.App
import com.horizam.sapid.Constants
import com.horizam.sapid.Constants.Companion.NFC_INTENT
import com.horizam.sapid.R
import com.horizam.sapid.callbacks.DrawerHandler
import com.horizam.sapid.databinding.ActivityMainBinding
import com.horizam.sapid.databinding.NavHeaderMainBinding
import com.horizam.sapid.fragments.BottomSheetRead
import com.horizam.sapid.networking.ApiListener
import com.horizam.sapid.networking.NetworkingModel
import com.horizam.sapid.networking.request.UpdateProfileRequest
import com.horizam.sapid.networking.response.UserResponse
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class MainActivity : AppCompatActivity(), DrawerHandler {

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingHeader: NavHeaderMainBinding
    private lateinit var navController: NavController
    private lateinit var prefManager: PrefManager
    var selected = 0

    //not working for now
//    lateinit var readFromNfcCard: ReadFromNfcCard;

    var intentFiltersArray: Array<IntentFilter>? = null
    private val techListsArray = arrayOf(arrayOf(NfcF::class.java.name))
    var iswrite = "0"
    var userEmail = "";
    var type = "";
    var username: String? = ""
    var gender = 0
    var dob: String? = ""
    var bio: String? = ""
    var pendingIntent: PendingIntent? = null
    private lateinit var networkingModel: NetworkingModel
    private val nfcAdapter: NfcAdapter? by lazy {
        NfcAdapter.getDefaultAdapter(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        ChangeLanguage.setLocale()


        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        if (isOnline()) {

        } else {
            val alertDialog = AlertDialog.Builder(this).create()

            alertDialog.setTitle("Ezlinq")
            alertDialog.setMessage("Your internet connection appears to be offline")
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert)
            alertDialog.setCancelable(false)
            alertDialog.setButton(
                "OK"
            ) { dialog, which -> alertDialog.dismiss() }

            alertDialog.show()
            val messageView: TextView = alertDialog.findViewById(android.R.id.message) as TextView
            messageView.setGravity(Gravity.CENTER)
        }
        initViews()


        exeUserprofileApi()
        initNfc()
        readTikl()
        getIntentValue()
        searchList()
        setNavDrawer()
        setClickListeners()
    }

    private fun getIntentValue() {
        if (intent.hasExtra("load")) {
            if (intent.getStringExtra("load").equals("OtherUserProfileFragment")) {
                var bundle = Bundle()
                bundle.putString("url", intent.getStringExtra("url"))
                navController.navigate(R.id.tikl_profile_fragment, bundle)
            }
        }
    }

    private fun settoogleApi() {

        var updateProfileRequest = UpdateProfileRequest(
            username,
            null,
            null,
            null,
            null,
            null,
            gender.toString(),
            null,
            dob,
            bio,
            selected
        )
        upDateProfileWithoutImage(updateProfileRequest)
    }


    private fun exeUserprofileApi() {
//        showMainLoader()


        networkingModel = NetworkingModel()
        prefManager = PrefManager(this)
        val thisUser = prefManager.getUserId().toString()
        networkingModel.exeUserprofileApi(thisUser, object : ApiListener<UserResponse> {
            override fun onSuccess(body: UserResponse?) {
                //                hideMainLoader()
                body.also {

                    if (it!!.status == 200) {
                        Constants.userResponse = it
                        username = it.profile!!.username
//                        gender = it.profile.gender
                        dob = it!!.profile!!.dob
                        bio = it!!.profile!!.bio
                        if (username == null) {
                            Toast.makeText(
                                this@MainActivity,
                                resources.getString(R.string.please_enter_your_username),
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent =
                                Intent(this@MainActivity, UserNameActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(
                                intent
                            )
                            finish()
                            return
                        }

                        var pos = it!!.profile!!.private

                        selected = pos!!
                        showtoogle()


                    } else {
                        Toast.makeText(App.getAppContext(), it.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            override fun onFailure(error: Throwable) {
                //                hideMainLoader()
                Toast.makeText(App.getAppContext(), "$error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun upDateProfileWithoutImage(updateProfileRequest: UpdateProfileRequest) {
        networkingModel.exeUpdateProfileApi(
            updateProfileRequest,
            object : ApiListener<UserResponse> {
                override fun onSuccess(body: UserResponse?) {
                    if (body != null) {
                        if (body.status == 200) {

                        }
                    }
                }

                override fun onFailure(error: Throwable) {
                    Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_SHORT)
                        .show()
                }
            })

    }

    private fun readTikl() {
        val userIdNfc = intent.getStringExtra(NFC_INTENT)
        if (!userIdNfc.isNullOrEmpty()) {
            val strs = userIdNfc.split("/").toTypedArray()
            val userApikey = strs[strs.size - 1]
            val strs1 = userApikey.split("?").toTypedArray()
            val userApikey1 = strs1[0]
            if (userApikey1 != prefManager.getUserName()) {
                Toast.makeText(App.getAppContext(), userIdNfc, Toast.LENGTH_SHORT).show()
                prefManager = PrefManager(this)
                prefManager.setUserTiklData(userIdNfc)
                prefManager.setUserSearchName("")
                navController.navigate(R.id.tikl_profile_fragment)
            }
        }
    }

    private fun searchList() {
        val userId = intent.getStringExtra(Constants.SEARCH_INTENT)
        if (!userId.isNullOrEmpty()) {
            Toast.makeText(App.getAppContext(), userId, Toast.LENGTH_SHORT).show()
            prefManager = PrefManager(this)
            prefManager.setUserSearchName(userId)
            prefManager.setUserTiklData("")
            navController.navigate(R.id.tikl_profile_fragment)
        }
    }

    private fun initNfc() {
//        readFromNfcCard = ReadFromNfcCard(App.getAppContext()!!)


        pendingIntent = PendingIntent.getActivity(
            this, 0, Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
        )
        val ndef = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        try {
            ndef.addDataType("text/uri-list")
        } catch (e: IntentFilter.MalformedMimeTypeException) {
            throw RuntimeException("fail", e)
        }
        intentFiltersArray = arrayOf(ndef)


    }

    private fun setClickListeners() {
        binding.ivNavEditProfile.setOnClickListener {
            startActivity(Intent(this@MainActivity, EditProfileActivity::class.java))
        }
        binding.ivNavReadTickl.setOnClickListener {
            val bottomSheet = BottomSheetRead()
            bottomSheet.show(supportFragmentManager, "exampleBottomSheet")
        }
        binding.navTikl.setOnClickListener {
            if (navController.currentDestination!!.id != R.id.twohome_fragment) {
                navController.navigate(R.id.twohome_fragment)
            }
        }
        binding.ivNavSearchpeople.setOnClickListener {
            navController.navigate(R.id.search_fragment)
        }
        binding.ivNevQr.setOnClickListener {
            startActivity(Intent(this@MainActivity, QrscanActivity::class.java))
        }
        bindingHeader.tvTiklOff.setOnClickListener {
            selected = 1
            showtoogle()
        }
        bindingHeader.tvTiklOn.setOnClickListener {
            selected = 0
            showtoogle()
        }

        val headerView: View = binding.navView.getHeaderView(0)
        var cvEnglish: CardView = headerView.findViewById(R.id.cv_english)
        var cvSpanish: CardView = headerView.findViewById(R.id.cv_spanish)
        var rlLang: RelativeLayout = headerView.findViewById(R.id.rl_lang)

        if (prefManager.getLang() == "en") {
            cvEnglish.visibility = View.VISIBLE
            cvSpanish.visibility = View.INVISIBLE
        } else {
            cvEnglish.visibility = View.INVISIBLE
            cvSpanish.visibility = View.VISIBLE
        }

        cvEnglish.setOnClickListener {
            cvEnglish.visibility = View.VISIBLE
            cvSpanish.visibility = View.INVISIBLE
            prefManager.setLang("en")
            var intent = Intent(this, SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        rlLang.setOnClickListener {
            if (cvSpanish.visibility == View.VISIBLE) {
                cvEnglish.visibility = View.VISIBLE
                cvSpanish.visibility = View.INVISIBLE
                prefManager.setLang("en")
                var intent = Intent(this, SplashActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                cvEnglish.visibility = View.INVISIBLE
                cvSpanish.visibility = View.VISIBLE
                prefManager.setLang("es")
                var intent = Intent(this, SplashActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }

        cvSpanish.setOnClickListener {
            cvEnglish.visibility = View.INVISIBLE
            cvSpanish.visibility = View.VISIBLE
            prefManager.setLang("es")
            var intent = Intent(this, SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

    }

    private fun showtoogle() {
        if (selected == 1) {
            bindingHeader.tvTiklOn.setTextColor(Color.parseColor("#000000"))
            bindingHeader.tvTiklOn.setBackgroundColor(Color.parseColor("#ffffff"))
            bindingHeader.tvTiklOff.setTextColor(Color.parseColor("#ffffff"))
            bindingHeader.tvTiklOff.setBackgroundColor(Color.parseColor("#000000"))
        } else {
            bindingHeader.tvTiklOff.setTextColor(Color.parseColor("#000000"))
            bindingHeader.tvTiklOff.setBackgroundColor(Color.parseColor("#ffffff"))
            bindingHeader.tvTiklOn.setTextColor(Color.parseColor("#ffffff"))
            bindingHeader.tvTiklOn.setBackgroundColor(Color.parseColor("#000000"))
        }
        settoogleApi()

    }

    fun isOnline(): Boolean {
        val conMgr: ConnectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo: NetworkInfo? = conMgr.getActiveNetworkInfo()
        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            Toast.makeText(this, "No Internet connection!", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun initViews() {
        navHostFragment =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?)!!
        navController = navHostFragment.navController
    }

    private fun setNavDrawer() {
        val navHeader = binding.navView.getHeaderView(0)
        bindingHeader = NavHeaderMainBinding.bind(navHeader)
        setNavHeader()
        setNavClicks()
    }

    private fun setNavClicks() {
        binding.navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home_fragment -> {
                    // Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_edit_profile_fragment -> {
                    // Toast.makeText(this, "Edit Profile", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, EditProfileActivity::class.java))
                }
                R.id.nav_activate_fragment -> {
                    // Toast.makeText(this, "Activate", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, ActivateActivity::class.java))
                }
                R.id.nav_read_a_tickle_fragment -> {
                    //Toast.makeText(this, "Read tikl", Toast.LENGTH_SHORT).show()`
                    val bottomSheet = BottomSheetRead()
                    bottomSheet.show(supportFragmentManager, "exampleBottomSheet")
                }
                R.id.nav_QR_fragment -> {
                    // Toast.makeText(this, "QR fragment", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, QrscanActivity::class.java))
                }
                R.id.nav_Buy_tickle_fragment -> {
                    // Toast.makeText(this, "buy tikl", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, BuyTiklActivity::class.java))
                }
//                R.id.nav_go_pro_famgment -> {
//                    Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show()
//                }
                R.id.nav_home_to_tick_fagment -> {
                    startActivity(Intent(this, HowToTiklActivity::class.java))
                }
                R.id.nav_logout_framment -> {
                    val dialog = AlertDialog.Builder(this)
                    dialog.setIcon(R.drawable.ic_logo)
                    //performing positive action
                    dialog.setTitle(resources.getString(R.string.confirm))
                    dialog.setMessage(resources.getString(R.string.are_you_sure_want_to_logout))
                    dialog.setPositiveButton(resources.getString(R.string.yes)) { _, _ ->

                        Toast.makeText(
                            this,
                            resources.getString(R.string.logout),
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(App.getAppContext(), WelcomeActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        prefManager.clearAll()
                        finish()

                    }
                    dialog.setNegativeButton(resources.getString(R.string.no)) { dialogInterface: DialogInterface, _: Int -> dialogInterface.dismiss() }

                    dialog.show()

                }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }
    }

    private fun setNavHeader() {

//        val content = SpannableString(prefManager.getUserName())
//        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        bindingHeader.tvHeaderUserName.text = prefManager.getUserName()!!.toUpperCase()
        setVersionName()
    }

    private fun setVersionName() {
        try {
            val pInfo: PackageInfo = packageManager.getPackageInfo(packageName, 0)
            val version = pInfo.versionName
            binding.tvVersionName.text = "Version $version"
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(private: Int) {
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun openDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }

    private fun clearPrefManager() {
        prefManager = PrefManager(this)
        prefManager.editor.clear()
        prefManager.editor.apply()
    }

    override fun onResume() {
        super.onResume()
        exeUserprofileApi()
//        readFromNfcCard.activeNfc(this)

        nfcAdapter?.enableForegroundDispatch(
            this,
            pendingIntent,
            intentFiltersArray,
            techListsArray
        )

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
//        readFromNfcCard.onIntentAction(intent, App.getAppContext()!!)


        val action = intent!!.action
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == action) {

            val parcelables = intent!!.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            with(parcelables) {
                try {
                    val inNdefMessage = this!![0] as NdefMessage
                    val inNdefRecords = inNdefMessage.records

                    val ndefRecord_0 = inNdefRecords[0]
                    var inMessage = String(ndefRecord_0.payload)
                    if (inMessage.isNotEmpty()) {
                        navController.navigate(R.id.tikl_profile_fragment)
                    }


                } catch (ex: Exception) {
                    Toast.makeText(
                        applicationContext,
                        resources.getString(R.string.failed_to_read_Data), Toast.LENGTH_SHORT
                    ).show()
                }
            }


        }
    }
}












