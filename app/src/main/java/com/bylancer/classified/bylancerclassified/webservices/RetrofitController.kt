package com.bylancer.classified.bylancerclassified.webservices

import android.nfc.NfcAdapter
import com.bylancer.classified.bylancerclassified.dashboard.DashboardDetailModel
import com.bylancer.classified.bylancerclassified.login.AppConfigModel
import com.bylancer.classified.bylancerclassified.utils.AppConstants.Companion.BASE_URL
import com.bylancer.classified.bylancerclassified.webservices.chat.ChatMessageModel
import com.bylancer.classified.bylancerclassified.webservices.login.UserLoginData
import com.bylancer.classified.bylancerclassified.webservices.login.UserLoginStatus
import com.bylancer.classified.bylancerclassified.webservices.productlist.ProductInputData
import com.bylancer.classified.bylancerclassified.webservices.productlist.ProductsData
import com.bylancer.classified.bylancerclassified.webservices.registration.UserForgetPasswordStatus
import com.bylancer.classified.bylancerclassified.webservices.registration.UserRegistrationData
import com.bylancer.classified.bylancerclassified.webservices.registration.UserRegistrationStatus
import com.bylancer.classified.bylancerclassified.webservices.settings.CityListModel
import com.bylancer.classified.bylancerclassified.webservices.settings.CountryListModel
import com.bylancer.classified.bylancerclassified.webservices.settings.StateListModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitController {
    companion object {
        private val mRetrofit: Retrofit = getInstance()
        private val webserviceApi = mRetrofit.create<WebServiceApiInterface>(WebServiceApiInterface::class.java!!)

        fun getInstance() : Retrofit {
            if (mRetrofit == null) {
                val gson = GsonBuilder()
                        .setLenient()
                        .create()

                return Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()
            }

            return mRetrofit
        }

        fun registerUser(userData: UserRegistrationData, registerUserCallBack: Callback<UserRegistrationStatus>) {
            val call = webserviceApi.registerUser(userData.name!!, userData!!.email!!, userData!!.username!!, userData!!.password!!, userData!!.fbLogin!!)
            call.enqueue(registerUserCallBack)
        }

        fun loginUserUsingEmail(userData: UserLoginData, registerUserCallBack: Callback<UserLoginStatus>) {
            val call = webserviceApi.loginUserUsingEmail(userData.email!!, userData!!.password!!)
            call.enqueue(registerUserCallBack)
        }

        fun loginUserUsingUsername(userData: UserLoginData, registerUserCallBack: Callback<UserLoginStatus>) {
            val call = webserviceApi.loginUserUsingUsername(userData.username!!, userData!!.password!!)
            call.enqueue(registerUserCallBack)
        }

        fun userForgetPassword(email: String, registerUserCallBack: Callback<UserForgetPasswordStatus>) {
            val call = webserviceApi.forgetPassword(email)
            call.enqueue(registerUserCallBack)
        }

        fun fetchProducts(productInputData: ProductInputData, fetchProductsCallBack: Callback<List<ProductsData>>) {
            val call = webserviceApi.fetchProducts(productInputData.status, productInputData.countryCode, productInputData.pageNumber,
                    productInputData.limit)
            call.enqueue(fetchProductsCallBack)
        }

        fun fetchProductDetails(productId: String, fetchProductsDetailCallBack: Callback<DashboardDetailModel>) {
            val call = webserviceApi.fetchProductsDetails(productId)
            call.enqueue(fetchProductsDetailCallBack)
        }

        fun fetchCountryDetails(fetchCountriesDetailCallBack: Callback<List<CountryListModel>>) {
            val call = webserviceApi.fetchCountryDetails()
            call.enqueue(fetchCountriesDetailCallBack)
        }

        fun fetchStateDetails(countryId: String, fetchStateDetailCallBack: Callback<List<StateListModel>>) {
            val call = webserviceApi.fetchStateDetailsByCountry(countryId)
            call.enqueue(fetchStateDetailCallBack)
        }

        fun fetchCityDetails(stateId: String, fetchCityDetailCallBack: Callback<List<CityListModel>>) {
            val call = webserviceApi.fetchCityDetailsByState(stateId)
            call.enqueue(fetchCityDetailCallBack)
        }

        fun fetchAppConfig(appConfigModelCallBack: Callback<AppConfigModel>) {
            val call = webserviceApi.fetchAppConfiguration()
            call.enqueue(appConfigModelCallBack)
        }

        fun fetchChatMessages(userName:String, clientUserName: String, pageNo: String, fetchChatMessageCallback: Callback<List<ChatMessageModel>>) {
            val call = webserviceApi.fetchChatMessage(userName, clientUserName, pageNo)
            call.enqueue(fetchChatMessageCallback)
        }
    }
}