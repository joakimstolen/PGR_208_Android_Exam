package com.example.exam_testing

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [(PlaceEntity::class)], version = 1)
abstract class PlaceDatabase : RoomDatabase(){

    abstract fun placeDao(): PlaceDAO
}