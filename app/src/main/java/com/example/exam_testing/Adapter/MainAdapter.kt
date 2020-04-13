package com.example.exam_testing.Adapter


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.exam_testing.Activity.MapsActivity
import com.example.exam_testing.Activity.PlacesActivity
import com.example.exam_testing.Data.PlaceEntity
import com.example.exam_testing.Data.Places
import com.example.exam_testing.R
import kotlinx.android.synthetic.main.places_info_row.view.*


class MainAdapter(val places: Places, private var placeListFull: MutableList<PlaceEntity> = mutableListOf()) : RecyclerView.Adapter<MainAdapter.CustomViewHolder?>(), Filterable {

    private var placeEntityListToShow: MutableList<PlaceEntity> = mutableListOf()



    init {
        placeEntityListToShow = placeListFull as MutableList<PlaceEntity>
    }





    //number of items showed
    override fun getItemCount(): Int {
        return placeEntityListToShow.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.places_info_row, parent, false)
        return CustomViewHolder(
            cellForRow
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        //val placeTitles = placeTitles.get(position)
        val placeEntity = placeEntityListToShow.get(position)

        holder.view.textView_place_name.text = placeEntity.name

        holder.placeEntity = placeEntity

    }

    override fun getFilter(): Filter {
        return placeFilter
    }

    private val placeFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults? {
            var aFilteredList: MutableList<PlaceEntity> = mutableListOf()
            if (constraint == null || constraint.isEmpty()) {
                aFilteredList = placeListFull as MutableList<PlaceEntity>

            } else {
                aFilteredList = placeListFull.filter {
                    it.name.contains(
                        constraint.toString(),
                        ignoreCase = true
                    )
                } as MutableList<PlaceEntity>

                println(aFilteredList)

            }

            println(aFilteredList)
            val result = FilterResults()
            result.values = aFilteredList
            return result
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

            results?.values.let {
                placeEntityListToShow = it as MutableList<PlaceEntity>
            }
            notifyDataSetChanged()
            println(placeEntityListToShow)
        }


    }


    class CustomViewHolder(
        val view: View,
        var placeEntity: PlaceEntity? = null
    ) : RecyclerView.ViewHolder(view) {

        companion object {
            val PLACE_TITLE_KEY = "PLACE_TITLE"
            val PLACE_ID_KEY = "PLACE_ID"

        }

        init {

            //launch detailpage of each place
            view.setOnClickListener {
                val intent = Intent(view.context, PlacesActivity::class.java)

                intent.putExtra(PLACE_TITLE_KEY, placeEntity?.name)
                intent.putExtra(PLACE_ID_KEY, placeEntity?.id)


                view.context.startActivity(intent)
            }

            //launch map-location for each place
            view.button_launch_map.setOnClickListener {
                val intent = Intent(view.context, MapsActivity::class.java)


                intent.putExtra(PlacesActivity.PlaceDetailViewHolder.PLACE_LAT_KEY, placeEntity?.lat)
                intent.putExtra(PlacesActivity.PlaceDetailViewHolder.PLACE_LON_KEY, placeEntity?.lon)
                intent.putExtra(PlacesActivity.PlaceDetailViewHolder.PLACE_NAME_KEY, placeEntity?.name)

                view.context.startActivity(intent)
            }
        }



    }
}



