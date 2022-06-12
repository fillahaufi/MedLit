package com.fillahaufi.medlit.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.fillahaufi.medlit.data.local.Medicine

@Dao
interface MedDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(medicines: List<Medicine>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(medicine: Medicine)

    @Delete
    fun delete(medicine: Medicine)

    @Update
    fun update(medicine: Medicine)

    @Query("SELECT * FROM medicine where isBookmarked = 1")
    fun getBookmarkedUsers(): LiveData<List<Medicine>>

    @Query("SELECT EXISTS(SELECT * FROM medicine WHERE name = :name AND isBookmarked = 1)")
    fun isMedicineBookmarked(name: String): Boolean
}