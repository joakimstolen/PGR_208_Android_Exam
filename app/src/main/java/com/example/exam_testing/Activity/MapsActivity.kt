package com.example.exam_testing.Activity

import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.exam_testing.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener  {



    companion object{
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1

    }

    private lateinit var map: GoogleMap

    private lateinit var lastLocation: Location

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        val navBarTitle = intent.getStringExtra(PlacesActivity.PlaceDetailViewHolder.PLACE_NAME_KEY)
        supportActionBar?.title = navBarTitle


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap



        val lat = intent.getDoubleExtra(PlacesActivity.PlaceDetailViewHolder.PLACE_LAT_KEY, -1.0)
        val lon = intent.getDoubleExtra(PlacesActivity.PlaceDetailViewHolder.PLACE_LON_KEY, -1.0)

        //map.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null){
                lastLocation = location
                val currentLatLng = LatLng(lat, lon)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        }

        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)

        setUpMap()



    }


    private fun setUpMap() {

        val lat = intent.getDoubleExtra(PlacesActivity.PlaceDetailViewHolder.PLACE_LAT_KEY, -1.0)
        val lon = intent.getDoubleExtra(PlacesActivity.PlaceDetailViewHolder.PLACE_LON_KEY, -1.0)



        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        //map.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(lat, lon)
                placeMarkerOnMap(currentLatLng)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        }
    }

    private fun placeMarkerOnMap(location: LatLng){
        val markerOptions = MarkerOptions().position(location)

        val titleStr = getAddress(location)
        markerOptions.title(titleStr)

        map.addMarker(markerOptions)
    }

    private fun getAddress(latLng: LatLng): String {

        val lat = intent.getDoubleExtra(PlacesActivity.PlaceDetailViewHolder.PLACE_LAT_KEY, -1.0)
        val lon = intent.getDoubleExtra(PlacesActivity.PlaceDetailViewHolder.PLACE_LON_KEY, -1.0)

        // 1
        val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())
        val addresses: List<Address>?
        val address: Address?
        var addressText = ""

        try {
            // 2
            addresses = geocoder.getFromLocation(lat, lon, 1)
            // 3
            if (null != addresses && !addresses.isEmpty()) {
                address = addresses[0]
                for (i in 0 until address.maxAddressLineIndex) {
                    addressText += if (i == 0) address.getAddressLine(i) else "\n" + address.getAddressLine(i)
                }
            }
        } catch (e: IOException) {
            Log.e("MapsActivity", e.localizedMessage)
        }

        return addressText
    }


    override fun onMarkerClick(p0: Marker?) = false



}
