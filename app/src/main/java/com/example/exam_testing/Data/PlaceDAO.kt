package com.example.exam_testing.Data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface PlaceDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePlaces(placeEntity: PlaceEntity)

    @Query("SELECT * FROM placeentity")
    fun getAllPlaces(): List<PlaceEntity>
}