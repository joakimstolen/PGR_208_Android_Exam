package com.example.exam_testing


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.places_info_row.view.*


class MainAdapter(val places: Places, private var placeListFull: MutableList<Feature> = mutableListOf()) : RecyclerView.Adapter<MainAdapter.CustomViewHolder?>(), Filterable {

    private var featureListToShow: MutableList<Feature> = mutableListOf()



    init {
        featureListToShow = placeListFull as MutableList<Feature>
    }



    //number of items showed
    override fun getItemCount(): Int {
        return featureListToShow.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.places_info_row, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        //val placeTitles = placeTitles.get(position)
        val feature = featureListToShow.get(position)

        holder.view.textView_place_name.text = feature.properties.name

        holder.feature = feature

    }

    override fun getFilter(): Filter {
        return placeFilter
    }

    private val placeFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults? {
            var aFilteredList: MutableList<Feature> = mutableListOf()
            if (constraint == null || constraint.isEmpty()) {
                aFilteredList = placeListFull as MutableList<Feature>

            } else {
                aFilteredList = placeListFull.filter {
                    it.properties.name.contains(
                        constraint.toString(),
                        ignoreCase = true
                    )
                } as MutableList<Feature>

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
                featureListToShow = it as MutableList<Feature>
            }
            notifyDataSetChanged()
            println(featureListToShow)
        }


    }


    class CustomViewHolder(
        val view: View,
        var feature: Feature? = null
    ) : RecyclerView.ViewHolder(view) {

        companion object {
            val PLACE_TITLE_KEY = "PLACE_TITLE"
            val PLACE_ID_KEY = "PLACE_ID"

        }

        init {

            //launch detailpage of each place
            view.setOnClickListener {
                val intent = Intent(view.context, PlacesDetails::class.java)

                intent.putExtra(PLACE_TITLE_KEY, feature?.properties?.name)
                intent.putExtra(PLACE_ID_KEY, feature?.properties?.id)


                view.context.startActivity(intent)
            }

            //launch map-location for each place
            view.button_launch_map.setOnClickListener {
                val intent = Intent(view.context, MapsActivity::class.java)


                intent.putExtra(PlacesDetails.PlaceDetailViewHolder.PLACE_LAT_KEY, feature?.geometry?.coordinates?.elementAt(1))
                intent.putExtra(PlacesDetails.PlaceDetailViewHolder.PLACE_LON_KEY, feature?.geometry?.coordinates?.elementAt(0))
                intent.putExtra(PlacesDetails.PlaceDetailViewHolder.PLACE_NAME_KEY, feature?.properties?.name)

                view.context.startActivity(intent)
            }
        }



    }
}



