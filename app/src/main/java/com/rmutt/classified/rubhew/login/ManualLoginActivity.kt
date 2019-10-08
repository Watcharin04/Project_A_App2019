package com.rmutt.classified.rubhew.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.rmutt.classified.rubhew.R
import com.rmutt.classified.rubhew.activities.BylancerBuilderActivity
import com.rmutt.classified.rubhew.dashboard.DashboardActivity
import com.rmutt.classified.rubhew.utils.*
import com.rmutt.classified.rubhew.webservices.RetrofitController
import com.rmutt.classified.rubhew.webservices.login.UserLoginData
import com.rmutt.classified.rubhew.webservices.login.UserLoginStatus
import kotlinx.android.synthetic.main.activity_manual_login.*
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ManualLoginActivity : BylancerBuilderActivity(), View.OnFocusChangeListener , TextWatcher, View.OnClickListener,
        Callback<UserLoginStatus> {
    var isPasswordShown = false

    override fun setLayoutView() = R.layout.activity_manual_login

    override fun initialize(savedInstanceState: Bundle?) {
        log_in_with_email_title_text_view.text = LanguagePack.getString(getString(R.string.login_with_email))
        login_email_id_edit_text.hint = LanguagePack.getString(getString(R.string.email_username))
        login_password_edit_text.hint = LanguagePack.getString(getString(R.string.password))
        show_hide_password_text_view.text = LanguagePack.getString(getString(R.string.show))
        login_forget_password_text_view.text = LanguagePack.getString(getString(R.string.forget_password))
        login_button.text = LanguagePack.getString(getString(R.string.log_in))
        login_email_id_edit_text.addTextChangedListener(this)
        login_password_edit_text.addTextChangedListener(this)
        login_password_edit_text.setOnFocusChangeListener(this)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(p0: Editable?) {
        login_button.isEnabled = Utility.hasText(login_email_id_edit_text) && Utility.hasText(login_password_edit_text)
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if(hasFocus) {
            login_password_frame.setBackgroundResource(R.drawable.rounded_edittext_focussed)
        } else {
            login_password_frame.setBackgroundResource(R.drawable.rounded_edittext_normal)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.show_hide_password_text_view -> {
                if (!isPasswordShown) {
                    show_hide_password_text_view.text = LanguagePack.getString(getString(R.string.hide))
                    login_password_edit_text.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
                } else {
                    show_hide_password_text_view.text = LanguagePack.getString(getString(R.string.show))
                    login_password_edit_text.setTransformationMethod(PasswordTransformationMethod.getInstance())
                }
                login_password_edit_text.setSelection(login_password_edit_text.text.length)
                isPasswordShown = !isPasswordShown
            }

            R.id.manual_login_screen_back -> onBackPressed()

            R.id.login_forget_password_text_view -> startActivity(ForgotPasswordActivity::class.java,false)

            R.id.login_button -> loginUser()
        }
    }

    fun loginUser() {
        Utility.hideKeyboard(this)
        login_sliding_progress_indicator.visibility = View.VISIBLE
        val userLoginData = UserLoginData()
        userLoginData.email = login_email_id_edit_text.text.toString()
        userLoginData.username = login_email_id_edit_text.text.toString()
        userLoginData.password = login_password_edit_text.text.toString()

        /*if (login_email_id_edit_text.text.contains("@")) {
            RetrofitController.loginUserUsingEmail(userLoginData, this)
        } else {*/
            RetrofitController.loginUserUsingUsername(userLoginData, this)
        //}
    }

    override fun onFailure(call: Call<UserLoginStatus>?, t: Throwable?) {
        login_sliding_progress_indicator.visibility = View.GONE
        Utility.showSnackBar(activity_login_user_parent_layout, "Please check your internet connection", this)
    }

    override fun onResponse(call: Call<UserLoginStatus>?, response: Response<UserLoginStatus>?) {
        if (response != null && response.isSuccessful) {
            val responseBody: UserLoginStatus = response.body()
            if (responseBody != null && AppConstants.SUCCESS.equals(responseBody.status!!)) {
                SessionState.instance.displayName = responseBody.name!!
                SessionState.instance.userName = responseBody.username!!
                SessionState.instance.email = responseBody.email!!
                SessionState.instance.userId = responseBody.userId!!
                SessionState.instance.profilePicUrl = if (responseBody.profilePicture != null) responseBody.profilePicture!! else ""

                SessionState.instance.saveValuesToPreferences(this, AppConstants.Companion.PREFERENCES.PROFILE_PIC.toString(), SessionState.instance.profilePicUrl)
                SessionState.instance.saveValuesToPreferences(this, AppConstants.Companion.PREFERENCES.USERNAME.toString(), responseBody.username!!)
                SessionState.instance.saveValuesToPreferences(this, AppConstants.Companion.PREFERENCES.EMAIL.toString(), responseBody.email!!)
                SessionState.instance.saveValuesToPreferences(this, AppConstants.Companion.PREFERENCES.DISPLAY_NAME.toString(), responseBody.name!!)
                SessionState.instance.saveValuesToPreferences(this, AppConstants.Companion.PREFERENCES.USER_ID.toString(), responseBody.userId!!)

                fetchLanguagePackDetails()
            } else {
                login_sliding_progress_indicator.visibility = View.GONE
                Utility.showSnackBar(activity_login_user_parent_layout, "Username/Email or Password not matched", this)
            }
        } else {
            login_sliding_progress_indicator.visibility = View.GONE
            Utility.showSnackBar(activity_login_user_parent_layout, "Something went wrong, we are working on it to fix it as soon as possible", this)
        }
    }

    private fun fetchLanguagePackDetails() {
        var mRequestQueue = Volley.newRequestQueue(this)

        //String Request initialized
        var mStringRequest = JsonArrayRequest(Request.Method.GET, AppConstants.BASE_URL + AppConstants.FETCH_LANGUAGE_PACK_URL, null, com.android.volley.Response.Listener<JSONArray> { response ->
            if (response != null) {
                LanguagePack.instance.saveLanguageData(this@ManualLoginActivity, response.toString())
                LanguagePack.instance.setLanguageData(response.toString())
                SessionState.instance.isLogin = true
                SessionState.instance.saveBooleanToPreferences(this, AppConstants.Companion.PREFERENCES.LOGIN_STATUS.toString(), true)
                moveToDashboard()
            } else {
                Utility.showSnackBar(activity_login_user_parent_layout, getString(R.string.internet_issue), this)
            }
        }, com.android.volley.Response.ErrorListener {
            if (Utility.isNetworkAvailable(this@ManualLoginActivity)) {
                fetchLanguagePackDetails()
            } else {
                Utility.showSnackBar(activity_login_user_parent_layout, getString(R.string.internet_issue), this)
            }
        })

        mRequestQueue.add(mStringRequest)
    }

    private fun moveToDashboard() {
        sendTokenToServer()
        Utility.removeProgressBar(login_sliding_progress_indicator)
        Utility.showSnackBar(activity_login_user_parent_layout, LanguagePack.getString("You have successfully logged in"), this)
        startActivity(DashboardActivity::class.java, true)
        finish()
    }
}
