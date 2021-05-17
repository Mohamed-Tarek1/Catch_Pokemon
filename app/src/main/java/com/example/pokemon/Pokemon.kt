package com.example.pokemon

import android.location.Location

class Pokemon {
    var name:String?=null
    var des:String?=null
    var image:Int?=null
    var power:Double?=null
    var location:Location?=null
    var isCatch:Boolean?=false

    constructor(image:Int,name:String,des:String ,power:Double,lat:Double,log:Double){
        this.image=image
        this.name=name
        this.des= des
        this.power=power
        this.location= Location(name)
        this.location!!.latitude=lat
        this.location!!.longitude=log
    }

}