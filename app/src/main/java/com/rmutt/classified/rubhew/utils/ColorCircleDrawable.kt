package com.rmutt.classified.rubhew.utils

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.Drawable

class ColorCircleDrawable(color: Int) : Drawable() {
    private val mPaint: Paint
    private var mRadius = 0

    init {
        this.mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        this.mPaint.color = color
    }

    override fun draw(canvas: Canvas) {
        val bounds = bounds
        canvas.drawCircle(bounds.centerX().toFloat(), bounds.centerY().toFloat(), mRadius.toFloat(), mPaint)
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        mRadius = Math.min(bounds.width(), bounds.height()) / 2
    }

    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
    }

    override fun setColorFilter(cf: ColorFilter?) {
        mPaint.colorFilter = cf
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }
}