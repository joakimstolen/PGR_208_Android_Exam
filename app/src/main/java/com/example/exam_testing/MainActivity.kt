package com.example.exam_testing

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException



class MainActivity : AppCompatActivity() {

    companion object{
        val TAG = "MainActivity"
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        Toast.makeText(this, "Welcome", Toast.LENGTH_LONG).show()

        recyclerview_main.layoutManager = LinearLayoutManager(this)
        //recyclerview_main.adapter = MainAdapter()

        fetchJson()






    }

    private fun fetchJson() {

        val url = "https://www.noforeignland.com/home/api/v1/places/"

        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback{

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                println(body)

                val gson = GsonBuilder().create()
                val places = gson.fromJson(body, Places::class.java)

                runOnUiThread{
                    recyclerview_main.adapter = MainAdapter(places)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "Failed to execute request")
            }
        })
    }
}
