package com.example.exam_testing.Activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.exam_testing.Adapter.MainAdapter
import com.example.exam_testing.Adapter.PlaceActivityAdapter
import com.example.exam_testing.Data.FromPlaceId
import com.example.exam_testing.R
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.places_details_row.view.*
import okhttp3.*
import java.io.IOException

class PlacesActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        recyclerview_main.layoutManager = LinearLayoutManager(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //setting navbar title
        val navBarTitle = intent.getStringExtra(MainAdapter.CustomViewHolder.PLACE_TITLE_KEY)
        supportActionBar?.title = navBarTitle


        fetchJson()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)

    }



    private fun fetchJson() {

        val placeId = intent.getLongExtra(MainAdapter.CustomViewHolder.PLACE_ID_KEY, -1)
        val placeDetailUrl = "https://www.noforeignland.com/home/api/v1/place?id=" + placeId

        val client = OkHttpClient()
        val request = Request.Builder().url(placeDetailUrl).build()
        client.newCall(request).enqueue(object: Callback {

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()

                val gson = GsonBuilder().create()
                val fromPlaceId = gson.fromJson(body, FromPlaceId::class.java)

                runOnUiThread {
                    recyclerview_main.adapter =
                        PlaceActivityAdapter(
                            fromPlaceId
                        )


                }


            }

            override fun onFailure(call: Call, e: IOException) {

            }
        })
    }






    class PlaceDetailViewHolder(val customView: View, var fromPlaceId: FromPlaceId? = null): RecyclerView.ViewHolder(customView) {

        companion object {
            val PLACE_LAT_KEY = "PLACE_LAT_KEY"
            val PLACE_LON_KEY = "PLACE_LON_KEY"
            val PLACE_NAME_KEY = "PLACE_NAME_KEY"



        }


        init {


            customView.button_places_detail_showmap.setOnClickListener {
                println("Attempt to load map")

                val intent = Intent(customView.context, MapsActivity::class.java)

                intent.putExtra(PLACE_LAT_KEY, fromPlaceId?.place?.lat)
                intent.putExtra(PLACE_LON_KEY, fromPlaceId?.place?.lon)
                intent.putExtra(PLACE_NAME_KEY, fromPlaceId?.place?.name)

                customView.context.startActivity(intent)

            }


        }



    }
}