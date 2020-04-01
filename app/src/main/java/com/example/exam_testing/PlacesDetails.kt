package com.example.exam_testing

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.exam_testing.databinding.PlacesDetailsRecyclerviewBinding
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.places_details_recyclerview.*
import kotlinx.android.synthetic.main.places_details_row.view.*
import kotlinx.android.synthetic.main.places_info_row.view.*
import okhttp3.*
import java.io.IOException

class PlacesDetails : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        recyclerview_main.layoutManager = LinearLayoutManager(this)





        //setting navbar title
        val navBarTitle = intent.getStringExtra(MainAdapter.CustomViewHolder.PLACE_TITLE_KEY)
        supportActionBar?.title = navBarTitle


        fetchJson()
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
                    recyclerview_main.adapter = PlaceDetailAdapter(fromPlaceId)

                }


            }

            override fun onFailure(call: Call, e: IOException) {

            }
        })
    }




    private class PlaceDetailAdapter(val fromPlaceId: FromPlaceId): RecyclerView.Adapter<PlaceDetailViewHolder>(){

        override fun getItemCount(): Int {
            return 1
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceDetailViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val customView = layoutInflater.inflate(R.layout.places_details_row, parent, false)




            return PlaceDetailViewHolder(customView, fromPlaceId)

        }

        override fun onBindViewHolder(holder: PlaceDetailViewHolder, position: Int) {

            println(fromPlaceId.place.comments)

            val place = fromPlaceId.place


            holder.customView.textView_places_details_title.text = place.name
            holder.customView.textView_places_detail_comment.text = place.comments

            val bannerImageView = holder.customView.imageView_places_detail_banner
            val bannerUrl = place.banner

            val defaultImageUrl = "https://i.imgur.com/JjEYAyS.jpg"

            if (bannerUrl.isEmpty()){
                Picasso.get().load(defaultImageUrl).into(bannerImageView)
            } else {
                Picasso.get().load(bannerUrl).into(bannerImageView)
            }


        }

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