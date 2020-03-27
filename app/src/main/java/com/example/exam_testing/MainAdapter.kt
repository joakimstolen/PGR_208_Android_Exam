package com.example.exam_testing


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.places_info_row.view.*

class MainAdapter(val places: Places) : RecyclerView.Adapter<CustomViewHolder>() {


    //number of items showed
    override fun getItemCount(): Int {
        return places.features.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.places_info_row, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        //val placeTitles = placeTitles.get(position)
        val feature = places.features.get(position)


        holder.view.textView_place_name.text = feature.properties.name

        holder?.feature = feature

    }



}


class CustomViewHolder(val view: View, var feature: Feature? = null, var fromPlaceId: FromPlaceId? = null) : RecyclerView.ViewHolder(view) {

    companion object{
        val PLACE_TITLE_KEY = "PLACE_TITLE"
        val PLACE_ID_KEY = "PLACE_ID"
        val PLACE_LAT_KEY_MAIN = "PLACE_LAT_KEY"
        val BUTTON_MAIN_MAP = "PLACE_LON_KEY"
    }

    init {
        view.setOnClickListener {
            val intent = Intent(view.context, PlacesDetails::class.java)

            intent.putExtra(PLACE_TITLE_KEY, feature?.properties?.name)
            intent.putExtra(PLACE_ID_KEY, feature?.properties?.id)
            intent.putExtra(BUTTON_MAIN_MAP, R.id.button_places_map_main)

            view.context.startActivity(intent)
        }




    }

}



