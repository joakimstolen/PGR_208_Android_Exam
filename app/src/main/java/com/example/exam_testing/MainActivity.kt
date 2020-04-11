package com.example.exam_testing

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException



class MainActivity : AppCompatActivity() {

    private var adapter: MainAdapter? = null


    companion object{
        val TAG = "MainActivity"

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Toast.makeText(this, "Welcome", Toast.LENGTH_LONG).show()

        recyclerview_main.layoutManager = LinearLayoutManager(this)


        fetchJson()
    }





    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.places_menu, menu)

        val searchItem: MenuItem = menu.findItem(R.id.places_search)
        val searchView: SearchView = searchItem.actionView as SearchView

        searchView.imeOptions = EditorInfo.IME_ACTION_DONE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter?.filter?.filter(newText)
                return false
            }

        })
        return true
    }

    private fun fetchJson() {

        val db = Room.databaseBuilder(applicationContext, PlaceDatabase::class.java, "PlacesDatabaseReal.db").build()

        val url = "https://www.noforeignland.com/home/api/v1/places/"

        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback{

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                println(body)

                val gson = GsonBuilder().create()
                val places = gson.fromJson(body, Places::class.java)


                if (db.placeDao().getAllPlaces().isEmpty()){
                    println("Storing data to local")

                    for (position in places.features.indices){
                        val feature = places.features.get(position)
                        val thread = Thread {
                            var placeEntity = PlaceEntity()
                            placeEntity.id = feature.properties.id
                            placeEntity.name = feature.properties.name
                            placeEntity.lon = feature.geometry.coordinates[0]
                            placeEntity.lat = feature.geometry.coordinates[1]
                            db.placeDao().savePlaces(placeEntity)

                        }
                        thread.start()

                    }
                }


                runOnUiThread{
                    adapter = MainAdapter(places, places.features as MutableList<Feature>)
                    recyclerview_main.adapter = adapter
                    adapter!!.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "Failed to execute request")
            }
        })
    }
}
