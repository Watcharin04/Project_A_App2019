package com.rmutt.classified.rubhew.database

import android.content.Context
import android.os.AsyncTask
import com.rmutt.classified.rubhew.dashboard.DashboardDetailModel
import com.rmutt.classified.rubhew.settings.FetchAllSavedProduct

class MyFavouriteProductsGetterAsycTask(val context: Context, private val fetchAllSavedProduct: FetchAllSavedProduct): AsyncTask<Void, Void, List<DashboardDetailModel>>() {

    override fun doInBackground(vararg p0: Void): List<DashboardDetailModel> {
        return DBUtil.getDatabaseInstance(context).daoAccess().
                    fetchAllProperties()
    }

    override fun onPostExecute(result: List<DashboardDetailModel>) {
        super.onPostExecute(result)
        if (fetchAllSavedProduct != null) {
            fetchAllSavedProduct.onAllMyFavoriteProductsFetched(result)
        }
    }

}