package com.rmutt.classified.rubhew

import android.app.Application
import android.os.StrictMode
import com.rmutt.classified.rubhew.utils.SessionState
import com.crashlytics.android.Crashlytics
import com.facebook.FacebookSdk
import com.facebook.ads.AudienceNetworkAds
import com.google.android.gms.ads.MobileAds
//import com.google.android.gms.ads.MobileAds
import io.fabric.sdk.android.Fabric

class ProjectA : Application() {
    private val TAG = ProjectA::class.java!!.getSimpleName()

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        Fabric.with(this, Crashlytics())
        logUserToCrashlytics()
        FacebookSdk.sdkInitialize(applicationContext)
        MobileAds.initialize(this)
        // for exposed beyond app through ClipData.Item.getUri() issues
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        // Initialize the Audience Network SDK
        AudienceNetworkAds.initialize(this);
    }

    companion object {
        var mInstance: ProjectA? = null

        @Synchronized
        fun getInstance(): ProjectA {
            return mInstance!!
        }
    }

    private fun logUserToCrashlytics() {
        // TODO: Use the current user's information
        // You can call any combination of these three methods
        if (SessionState.instance.isLoggedIn) {
            Crashlytics.setUserIdentifier(SessionState.instance.userId)
            Crashlytics.setUserEmail(SessionState.instance.email)
            Crashlytics.setUserName(SessionState.instance.userName)
        }
    }

}
