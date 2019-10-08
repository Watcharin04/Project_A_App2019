package com.rmutt.classified.rubhew.widgets

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.Window


class BylancerProgressBar {

    companion object {
        var mDialog = null

        fun showDialog(context: Context) {
            var dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            //dialog.setContentView(R.layout.dialog_login)
            dialog.getWindow().setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
           // mDialog = dialog
        }
    }
}