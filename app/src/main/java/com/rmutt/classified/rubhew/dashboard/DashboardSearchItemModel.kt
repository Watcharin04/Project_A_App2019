package com.rmutt.classified.rubhew.dashboard

import ir.mirrajabi.searchdialog.core.Searchable


class DashboardSearchItemModel(private var mTitle: String?) : Searchable {

    override fun getTitle(): String? {
        return mTitle
    }

    fun setTitle(title: String): DashboardSearchItemModel {
        mTitle = title
        return this
    }
}