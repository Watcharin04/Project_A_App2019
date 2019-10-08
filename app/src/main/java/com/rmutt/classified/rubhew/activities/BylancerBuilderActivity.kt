package com.rmutt.classified.rubhew.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.rmutt.classified.rubhew.R
import com.rmutt.classified.rubhew.chat.ChatActivity
import com.rmutt.classified.rubhew.login.LoginRequiredActivity
import com.rmutt.classified.rubhew.login.LoginActivity
import com.rmutt.classified.rubhew.login.ManualLoginActivity
import com.rmutt.classified.rubhew.login.RegisterUserActivity
import com.rmutt.classified.rubhew.splash.SplashActivity
import com.rmutt.classified.rubhew.uploadproduct.categoryselection.UploadCategorySelectionActivity
import com.rmutt.classified.rubhew.utils.AppConstants
import com.rmutt.classified.rubhew.utils.SessionState
import com.rmutt.classified.rubhew.utils.Utility
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import java.util.*

/**
 * Created by Ani on 3/20/18.
 */
abstract class BylancerBuilderActivity : AppCompatActivity() {
    lateinit var mInterstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        isRTLSupportRequired()
        super.onCreate(savedInstanceState)
        if(this::class.simpleName != LoginRequiredActivity::class.simpleName &&
                this::class.simpleName != LoginActivity::class.simpleName &&
                this::class.simpleName != ChatActivity::class.simpleName &&
                this::class.simpleName != UploadCategorySelectionActivity::class.simpleName) {
            Utility.slideActivityRightToLeft(this)
        } else {
            Utility.slideActivityBottomToTop(this)
        }
        setContentView(setLayoutView())

        setOrientation() // Fixing Android O issue

        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = getString(R.string.ad_mob_interstitial_ad_unit)
        loadInterstitialAd()
        addInterstitialAdListener()

        if (SessionState.instance.isGoogleInterstitialSupported
                && this::class.simpleName != LoginRequiredActivity::class.simpleName
                && this::class.simpleName != LoginActivity::class.simpleName
                && this::class.simpleName != RegisterUserActivity::class.simpleName
                && this::class.simpleName != SplashActivity::class.simpleName
                && this::class.simpleName != ManualLoginActivity::class.simpleName) {
            scheduleInterstitialAd()
        }

        initialize(savedInstanceState)
    }

    protected abstract fun setLayoutView(): Int

    protected abstract fun initialize(savedInstanceState: Bundle?)

    fun startActivity(clazz: Class<out Activity>, isNewTask:Boolean) {
        val intent = Intent(this, clazz)
        if(isNewTask) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    fun startActivityWithAffinity(clazz: Class<out Activity>, isNewTask:Boolean) {
        val intent = Intent(this, clazz)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    fun setOrientation() {
       // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    }

    fun startActivity(clazz: Class<out Activity>, isNewTask:Boolean, bundle: Bundle) {
        val intent = Intent(this, clazz)
        intent.putExtra(AppConstants.BUNDLE, bundle)
        if(isNewTask) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    fun startActivityForResult(clazz: Class<out Activity>, isNewTask:Boolean, bundle: Bundle, activityStartCode: Int) {
        val intent = Intent(this, clazz)
        intent.putExtra(AppConstants.BUNDLE, bundle)
        if(isNewTask) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivityForResult(intent, activityStartCode)
    }

    fun startActivity(clazz: Class<out Activity>, extra:Bundle, parcelName:String) {
        val intent = Intent(this, clazz)
        intent.putExtra(AppConstants.BUNDLE, extra)
        startActivity(intent)
    }

    override fun onBackPressed() {
        try {
            super.onBackPressed()
            if(this::class.simpleName != LoginRequiredActivity::class.simpleName &&
                    this::class.simpleName != LoginActivity::class.simpleName &&
                    this::class.simpleName != ChatActivity::class.simpleName &&
                    this::class.simpleName != UploadCategorySelectionActivity::class.simpleName) {
                Utility.slideActivityLeftToRight(this)
            } else {
                Utility.slideActivityTopToBottom(this)
            }
        } catch (nullPointerException: NullPointerException) {

        } finally {
            finish()
        }
    }

    private fun loadInterstitialAd() {
        mInterstitialAd?.loadAd(AdRequest.Builder().build())
    }

    private fun addInterstitialAdListener() {
        mInterstitialAd?.adListener  = object: AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                loadInterstitialAd()
            }

            override fun onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            override fun onAdClosed() {
                loadInterstitialAd()
            }
        }
    }

    private fun scheduleInterstitialAd() {
        val timer = Timer()
        val interstitialTask = object : TimerTask() {
            override fun run() {
                this@BylancerBuilderActivity.runOnUiThread() {
                    if (mInterstitialAd?.isLoaded && !this@BylancerBuilderActivity.isFinishing) {
                        mInterstitialAd?.show()
                    }
                }
            }
        }
        val delay = (1000 * 60 * AppConstants.INTERSTITIAL_DELAY)
        timer.schedule(interstitialTask, 0L, delay.toLong())
    }

    private fun isRTLSupportRequired() {
        if (AppConstants.DIRECTION_RTL.equals(SessionState.instance.selectedLanguageDirection)) {
            window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
        } else {
            window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
        }
    }
}

