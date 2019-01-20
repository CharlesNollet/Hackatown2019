package club.touchandgo.tag

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPut
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_maps.*
import org.json.JSONObject

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    override fun onMarkerClick(p0: Marker?) = false

    var getPlayersURL = "http://207.246.122.125:8080/getPlayers"
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private var playerName = ""
    private var myPlayer: Player? = null
    private val thread = Thread(Runnable {
        while(true){
            Thread.sleep(1000)
            getPlayerNames()
            updatePosition()
        }
    })

    private val mLocationRequest = LocationRequest.create()
        .setInterval(2)
        .setFastestInterval(1)
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)
            for (location in result!!.locations) {
                if (location != null) {
                    val newLatLng = LatLng(location.latitude, location.longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(newLatLng))
                    break
                }
            }
        }
    }

    override fun onBackPressed() {
        // super.onBackPressed(); commented this line in order to disable back press
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_AppCompat_DayNight_NoActionBar)
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
        thread.start()
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

        mMap.setOnMarkerClickListener {
            runOnUiThread{
                if(myPlayer?.tag == true && it.title != playerName){
                    updatePlayer(true, it.title)
                    updatePlayer(false, playerName)
                    val toast = Toast.makeText(applicationContext,"You just tagged ${it.title}", Toast.LENGTH_LONG)
                    toast.show()
                }
            }
            return@setOnMarkerClickListener false
        }

    }

    private fun updatePosition(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    lastLocation = location
                    updatePlayer(location.latitude.toFloat(), location.longitude.toFloat())
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
                    mMap.clear()
                    for (player in players){
                        if(myPlayer != null && player.username != playerName){
                            if((myPlayer as Player).game == player.game){
                                var bmp: BitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.blue_circle)
                                if(player.tag){
                                    bmp = BitmapDescriptorFactory.fromResource(R.drawable.red_circle)
                                }
                                mMap.addMarker(
                                    MarkerOptions()
                                        .position(LatLng(player.lat.toDouble(),player.long.toDouble()))
                                        .icon(bmp)
                                        .title(player.username))
                            }
                        } else if (player.username == playerName) {
                            if(players.size == 1){
                                updatePlayer(true, playerName)
                            }
                            var bmp: BitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.green_circle)
                            topBar.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                            topBar.text = "HUNTED"
                            if(player.tag){
                                topBar.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.tagColor))
                                topBar.text = "TAGGER"
                                bmp = BitmapDescriptorFactory.fromResource(R.drawable.red_circle)
                                if(myPlayer?.tag == false){
                                    val toast = Toast.makeText(applicationContext,"You just got tagged!", Toast.LENGTH_LONG)
                                    toast.show()
                                }
                            }
                            myPlayer = player
                            mMap.addMarker(
                                MarkerOptions()
                                    .position(LatLng(player.lat.toDouble(),player.long.toDouble()))
                                    .icon(bmp)
                                    .title(player.username))
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


        var putPlayerURL = "http://207.246.122.125:8080/putPlayer/$playerName"
        val request = putPlayerURL.httpPut().body(json.toString())
        request.httpHeaders["Content-Type"] = "application/json"
        request.responseString{ _, _, result ->

        }
    }

    private fun updatePlayer(tag: Boolean, name:String){
        val json = JSONObject()
        json.put("username", name)
        json.put("tag", tag)


        var putPlayerURL = "http://207.246.122.125:8080/putPlayer/$name"
        val request = putPlayerURL.httpPut().body(json.toString())
        request.httpHeaders["Content-Type"] = "application/json"
        request.responseString{ _, _, result ->

        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}

