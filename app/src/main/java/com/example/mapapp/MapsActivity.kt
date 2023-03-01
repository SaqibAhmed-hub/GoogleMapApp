package com.example.mapapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //check permission
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            currentLocation()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                44
            )
        }




    }

    @SuppressLint("MissingPermission")
    private fun currentLocation() {
       val client = LocationServices.getFusedLocationProviderClient(this)
        val task = client.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null){
                mapFragment.getMapAsync { googlemap ->
                    val latlng = LatLng(location.latitude,location.longitude)
                    googlemap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15f))
                    googlemap.addMarker(MarkerOptions().position(latlng).title("You are Here"))
                }
            }
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 44){
            currentLocation()
        }else{
            Toast.makeText(this,"Please enable the location", Toast.LENGTH_SHORT).show()
        }
    }


}