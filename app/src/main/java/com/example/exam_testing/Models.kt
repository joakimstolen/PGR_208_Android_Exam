package com.example.exam_testing

class Places(val features : List<Feature>)

class Feature(val type : String, val properties: Properties, val geometry: Geometry)

class Geometry(val type : String, val coordinates : ArrayList<Double>)

class Properties(val name : String, val icon : String, val id : Long)


class FromPlaceId(val place : Place)

data class Place(val id: Long, val name: String, val comments: String, val banner: String, val lat: Double, val lon: Double)