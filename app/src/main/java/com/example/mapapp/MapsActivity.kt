package com.example.mapapp

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.mapapp.model.SiteData.data1
import com.example.mapapp.model.SiteData.data2
import com.example.mapapp.model.SiteData.data3
import com.example.mapapp.model.SiteData.data4
import com.example.mapapp.model.SiteData.data5
import com.example.mapapp.model.SiteLocation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    private val REQUEST_CODE = 200
    private val locationlist: MutableList<SiteLocation> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        /**
         * Add the data to a list
         */

        if (locationlist.isEmpty()) {
            locationlist.add(data1)
            locationlist.add(data2)
            locationlist.add(data3)
            locationlist.add(data4)
            locationlist.add(data5)
        }

        //check permission
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mapFragment.getMapAsync(this)
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
            )
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val customMarker: View =
            (getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.custom_marker,
                null
            )
        val numTxt = customMarker.findViewById<View>(R.id.tvProgress) as TextView

        for (element in locationlist) {
            numTxt.text = element.siteId.toString()
            val marker: Marker = mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(element.lat, element.long))
                    .title(element.siteName)
                    .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(customMarker)))
            )
            marker.showInfoWindow()
        }

        //To show the first item in list , move the camera towards it.
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    locationlist[0].lat,
                    locationlist[0].long
                ), 12f
            )
        )


    }

    private fun createDrawableFromView(custom_marker: View): Bitmap? {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        custom_marker.layoutParams =
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
        custom_marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
        custom_marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
        custom_marker.buildDrawingCache()
        val bitmap = Bitmap.createBitmap(
            custom_marker.measuredWidth,
            custom_marker.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        custom_marker.draw(canvas)
        return bitmap
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            mapFragment.getMapAsync(this)
        } else {
            Toast.makeText(this, "Please enable the location", Toast.LENGTH_SHORT).show()
        }
    }


}