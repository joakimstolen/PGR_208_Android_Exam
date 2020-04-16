package com.example.exam_testing.Activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.exam_testing.Adapter.MainAdapter
import com.example.exam_testing.Data.PlaceDatabase
import com.example.exam_testing.Data.PlaceEntity
import com.example.exam_testing.R
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {

    private var adapter: MainAdapter? = null


    companion object{
        val TAG = "MainActivity"

    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        println("oncreate called")
        

        recyclerview_main.layoutManager = LinearLayoutManager(this)


        renderLocation()

        Toast.makeText(this, "Loading...", Toast.LENGTH_LONG).show()


    }

    private fun renderLocation(){

        val db = Room.databaseBuilder(applicationContext, PlaceDatabase::class.java, "PlacesDatabaseReal.db").allowMainThreadQueries().build()
        val getPlaces = db.placeDao().getAllPlaces()

        runOnUiThread{
            println("Loading data from database")

            adapter = MainAdapter(
                getPlaces as MutableList<PlaceEntity>
            )
            recyclerview_main.adapter = adapter
            adapter!!.notifyDataSetChanged()

        }
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


}
