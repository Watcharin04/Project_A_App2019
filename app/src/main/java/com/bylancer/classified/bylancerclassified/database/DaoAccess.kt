package com.bylancer.classified.bylancerclassified.database

import android.arch.persistence.room.*
import com.bylancer.classified.bylancerclassified.dashboard.DashboardDetailModel

@Dao
public interface DaoAccess {

    @Insert
    fun insertProperty (property: DashboardDetailModel)

    @Query("SELECT COUNT(*) FROM DashboardDetailModel WHERE id = :propertyId")
    fun checkPropertyExist(propertyId: String) : Int

    @Query("SELECT * FROM DashboardDetailModel")
    fun fetchAllProperties(): List<DashboardDetailModel>

    @Update
    fun updateProperty(property: DashboardDetailModel)

    @Delete
    fun deleteProperty(property: DashboardDetailModel)
}