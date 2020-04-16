package com.example.exam_testing.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.exam_testing.Data.PlaceDatabase
import com.example.exam_testing.Data.PlaceEntity
import com.example.exam_testing.Data.Places
import com.example.exam_testing.R
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

class SplashScreenActivity : AppCompatActivity() {

    lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        fetchJson()

        handler = Handler()
        handler.postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 30000)


        Toast.makeText(this, "Loading data...", Toast.LENGTH_SHORT).show()


    }


    private fun fetchJson() {

        val db = Room.databaseBuilder(applicationContext, PlaceDatabase::class.java, "PlacesDatabaseReal.db").allowMainThreadQueries().build()


        val url = "https://www.noforeignland.com/home/api/v1/places/"

        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                println(body)

                val gson = GsonBuilder().create()
                val places = gson.fromJson(body, Places::class.java)


                if (db.placeDao().getAllPlaces().isEmpty()){
                    println("Storing data to local")


                    places.features.forEach {
                        val thread = Thread {
                            var placeEntity =
                                PlaceEntity()
                            placeEntity.id = it.properties.id
                            placeEntity.name = it.properties.name
                            placeEntity.lon = it.geometry.coordinates[0]
                            placeEntity.lat = it.geometry.coordinates[1]
                            db.placeDao().savePlaces(placeEntity)

                        }

                        thread.start()
                    }
                } else {
                    println("Data already stored in database")
                    handler.postDelayed({
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }, 1500)

                }

            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d(MainActivity.TAG, "Failed to execute request")
            }
        })
    }

}
