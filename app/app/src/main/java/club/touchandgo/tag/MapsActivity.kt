package club.touchandgo.tag

import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPut
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.gson.Gson
import org.json.JSONObject

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    override fun onMarkerClick(p0: Marker?) = false

    var getPlayersURL = "http://207.246.122.125:8080/getPlayers"
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private var playerName = ""

    private val mLocationRequest = LocationRequest.create()
        .setInterval(1000)
        .setFastestInterval(500)
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)
            for (location in result!!.locations)
                if (location != null) {
                    val newLatLng = LatLng(location.latitude, location.longitude)
                    updatePlayer(location.latitude.toFloat(), location.longitude.toFloat())
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(newLatLng))
                    break
                }
        }
    }

    override fun onBackPressed() {
        // super.onBackPressed(); commented this line in order to disable back press
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        playerName = intent.getStringExtra(AlarmClock.EXTRA_MESSAGE)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onResume() {
        super.onResume()
        getPlayerNames()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)

        // Customise the styling of the base map using a JSON object defined
        // in a raw resource file.
        val success = googleMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                this, R.raw.style_json
            )
        )


        setUpMap()
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        mMap.isMyLocationEnabled = true

        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    lastLocation = location
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16f))
                } else {
                    fusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null)
                }
            }
        } catch (ex: SecurityException) {
            ex.printStackTrace()
        }

    }

    private fun getPlayerNames(){
        getPlayersURL.httpGet().responseObject(Player.Deserializer()) { request, response, result ->
            runOnUiThread{
                val (players, err) = result
                if (players != null){
                    for (player in players){
                        if(player.username != playerName){
                            //setIcon
                        }
                    }
                }
            }
        }
    }

    private fun updatePlayer(latitude: Float, longitude: Float){
        val json = JSONObject()
        json.put("username", playerName)
        json.put("lat", latitude)
        json.put("long", longitude)
        json.put("tag", "false")


        var putPlayerURL = "http://207.246.122.125:8080/putPlayer/$playerName"
        val request = putPlayerURL.httpPut().body(json.toString())
        request.httpHeaders["Content-Type"] = "application/json"
        request.responseString{ _, _, result ->

        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}

