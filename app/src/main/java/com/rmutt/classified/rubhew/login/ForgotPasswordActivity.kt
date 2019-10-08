package com.rmutt.classified.rubhew.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.rmutt.classified.rubhew.R
import com.rmutt.classified.rubhew.activities.BylancerBuilderActivity
import com.rmutt.classified.rubhew.utils.AppConstants
import com.rmutt.classified.rubhew.utils.Utility
import com.rmutt.classified.rubhew.webservices.RetrofitController
import com.rmutt.classified.rubhew.webservices.registration.UserForgetPasswordStatus
import kotlinx.android.synthetic.main.activity_forgot_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordActivity : BylancerBuilderActivity(), TextWatcher, View.OnClickListener,
        Callback<UserForgetPasswordStatus> {

    override fun setLayoutView() = R.layout.activity_forgot_password

    override fun initialize(savedInstanceState: Bundle?) {
        forgot_password_edit_text.addTextChangedListener(this)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(p0: Editable?) {
        forgot_password_button.isEnabled = Utility.hasText(forgot_password_edit_text)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.forgot_password_screen_back -> onBackPressed()

            R.id.forgot_password_button -> {
                if (Utility.isValidEmail(forgot_password_edit_text.text.toString())) {
                    resetPassword()
                } else {
                    Utility.showSnackBar(forget_password_screen_parent_layout, "Please enter correct Email id", this)
                }
            }
        }
    }

    private fun resetPassword() {
        Utility.hideKeyboard(this)
        forget_password_sliding_progress_indicator.visibility = View.VISIBLE
        RetrofitController.userForgetPassword(forgot_password_edit_text.text.toString(), this)
    }

    override fun onFailure(call: Call<UserForgetPasswordStatus>?, t: Throwable?) {
        forget_password_sliding_progress_indicator.visibility = View.GONE
        Utility.showSnackBar(forget_password_screen_parent_layout, "Please check your internet connection", this)
    }

    override fun onResponse(call: Call<UserForgetPasswordStatus>?, response: Response<UserForgetPasswordStatus>?) {
        forget_password_sliding_progress_indicator.visibility = View.GONE

        if (response != null && response.isSuccessful) {
            val responseBody: UserForgetPasswordStatus = response.body()
            if (responseBody != null && AppConstants.SUCCESS.equals(responseBody.status!!)) {
                val bundle = Bundle()
                bundle.putString(AppConstants.MESSAGE, responseBody.message!!)
                startActivity(LoginActivity::class.java, true, bundle)
                finish()
            } else if (responseBody != null) {
                Utility.showSnackBar(forget_password_screen_parent_layout, responseBody.message!!, this)
            } else {
                Utility.showSnackBar(forget_password_screen_parent_layout, "Something went wrong, we are working on it to fix it as soon as possible", this)
            }
        }
    }
}
