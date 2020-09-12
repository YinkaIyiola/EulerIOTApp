package com.realyinkaiyiola.eulerhome


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Device {

    @SerializedName("fan")
    @Expose
    private var fan: Int = 0

    @SerializedName("bulb")
    @Expose
    private var bulb: Int = 0

    @SerializedName("tv")
    @Expose
    private var tv: Int = 0

    constructor()

    constructor(fan: Int, bulb: Int, tv: Int) {
        this.fan = fan
        this.bulb = bulb
        this.tv = tv
    }

    fun getFan(): Int{
        return fan
    }

    fun setFan(fan: Int){
        this.fan = fan
    }

    fun getBulb(): Int{
        return bulb
    }

    fun setBulb(bulb: Int){
        this.bulb = bulb
    }

    fun getTV(): Int{
        return tv
    }

    fun setTV(tv: Int){
        this.tv = tv
    }
}
