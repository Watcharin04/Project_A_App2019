package com.rmutt.classified.rubhew.dashboard

import android.content.Context
import androidx.viewpager.widget.PagerAdapter
import androidx.appcompat.widget.AppCompatTextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rmutt.classified.rubhew.utils.Utility


class ProductDetailViewPagerAdapter(private val mContext: Context, private val images: List<String>, private val imagePath:String) : PagerAdapter() {

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {

        val inflater = LayoutInflater.from(mContext)
        var b: Boolean? = true
        val layout = inflater.inflate(com.rmutt.classified.rubhew.R.layout.product_detail_view_pager_adapter, collection, false) as ViewGroup
        val productImage = layout.findViewById(com.rmutt.classified.rubhew.R.id.product_image_view_pager) as ImageView
        val productImageCounter = layout.findViewById(com.rmutt.classified.rubhew.R.id.product_image_counter_text_view) as AppCompatTextView
        productImageCounter.text = ((position + 1).toString() + "/" + (images.size).toString())
        if (images[position].isNullOrEmpty()) {
            Glide.with(mContext).load(imagePath + images[position]).apply(RequestOptions().placeholder(com.rmutt.classified.rubhew.R.drawable.image_not_available)).into(productImage)
        } else {
            Glide.with(mContext).load(imagePath + images[position]).apply(RequestOptions().placeholder(Utility.getCircularProgressDrawable(mContext))).into(productImage)
        }

        productImage.setOnClickListener(View.OnClickListener {
            Toast.makeText(mContext, "Congratulations!!", Toast.LENGTH_SHORT).show()
            if (b == true) {
                // b was not null and equal true
                b = false
//                ImageViewer.Builder(mContext, images).show()
//                Fresco.initialize(this);
//                productImage.layoutParams(new Linear)
//                productImage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//                productImage.setAdjustViewBounds(true)

            }
        })

        collection.addView(layout)
        return layout
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return ""
    }

    override fun isViewFromObject(view: View, p1: Any): Boolean {
        return view === p1
    }

}