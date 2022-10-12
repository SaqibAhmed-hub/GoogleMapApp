package com.example.mapapp

import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private lateinit var mMap: GoogleMap
    internal lateinit var mLastLocation: Location
    internal var mCurrentLoactionMarker: Marker? = null
    internal var mGoogleApiClient: GoogleApiClient? = null
    internal lateinit var mLocationrequest: LocationRequest


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                buildGoogleAPiClient()
                mMap.isMyLocationEnabled = true

            }
        } else {
            buildGoogleAPiClient()
            mMap.isMyLocationEnabled = true
        }
    }

    @Synchronized
    private fun buildGoogleAPiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API).build()
        mGoogleApiClient!!.connect()
    }

    override fun onLocationChanged(location: Location) {
        mLastLocation = location
        if (mCurrentLoactionMarker != null) {
            mCurrentLoactionMarker!!.remove()
        }

        //place the Current Location
        val latlang = LatLng(location.latitude, location.longitude)
        val markerOptions = MarkerOptions()
        markerOptions.position(latlang)
        markerOptions.title("Current Location")
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        mCurrentLoactionMarker = mMap.addMarker(markerOptions)
        //move the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlang))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11f))

        //stop location update
        if (mGoogleApiClient != null) {
            LocationServices.getFusedLocationProviderClient(this)
        }
    }

    override fun onConnected(bundle: Bundle?) {
        mLocationrequest = LocationRequest()
        mLocationrequest.interval = 1000
        mLocationrequest.fastestInterval = 1000
        mLocationrequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            LocationServices.getFusedLocationProviderClient(this)
        }
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.e("Error", "Unexpected Error")
    }

    fun searchLocation() {
        val locationsearch: EditText = findViewById<EditText>(R.id.ed_text)
        lateinit var location: String
        location = locationsearch.text.toString()
        var addressList: List<Address>? = null
        if (location.isEmpty()) {
            Toast.makeText(this, "Please enter the Location", Toast.LENGTH_SHORT).show()
        } else {
            val geoCoder = Geocoder(this, Locale.getDefault())
            try {
                addressList = geoCoder.getFromLocationName(location, 1)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (addressList != null) {
                val lat = addressList[0].latitude
                val lng = addressList[0].longitude
                val latLng = LatLng(lat, lng)
                mMap.addMarker(MarkerOptions().position(latLng).title(location))
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                Toast.makeText(this, "$lat $lng", Toast.LENGTH_SHORT)
                    .show()

            }

        }

    }
}