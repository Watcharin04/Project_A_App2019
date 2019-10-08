package com.rmutt.classified.rubhew.settings

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import android.view.View
import com.rmutt.classified.rubhew.R
import com.rmutt.classified.rubhew.activities.BylancerBuilderActivity
import com.rmutt.classified.rubhew.dashboard.DashboardDetailModel
import com.rmutt.classified.rubhew.dashboard.DashboardProductDetailActivity
import com.rmutt.classified.rubhew.dashboard.OnProductItemClickListener
import com.rmutt.classified.rubhew.database.MyFavouriteProductsGetterAsycTask
import com.rmutt.classified.rubhew.utils.AppConstants
import com.rmutt.classified.rubhew.utils.GridSpacingItemDecoration
import com.rmutt.classified.rubhew.utils.LanguagePack
import kotlinx.android.synthetic.main.activity_my_favorites.*

class MyFavoritesActivity : BylancerBuilderActivity(), OnProductItemClickListener, View.OnClickListener {
    val SPAN_COUNT = 2

    override fun setLayoutView() = R.layout.activity_my_favorites

    override fun initialize(savedInstanceState: Bundle?) {
        my_fav_title_text_view.text = LanguagePack.getString(getString(R.string.my_favorites))
        no_fav_added.text = LanguagePack.getString(getString(R.string.no_favorites))

        my_fav_recycler_view.layoutManager = GridLayoutManager(this, SPAN_COUNT)
        my_fav_recycler_view.setHasFixedSize(false)
        my_fav_recycler_view.isNestedScrollingEnabled = false
        my_fav_recycler_view.addItemDecoration(GridSpacingItemDecoration(SPAN_COUNT, 10, true))
    }

    override fun onResume() {
        super.onResume()
        MyFavouriteProductsGetterAsycTask(this, object: FetchAllSavedProduct {
            override fun onAllMyFavoriteProductsFetched(savedProductList: List<DashboardDetailModel>) {
                if (!savedProductList.isNullOrEmpty()) {
                    no_fav_frame.visibility = View.GONE
                    my_fav_recycler_view.visibility = View.VISIBLE
                    my_fav_recycler_view.adapter = MyFavoriteItemAdapter(savedProductList, this@MyFavoritesActivity)
                } else {
                    no_fav_frame.visibility = View.VISIBLE
                    my_fav_recycler_view.visibility = View.GONE
                }
            }
        }).execute()
    }

    override fun onProductItemClicked(productId: String?, productName: String?, userName: String?) {
        val bundle = Bundle()
        bundle.putString(AppConstants.PRODUCT_ID, productId)
        bundle.putString(AppConstants.PRODUCT_NAME, productName)
        bundle.putString(AppConstants.PRODUCT_OWNER_NAME, userName)
        startActivity(DashboardProductDetailActivity::class.java, false, bundle)
    }

    override fun onClick(view: View?) {
        if (view?.id == R.id.my_favorite_back_image_view) {
            onBackPressed()
        }
    }
}
