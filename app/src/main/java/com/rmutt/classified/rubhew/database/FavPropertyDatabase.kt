package com.rmutt.classified.rubhew.database

import androidx.room.RoomDatabase
import androidx.room.Database
import com.rmutt.classified.rubhew.dashboard.DashboardDetailModel
import com.rmutt.classified.rubhew.utils.AppConstants


@Database(entities = [DashboardDetailModel::class], version = AppConstants.DATABASE_VERSION, exportSchema = false)
abstract class FavPropertyDatabase : RoomDatabase() {
    abstract fun daoAccess(): DaoAccess
}