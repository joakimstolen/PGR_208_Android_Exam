package com.example.exam_testing.Data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

class Places(val features : List<Feature>)

class Feature(val type : String, val properties: Properties, val geometry: Geometry)

class Geometry(val type : String, val coordinates : ArrayList<Double>)

class Properties(val name : String, val icon : String, val id : Long)


class FromPlaceId(val place : Place)


data class Place(val id: Long, val name: String, val comments: String, val banner: String, val lat: Double, val lon: Double)

@Entity(indices = [Index(value = ["placeName"], unique = true)])
class PlaceEntity{
    @PrimaryKey(autoGenerate = false)
    var id: Long = 0

    @ColumnInfo(name = "placeName")
    var name: String = ""

    @ColumnInfo(name = "comment")
    var comments: String = ""

    @ColumnInfo(name = "banner")
    var banner: String = ""

    @ColumnInfo(name = "lat")
    var lat: Double = 0.0

    @ColumnInfo(name = "lon")
    var lon: Double = 0.0

}

