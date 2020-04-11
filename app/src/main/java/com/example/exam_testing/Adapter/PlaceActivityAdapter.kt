package com.example.exam_testing.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exam_testing.Activity.PlacesActivity
import com.example.exam_testing.Data.FromPlaceId
import com.example.exam_testing.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.places_details_row.view.*

class PlaceActivityAdapter(val fromPlaceId: FromPlaceId): RecyclerView.Adapter<PlacesActivity.PlaceDetailViewHolder>(){

    override fun getItemCount(): Int {
        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacesActivity.PlaceDetailViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val customView = layoutInflater.inflate(R.layout.places_details_row, parent, false)


        println(fromPlaceId.place.name)


        return PlacesActivity.PlaceDetailViewHolder(
            customView,
            fromPlaceId
        )

    }

    override fun onBindViewHolder(holder: PlacesActivity.PlaceDetailViewHolder, position: Int) {


        val place = fromPlaceId.place
        var comments = place.comments

        comments = android.text.Html.fromHtml(comments).toString()

        holder.customView.textView_places_details_title.text = place.name
        holder.customView.textView_places_detail_comment.text = comments

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