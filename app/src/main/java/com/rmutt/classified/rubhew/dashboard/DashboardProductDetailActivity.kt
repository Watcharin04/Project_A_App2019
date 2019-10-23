package com.rmutt.classified.rubhew.dashboard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.rmutt.classified.rubhew.R
import com.rmutt.classified.rubhew.activities.BylancerBuilderActivity
import com.rmutt.classified.rubhew.chat.ChatActivity
import com.rmutt.classified.rubhew.database.DatabaseTaskAsyc
import com.rmutt.classified.rubhew.login.LoginActivity
import com.rmutt.classified.rubhew.login.LoginRequiredActivity
import com.rmutt.classified.rubhew.utils.*
import com.rmutt.classified.rubhew.webservices.RetrofitController
import com.rmutt.classified.rubhew.webservices.makeanoffer.MakeAnOfferData
import com.rmutt.classified.rubhew.webservices.makeanoffer.MakeAnOfferStatus
import com.rmutt.classified.rubhew.widgets.CustomAlertDialog
import com.rmutt.classified.rubhew.widgets.htmlcontent.URLImageParser
import com.gmail.samehadar.iosdialog.IOSDialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.facebook.ads.*;
import com.google.android.gms.maps.model.MarkerOptions
import com.rmutt.classified.rubhew.webservices.chat.ChatSentStatus
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_dashboard_product_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.*

class DashboardProductDetailActivity: BylancerBuilderActivity(), Callback<DashboardDetailModel>, View.OnClickListener,
        OnMapReadyCallback {

    private var mCurrLocationMarker: Marker? = null
    private var mMap: GoogleMap? = null
    private var animUpDown: Animation? = null
    val MY_PERMISSIONS_REQUEST_LOCATION = 87
    private var phoneNumber: String? = null
    private var ownerEmail: String? = null
    var mDashboardDetailModel:DashboardDetailModel? = null
    var iosDialog: IOSDialog? = null
    private var facebookInterstitialAd : InterstitialAd? = null

    override fun setLayoutView() = R.layout.activity_dashboard_product_detail

    override fun initialize(savedInstanceState: Bundle?) {
        if (SessionState.instance.isLoggedIn) {
            login_required_product_detail_card_view.visibility = View.GONE
        } else {
            login_required_product_detail_card_view.visibility = View.VISIBLE
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission()
        }

        if (SessionState.instance.isFacebookInterstitialSupported) {
            loadFacebookInterstitialAd()
        }

        iosDialog = Utility.showProgressView(this@DashboardProductDetailActivity, LanguagePack.getString("Sending..."))
        product_detail_screen_sms_text_view.text = LanguagePack.getString(getString(R.string.sms))
        product_detail_screen_email_text_view.text = LanguagePack.getString(getString(R.string.email_id))
        product_detail_screen_call_text_view.text = LanguagePack.getString(getString(R.string.call))
        product_detail_screen_chat_text_view.text = LanguagePack.getString(getString(R.string.chat))
        make_an_offer_text_view.text = LanguagePack.getString(getString(R.string.make_an_offer))
        login_to_know_more_text_View.text = LanguagePack.getString(getString(R.string.login_to_know_more))
        product_detail_age.text = LanguagePack.getString(getString(R.string.age))
        start_login_screen_button.text = LanguagePack.getString(getString(R.string.login))
        product_detail_posted_by.text = LanguagePack.getString(getString(R.string.posted_by))
        product_detail_phone_number.text = LanguagePack.getString(getString(R.string.phone_number))
        product_detail_product_status.text = LanguagePack.getString(getString(R.string.status))
        product_detail_description.text = LanguagePack.getString(getString(R.string.description))
        product_detail_location.text = LanguagePack.getString(getString(R.string.location))

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.product_detail_product_map) as SupportMapFragment?

        mapFragment!!.getMapAsync(this)

        val bundle = intent.getBundleExtra(AppConstants.BUNDLE)
        if (bundle != null) {
            product_detail_title_text_view.text = bundle.getString(AppConstants.PRODUCT_NAME, "")
            getProductDetails(bundle.getString(AppConstants.PRODUCT_ID, ""))
        }
    }

    private fun loadFacebookInterstitialAd() {
        facebookInterstitialAd = InterstitialAd(this, AppConstants.FACEBOOK_INTERSTITIAL_PLACEMENT)
        facebookInterstitialAd?.loadAd()
        showFacebookAdWithDelay()
    }

    private fun getProductDetails(productId: String) {
        parent_scroll_view.visibility = View.GONE
        progress_view_dashboard_detail_frame.visibility = View.VISIBLE
        animUpDown = Utility.animateProgressView(this, progress_view_dashboard_detail)
        RetrofitController.fetchProductDetails(productId, this)
    }

    override fun onResponse(call: Call<DashboardDetailModel>?, response: Response<DashboardDetailModel>?) {
        if (!this.isFinishing) {
            progress_view_dashboard_detail_frame.visibility = View.GONE
            progress_view_dashboard_detail.clearAnimation()
            animUpDown = null

            if(response != null && response.isSuccessful) {
                mDashboardDetailModel =  response.body()
                if (mDashboardDetailModel != null) {
                    initializeUI(mDashboardDetailModel!!)
                } else {
                    Utility.showSnackBar(dashboard_product_detail_parent_layout, LanguagePack.getString(getString(R.string.some_wrong)), this@DashboardProductDetailActivity)
                }
            }
        }
    }

    override fun onFailure(call: Call<DashboardDetailModel>?, t: Throwable?) {
        if (!this.isFinishing) {
            progress_view_dashboard_detail_frame.visibility = View.GONE
            progress_view_dashboard_detail.visibility = View.GONE
            progress_view_dashboard_detail.clearAnimation()
            animUpDown = null
            Utility.showSnackBar(dashboard_product_detail_parent_layout, LanguagePack.getString(getString(R.string.internet_issue)), this@DashboardProductDetailActivity)
        }
    }

    private fun initializeUI(dashboardDetailModel: DashboardDetailModel) {
        parent_scroll_view.visibility = View.VISIBLE
        if(dashboardDetailModel.productImages != null && dashboardDetailModel.productImages!!.size > 0 && dashboardDetailModel.originalImagesPath != null) {
            product_detail_image_view_pager.adapter = ProductDetailViewPagerAdapter(this, dashboardDetailModel.productImages!!, dashboardDetailModel.originalImagesPath!!)

        }

        if(SessionState.instance.isLoggedIn) {
            post_login_product_detail_layout.visibility = View.VISIBLE
        }

        product_name_in_detail.text = dashboardDetailModel.title
        var symbol = ""
        if (dashboardDetailModel.currency != null) {
            symbol = Utility.decodeUnicode(dashboardDetailModel.currency!!)
        }
        if (AppConstants.CURRENCY_IN_LEFT.equals(dashboardDetailModel.currencyInLeft)) {
            product_detail_price_text_view.text = symbol + dashboardDetailModel.price
        } else {

            var a: Int? = dashboardDetailModel.price?.toInt()
            var price2: String? = String.format("%,d", a);
//            product_detail_price_text_view.text = NumberFormat.getNumberInstance(Locale.US).format(price)
            product_detail_price_text_view.setText(price2+symbol)
//            product_detail_price_text_view.text = dashboardDetailModel.price + symbol
        }

        if (!dashboardDetailModel.price.isNullOrEmpty() && checkIfNumber(dashboardDetailModel.price!!)) {
            product_detail_price_text_view.visibility = View.VISIBLE
        } else {
            product_detail_price_text_view.visibility = View.INVISIBLE
        }

        product_detail_timeline_text_view.text = dashboardDetailModel.createdAt
        product_details_category_text_view.text = dashboardDetailModel.categoryName
        product_detail_age_desc.text = dashboardDetailModel.updatedAt

        product_detail_description_detail.linksClickable = true
        product_detail_description_detail.movementMethod = LinkMovementMethod.getInstance()
        val urlImageParser = URLImageParser(product_detail_description_detail, this)
        val descriptionHtmlSpan = getDescription(dashboardDetailModel.description, urlImageParser)
        product_detail_description_detail.text = descriptionHtmlSpan

        product_detail_product_status_desc.text = dashboardDetailModel.status
        product_detail_phone_number_desc.text = if(AppConstants.HIDE_PHONE.equals(dashboardDetailModel.hidePhone)) dashboardDetailModel.phone else getString(R.string.hidden)
        product_detail_posted_by_desc.text = dashboardDetailModel.sellerName
        phoneNumber = dashboardDetailModel.phone
        ownerEmail = dashboardDetailModel.sellerEmail
        product_detail_location_detail.text = dashboardDetailModel.country + " > " + dashboardDetailModel.state + " > " + dashboardDetailModel.city

        DatabaseTaskAsyc(this@DashboardProductDetailActivity,
                mDashboardDetailModel!!, product_detail_favorite_image_view, dashboard_product_detail_parent_layout, true).execute()

        var parentLayoutIdToMatchConstraint = product_detail_product_status_separator.id
        if(dashboardDetailModel.customData != null) {
            for(i in 0..(dashboardDetailModel.customData!!.size - 1)) {
                val customDataElement = dashboardDetailModel.customData!![i]
                if (customDataElement != null && customDataElement.title != null && customDataElement.value != null) {
                    parentLayoutIdToMatchConstraint = addCustomDataDynamically(parentLayoutIdToMatchConstraint, customDataElement.title!!, customDataElement.value!!)
                }
            }
        }

        if(mMap != null) {
            if (!dashboardDetailModel.mapLatitude.isNullOrEmpty() && !dashboardDetailModel.mapLongitude.isNullOrEmpty()) {
                val latLng = LatLng((dashboardDetailModel.mapLatitude)!!.toDouble(), (dashboardDetailModel.mapLongitude)!!.toDouble())
                val markerOptions = MarkerOptions()
                markerOptions.position(latLng)
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                mCurrLocationMarker = mMap?.addMarker(markerOptions)
                mMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                mMap?.animateCamera(CameraUpdateFactory.zoomTo(15.0F))
            }
        }
    }

    private fun getDescription(description: String?, urlImageParser: URLImageParser): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(description, Html.FROM_HTML_SEPARATOR_LINE_BREAK_HEADING, urlImageParser, null)
        } else {
            Html.fromHtml(description, urlImageParser, null)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap?.setMapType(GoogleMap.MAP_TYPE_NORMAL)
        mMap?.getUiSettings()?.setZoomControlsEnabled(true)
        mMap?.getUiSettings()?.setZoomGesturesEnabled(true)
        mMap?.getUiSettings()?.setCompassEnabled(true)
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap?.setMyLocationEnabled(true)
            }
        } else {
            mMap?.setMyLocationEnabled(true)
        }
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.product_detail_back_image_view -> onBackPressed()
            R.id.make_an_offer_text_view -> {
                if (SessionState.instance.isLoggedIn) {
                    if(mDashboardDetailModel != null && product_detail_price_text_view != null) {
                        if (!SessionState.instance.email.equals(mDashboardDetailModel?.sellerEmail)) {
                            makeAnOffer(product_detail_price_text_view.text.toString())
                        } else {
                            Utility.showSnackBar(dashboard_product_detail_parent_layout, getString(R.string.posted_by_you), this@DashboardProductDetailActivity)
                        }
                    }
                } else {
                    startActivity(LoginRequiredActivity::class.java, false)
                }
            }
            R.id.product_detail_warning_image_view -> {
                if (SessionState.instance.isLoggedIn) {

                } else {
                    startActivity(LoginRequiredActivity::class.java, false)
                }
            }
            R.id.product_detail_share_image_view -> {
                if(mDashboardDetailModel != null && mDashboardDetailModel?.productUrl != null) {
                    Utility.shareProduct(mDashboardDetailModel!!.productUrl!!, getString(R.string.app_name), this)
                }
            }
            R.id.product_detail_favorite_image_view -> {
                if (SessionState.instance.isLoggedIn) {
                    if (mDashboardDetailModel != null) {
                        DatabaseTaskAsyc(this@DashboardProductDetailActivity,
                                mDashboardDetailModel!!, product_detail_favorite_image_view, dashboard_product_detail_parent_layout, false).execute()
                    }
                } else {
                    startActivity(LoginRequiredActivity::class.java, false)
                }
            }
            R.id.start_login_screen_button -> {
                if (SessionState.instance.isLoggedIn) {

                } else {
                    startActivity(LoginActivity::class.java, false)
                }
            }
            R.id.product_detail_screen_chat -> {
                if (SessionState.instance.isLoggedIn) {
                    if (mDashboardDetailModel != null && mDashboardDetailModel!!.sellerUsername != null) {
                        if (!SessionState.instance.email.equals(mDashboardDetailModel!!.sellerEmail)) {
                            val bundle = Bundle()
                            bundle.putString(AppConstants.CHAT_TITLE, mDashboardDetailModel!!.sellerName)
                            bundle.putString(AppConstants.CHAT_USER_NAME, mDashboardDetailModel!!.sellerUsername)
                            bundle.putString(AppConstants.CHAT_USER_IMAGE, mDashboardDetailModel!!.sellerImage)
                            bundle.putString(AppConstants.CHAT_USER_ID, mDashboardDetailModel!!.sellerId)
                            startActivity(ChatActivity::class.java, false, bundle)
                        } else {
                            Utility.showSnackBar(dashboard_product_detail_parent_layout, getString(R.string.posted_by_you), this@DashboardProductDetailActivity)
                        }
                    } else {
                        Utility.showSnackBar(dashboard_product_detail_parent_layout, getString(R.string.some_wrong), this@DashboardProductDetailActivity)
                    }
                } else {
                    startActivity(LoginRequiredActivity::class.java, false)
                }
            }
            R.id.product_detail_screen_call -> {
                if (SessionState.instance.isLoggedIn) {
                    if(!phoneNumber.isNullOrEmpty() && checkLocationPermission()) {
                        val intent = Intent(Intent.ACTION_CALL)
                        intent.data = Uri.parse("tel:" + phoneNumber)
                        startActivity(intent)
                    }  else if (phoneNumber.isNullOrEmpty()){
                        Utility.showSnackBar(dashboard_product_detail_parent_layout, getString(R.string.phone_number_undefined), this@DashboardProductDetailActivity)
                    }
                } else {
                    startActivity(LoginRequiredActivity::class.java, false)
                }
            }
            R.id.product_detail_screen_sms -> {
                if (SessionState.instance.isLoggedIn) {
                    if (!phoneNumber.isNullOrEmpty()) {
                        startActivityForResult(Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", phoneNumber, null)), 0)
                    } else {
                        Utility.showSnackBar(dashboard_product_detail_parent_layout, getString(R.string.phone_number_undefined), this@DashboardProductDetailActivity)
                    }
                } else {
                    startActivity(LoginRequiredActivity::class.java, false)
                }
            }
            R.id.product_detail_screen_email -> {
                if (SessionState.instance.isLoggedIn) {
                    if (ownerEmail != null) {
                        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto", ownerEmail, null));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Interested in "+ product_detail_title_text_view.text);
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi, \n \n"+ LanguagePack.getString("I am interested in your property") + " " + product_detail_title_text_view.text + ".\n "+ LanguagePack.getString("We can have discussion on") +" \n\n" + LanguagePack.getString("Regards")+",\n"+ SessionState.instance.displayName)
                        startActivityForResult(Intent.createChooser(emailIntent,  LanguagePack.getString("Send email...")), 0);
                    } else {
                        Utility.showSnackBar(dashboard_product_detail_parent_layout, LanguagePack.getString("Looks like owner has not shared his email productId"), this)
                    }
                } else {
                    startActivity(LoginRequiredActivity::class.java, false)
                }
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mMap?.isMyLocationEnabled = true
                    }
                } else {
                    Toast.makeText(this, "permission denied",
                            Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    private fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                        MY_PERMISSIONS_REQUEST_LOCATION)
            } else {
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                        MY_PERMISSIONS_REQUEST_LOCATION)
            }
            return false
        } else {
            return true
        }
    }

    private fun addCustomDataDynamically(parentLayoutId: Int, captionText: String, detailText: String): Int {
        val set = ConstraintSet()

        val captionTextView = TextView(this)
        captionTextView.id = View.generateViewId()
        captionTextView.text = LanguagePack.getString(captionText)
        captionTextView.setTextColor(resources.getColor(R.color.disable_icon_color))
        captionTextView.typeface = getDefaultFont()
        captionTextView.setPadding(Utility.valueInDp(10, this), Utility.valueInDp(10, this), Utility.valueInDp(10, this), Utility.valueInDp(10, this));
        post_login_product_detail_layout.addView(captionTextView, 0)
        set.clone(post_login_product_detail_layout)
        set.connect(captionTextView.id, ConstraintSet.LEFT,
                post_login_product_detail_layout.id, ConstraintSet.LEFT, 15)
        set.connect(captionTextView.id, ConstraintSet.TOP,
                parentLayoutId, ConstraintSet.BOTTOM, 10)
        set.applyTo(post_login_product_detail_layout)

        val detailTextView = TextView(this)
        detailTextView.typeface = getDefaultFont()
        detailTextView.id = View.generateViewId()
        detailTextView.layoutParams = ViewGroup.LayoutParams(
                0,
                ViewGroup.LayoutParams.MATCH_PARENT)
        detailTextView.text = detailText
        detailTextView.gravity = Gravity.RIGHT
        detailTextView.setTextColor(resources.getColor(R.color.shadow_black))
        detailTextView.setPadding(Utility.valueInDp(15, this), Utility.valueInDp(10, this), Utility.valueInDp(10, this), Utility.valueInDp(10, this));
        post_login_product_detail_layout.addView(detailTextView, 0)
        set.clone(post_login_product_detail_layout)
        set.connect(detailTextView.id, ConstraintSet.RIGHT,
                post_login_product_detail_layout.id, ConstraintSet.RIGHT, 15)
        set.connect(detailTextView.id, ConstraintSet.TOP,
                parentLayoutId, ConstraintSet.BOTTOM, 10)
        set.connect(detailTextView.id, ConstraintSet.LEFT,
                captionTextView.id, ConstraintSet.RIGHT, 10)
        set.setHorizontalBias(detailTextView.id, 1f);
        set.applyTo(post_login_product_detail_layout)

        val separatorView = View(this)
        separatorView.id = View.generateViewId()
        separatorView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                Utility.valueInDp(1, this))
        separatorView.setBackgroundColor(resources.getColor(R.color.button_disabled_color))
        post_login_product_detail_layout.addView(separatorView, 0)
        set.clone(post_login_product_detail_layout)
        set.connect(separatorView.id, ConstraintSet.LEFT,
                post_login_product_detail_layout.id, ConstraintSet.LEFT, 0)
        set.connect(separatorView.id, ConstraintSet.RIGHT,
                post_login_product_detail_layout.id, ConstraintSet.RIGHT, 0)
        set.connect(separatorView.id, ConstraintSet.TOP,
                detailTextView.id, ConstraintSet.BOTTOM, 10)
        set.applyTo(post_login_product_detail_layout)

        return separatorView.id
    }

    private fun makeAnOffer(askingPrice: String) {
        val makeAnOfferDialog = CustomAlertDialog(this, R.style.custom_login_dialog)
        makeAnOfferDialog.setContentView(R.layout.make_an_offer)
        makeAnOfferDialog.window?.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        makeAnOfferDialog.window?.setBackgroundDrawable(ColorDrawable(resources.getColor(android.R.color.transparent)))
        makeAnOfferDialog.setCanceledOnTouchOutside(true)
        makeAnOfferDialog.show()

        val dialogTitle = makeAnOfferDialog.findViewById(R.id.make_an_offer_title_text_view) as AppCompatTextView
        val dialogSubTitle = makeAnOfferDialog.findViewById(R.id.make_an_offer_title_sub_text_view) as AppCompatTextView
        val dialogEditText = makeAnOfferDialog.findViewById(R.id.user_bid_edit_text) as AppCompatEditText
        val dialogConfirmButton = makeAnOfferDialog.findViewById(R.id.bid_confirm_button) as AppCompatButton
        val dialogCancelButton = makeAnOfferDialog.findViewById(R.id.bid_cancel_button) as AppCompatButton

        dialogTitle.text = LanguagePack.getString("Make an Offer")
        dialogSubTitle.text = LanguagePack.getString("Seller asking price is") + " \""+ askingPrice +"\""
       // dialogSubTitle.text = String.format(LanguagePack.getString("Seller asking price is \" %s \""), askingPrice)
        dialogConfirmButton.text = LanguagePack.getString("Confirm")
        dialogCancelButton.text = LanguagePack.getString("Cancel")

        dialogConfirmButton.setOnClickListener() {
            //Utility.hideKeyboardFromDialogs(this@DashboardProductDetailActivity)
            if (dialogEditText.text.isNullOrEmpty()) {
                return@setOnClickListener
            }
            makeAnOfferDialog.dismiss()
            iosDialog?.show()
            sendMakeAnOfferRequest(dialogEditText.text.toString())
        }
        dialogCancelButton.setOnClickListener() {
           // Utility.hideKeyboardFromDialogs(this@DashboardProductDetailActivity)
            makeAnOfferDialog.dismiss()
        }

    }

    private fun sendMakeAnOfferRequest(offer : String) {
        if (mDashboardDetailModel != null) {
            val makeAnOfferData = MakeAnOfferData()
            makeAnOfferData.email = getFormattedValue(mDashboardDetailModel?.sellerEmail)
            makeAnOfferData.message = SessionState.instance.displayName + " " + LanguagePack.getString("is interested to buy") + " ${getFormattedValue(mDashboardDetailModel?.title)} " + LanguagePack.getString("at") + " " + Utility.decodeUnicode(getFormattedValue(mDashboardDetailModel?.currency))+ " $offer "
            makeAnOfferData.ownerName = getFormattedValue(mDashboardDetailModel?.sellerName)
            makeAnOfferData.productId = getFormattedValue(mDashboardDetailModel?.productId)
            makeAnOfferData.productName = getFormattedValue(mDashboardDetailModel?.title)
            makeAnOfferData.userId = getFormattedValue(mDashboardDetailModel?.sellerId)
            makeAnOfferData.type = "make_offer"
            makeAnOfferData.senderId = SessionState.instance.userId
            makeAnOfferData.senderName = SessionState.instance.displayName
            makeAnOfferData.subject = LanguagePack.getString("Offer from") + " " + getString(R.string.app_name)
            RetrofitController.makeAnOffer(makeAnOfferData,object: Callback<MakeAnOfferStatus> {
                override fun onFailure(call: Call<MakeAnOfferStatus>?, t: Throwable?) {
                    removeProgressBar()
                    Utility.showSnackBar(dashboard_product_detail_parent_layout, LanguagePack.getString(getString(R.string.internet_issue)), this@DashboardProductDetailActivity)
                }

                override fun onResponse(call: Call<MakeAnOfferStatus>?, response: Response<MakeAnOfferStatus>?) {
                    removeProgressBar()
                    Utility.showSnackBar(dashboard_product_detail_parent_layout, LanguagePack.getString(getString(R.string.offer_submitted)), this@DashboardProductDetailActivity)
                }

            })

        }
    }

    private fun getFormattedValue(value : String?) : String {
        var formattedValue = value
        if (formattedValue == null) {
            formattedValue = ""
        }
        return formattedValue
    }

    private fun removeProgressBar() {
        if (iosDialog != null) iosDialog?.dismiss()
    }

    private fun showFacebookAdWithDelay() {
        Handler().postDelayed( Runnable() {
                // Check if interstitialAd has been loaded successfully
             if(!this@DashboardProductDetailActivity.isFinishing &&
                        facebookInterstitialAd != null && facebookInterstitialAd?.isAdLoaded!!
                        && !facebookInterstitialAd?.isAdInvalidated!!) {
                    facebookInterstitialAd?.show()
                } else {
                    showFacebookAdWithDelay()
                }
        }, (1000 * 5)) // Show the ad after 5 sec
    }

    override fun onDestroy() {
        if (facebookInterstitialAd != null) {
            facebookInterstitialAd?.destroy();
        }
        super.onDestroy();
    }
}