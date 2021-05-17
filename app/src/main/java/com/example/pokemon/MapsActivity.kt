package com.example.pokemon

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Exception

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        checkPermissions()
    }
    val AccesLocation=123
    fun checkPermissions (){
        if(Build.VERSION.SDK_INT>=23){

            if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),AccesLocation)
                return
            }

        }
        loadPokemons()
         getUserLocation()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when(requestCode){
        AccesLocation->{
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getUserLocation()
            }else{
                Toast.makeText(this, "location access is denied",Toast.LENGTH_LONG).show()
            }
        }

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    // a function to get the location of the User -->"Mario"<--
    fun getUserLocation (){
        Toast.makeText(this, "location access now ",Toast.LENGTH_LONG).show()
        //ToDo: access user location
        val mylocation = MyLocationListener()
        val locationmanager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3,3f,mylocation)

        val myThread = MyThread()
        myThread.start()

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }
    var myLocation:Location?=null

    //inner class to change the Location of the pokemon
    inner class MyLocationListener:LocationListener{
        constructor(){
            myLocation = Location("me")
            myLocation!!.latitude=0.0
            myLocation!!.longitude=0.0
    }
        override fun onLocationChanged(location: Location){
            myLocation=location
        }

    }

    var oldLocation:Location?=null
    // inner class to manage the old & new location of the Pokemons
    inner class MyThread:Thread{
        constructor():super(){
        oldLocation= Location("oldLocation")
            oldLocation!!.latitude=0.0
            oldLocation!!.longitude=0.0

        }

        override fun run() {
            while (true){
                try {
                if(oldLocation!!.distanceTo(myLocation)==0f){
                    continue
                }
                oldLocation=myLocation


                    runOnUiThread{
                        mMap!!.clear()
                        // Add a marker in Sydney and move the camera
                        val sydney = LatLng(myLocation!!.latitude, myLocation!!.longitude)
                        mMap!!.addMarker(MarkerOptions()
                            .position(sydney)
                            .title("ME")
                            .snippet("here is my location")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mario)))
                        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,8f))

                        //show pokemons
                        for(i in 0..ListOfPokemons.size-1){
                        var newPokemon =ListOfPokemons[i]
                            if(newPokemon.isCatch==false){
                                val pockLocation = LatLng(newPokemon.location!!.latitude, newPokemon.location!!.longitude)
                                mMap!!.addMarker(MarkerOptions()
                                    .position(pockLocation)
                                    .title(newPokemon.name)
                                    .snippet(newPokemon.des+", power"+newPokemon.power)
                                    .icon(BitmapDescriptorFactory.fromResource(newPokemon.image!!)))

                                if (myLocation!!.distanceTo(newPokemon.location)<2){
                                    myPower+=newPokemon.power!!
                                    newPokemon.isCatch=true
                                    ListOfPokemons[i]=newPokemon
                                    Toast.makeText(applicationContext,"You catch new pokimon your new power is "+myPower,Toast.LENGTH_LONG).show()
                                }

                            }
                    }
                    }
                    Thread.sleep(1000)
                }catch (ex:Exception){}
            }
        }
    }

    //the power of mario
    var myPower:Double =0.0


    // an empty list of pokemons
    var ListOfPokemons = ArrayList<Pokemon>()

    // a function to add the Pokemons to the empty list
    fun loadPokemons(){
        ListOfPokemons.add(Pokemon(R.drawable.charmander,"Charmander","Charmander lives in jaban",55.0,29.9495,32.5989))

        ListOfPokemons.add(Pokemon(R.drawable.bulbasaur,"Bulbasur","Bulbasur lives in usa",90.5,30.92181,33.9350))

        ListOfPokemons.add(Pokemon(R.drawable.squirtle,"Squirtle","Squirtle lives in iraq",33.5,29.5466,28.5598))
    }
}