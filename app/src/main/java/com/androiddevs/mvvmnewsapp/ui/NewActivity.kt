package com.androiddevs.mvvmnewsapp.ui

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.*
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.db.ArticleDatabase
import com.androiddevs.mvvmnewsapp.repository.NewsRepository
import com.huawei.agconnect.config.AGConnectServicesConfig
import com.huawei.hms.aaid.HmsInstanceId
import com.huawei.hms.ads.AdListener
import com.huawei.hms.ads.AdParam
import com.huawei.hms.ads.HwAds
import com.huawei.hms.ads.InterstitialAd
import com.huawei.hms.ads.banner.BannerView
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.account.AccountAuthManager
import com.huawei.hms.support.account.request.AccountAuthParams
import com.huawei.hms.support.account.request.AccountAuthParamsHelper
import com.huawei.hms.support.account.service.AccountAuthService
import kotlinx.android.synthetic.main.activity_news.*


class NewActivity : AppCompatActivity() {


    lateinit var viewModel: NewsViewModel
    private lateinit var bottomBannerView: BannerView
    private lateinit var adParam: AdParam
    lateinit var mAuthParam: AccountAuthParams
    lateinit var mAuthManager: AccountAuthService
    lateinit var graph: NavGraph
    private var interstitialAd: InterstitialAd? = null
    private var pushtoken: String? = null


    val navController: NavController by lazy {
        findNavController(R.id.newsNavHostFragment)

    }
    val appBarConfiguration: AppBarConfiguration by lazy {
        AppBarConfiguration(
            setOf(
                R.id.breakingNewsFragment,
                R.id.savedNewsFragment,
                R.id.searchNewsFragment
            )
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        init()
        eventHandler()
        getToken()
        Log.i("ab", "onCreateFromActivity")


    }

    override fun onStart() {
        super.onStart()
        Log.i("ab", "onStartFromActivity")


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        when (item.itemId) {
            R.id.cancel_auth -> {

                mAuthManager.cancelAuthorization().addOnSuccessListener {
                    Toast.makeText(this, "Cancelled authorization", Toast.LENGTH_SHORT).show()
                    clearBackStack()


                }
            }
            R.id.sign_out -> {

                showInterstitialAd()
                mAuthManager.signOut().addOnSuccessListener {
                    Toast.makeText(this, "Signed out succesfully", Toast.LENGTH_SHORT).show()
                    clearBackStack()
                }

            }
        }
        return true
    }

    private fun showInterstitialAd() {

        if (interstitialAd != null && interstitialAd!!.isLoaded) {
            interstitialAd!!.show()
        } else {
            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearBackStack() {

        navController.graph = graph


    }

    private fun eventHandler() {

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.breakingNewsFragment) {
                bottomNavigationView.visibility = View.VISIBLE
                bottomBannerView.visibility = View.VISIBLE
                supportActionBar?.show()
            }

            if (destination.id == R.id.searchNewsFragment) {
                bottomNavigationView.visibility = View.VISIBLE
                bottomBannerView.visibility = View.VISIBLE
                supportActionBar?.show()
            }

            if (destination.id == R.id.savedNewsFragment) {
                bottomNavigationView.visibility = View.VISIBLE
                bottomBannerView.visibility = View.VISIBLE
                supportActionBar?.show()
            }
            if (destination.id == R.id.mapFragment) {
                bottomNavigationView.visibility = View.GONE
                bottomBannerView.visibility = View.GONE
                supportActionBar?.hide()
            }
            if (destination.id == R.id.loginScreen) {
                bottomNavigationView.visibility = View.GONE
                bottomBannerView.visibility = View.GONE
                supportActionBar?.hide()
            }
            if (destination.id == R.id.articleFragment) {
                bottomBannerView.visibility = View.GONE
            }

        }

        bottomBannerView.adListener = object : AdListener() {

            override fun onAdClosed() {
                Toast.makeText(
                    this@NewActivity, "Haber çeşitliliği azaltılacak",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    private fun bannerAds() {
        bottomBannerView = findViewById<BannerView>(R.id.hw_banner_view)
        adParam = AdParam.Builder().build()
        bottomBannerView.loadAd(adParam)

    }

    private fun interstitialAds() {
        interstitialAd = InterstitialAd(this)
        interstitialAd!!.adId = "testb4znbuh3n2"
        val adParam = AdParam.Builder().build()
        interstitialAd!!.loadAd(adParam)

    }

    fun setUpAds() {
        HwAds.init(this)
        bannerAds()
        interstitialAds()


    }

    private fun init() {
        setUpAds()




        mAuthParam =
            AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setIdToken()
                .createParams()
        mAuthManager = AccountAuthManager.getService(this@NewActivity, mAuthParam)



        bottomNavigationView
            .setupWithNavController(navController)

        setupActionBarWithNavController(navController, appBarConfiguration)


        val newRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        graph = navController.navInflater.inflate(R.navigation.news_nav_graph)


    }

    override fun onSupportNavigateUp(): Boolean {

        return navController.navigateUp()

    }

    private fun getToken() {
        // Create a thread.
        object : Thread() {
            override fun run() {
                try {
                    // Obtain the app ID from the agconnect-service.json file.
                    val appId = AGConnectServicesConfig.fromContext(this@NewActivity).getString("client/app_id")

                    // Set tokenScope to HCM.
                    val tokenScope = "HCM"
                    val token = HmsInstanceId.getInstance(this@NewActivity).getToken(appId, tokenScope)
                    Log.d("abc", "get token:$token")

                } catch (e: ApiException) {
                    Log.d("abc", "get token failed, $e")
                }
            }
        }.start()
    }
}
