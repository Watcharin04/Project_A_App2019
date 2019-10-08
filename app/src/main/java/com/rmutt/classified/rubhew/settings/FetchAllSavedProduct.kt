package com.rmutt.classified.rubhew.settings

import com.rmutt.classified.rubhew.dashboard.DashboardDetailModel

interface FetchAllSavedProduct {
    fun onAllMyFavoriteProductsFetched(savedProductList: List<DashboardDetailModel>)
}