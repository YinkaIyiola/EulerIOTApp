package com.realyinkaiyiola.eulerhome

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.zagum.speechrecognitionview.RecognitionProgressView
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class MainActivity : AppCompatActivity(), RecognitionListener {

    var fan_button_background : Int = 0
    var bulb_button_background : Int = 0
    var tv_button_background : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
        val fanButton: Button = findViewById(R.id.fan)
        val bulbButton: Button = findViewById(R.id.bulb)
        val tvButton: Button = findViewById(R.id.tv)
        */

        getState()

        recognition_view.visibility = View.INVISIBLE

        val mic: ImageView = findViewById(R.id.mic)

        val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer.setRecognitionListener(this)
        //this will start the listening process
        //intent below to listen to vocal input and return the text in the same activity
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

        //use a language model based on free-form speech recognition
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, applicationContext.packageName)

        val recognitionProgressView: RecognitionProgressView = findViewById(R.id.recognition_view)
        recognitionProgressView.setSpeechRecognizer(speechRecognizer)
        recognitionProgressView.setRecognitionListener(this)

        val colors = intArrayOf(
            ContextCompat.getColor(this, R.color.colorBlue),
            ContextCompat.getColor(this, R.color.colorGreen),
            ContextCompat.getColor(this, R.color.colorRed),
            ContextCompat.getColor(this, R.color.colorYellow)
        )
        recognitionProgressView.setColors(colors)

        mic.setOnClickListener {
            speechRecognizer.startListening(intent)
            recognitionProgressView.postDelayed(Runnable {
                recognitionProgressView.play()
            },50)
        }
        recognitionProgressView.stop()
        getState()
        /**
        fanButton.setOnClickListener {
            if(fan_button_background == 0){
                fanButton.setBackgroundColor(resources.getColor(R.color.orange))
                fan_button_background = 1
                sendData(1,0,0)
                //Log.d("FAN", (mDevice?.getFan()).toString())

            }else if(fan_button_background == 1){
                fanButton.setBackgroundColor(resources.getColor(R.color.blue))
                fan_button_background = 0
                sendData(0,0,0)
            }
        }

        bulbButton.setOnClickListener {
            if(bulb_button_background == 0){
                bulbButton.setBackgroundColor(resources.getColor(R.color.orange))
                bulb_button_background = 1
                sendData(0,1,0)
            }else if(bulb_button_background == 1){
                bulbButton.setBackgroundColor(resources.getColor(R.color.blue))
                bulb_button_background = 0
                sendData(0,0,0)
            }
        }

        tvButton.setOnClickListener {
            if(tv_button_background == 0){
                tvButton.setBackgroundColor(resources.getColor(R.color.orange))
                tv_button_background = 1
                sendData(0,0,1)
            }else if(tv_button_background == 1){
                tvButton.setBackgroundColor(resources.getColor(R.color.blue))
                tv_button_background = 0
                sendData(0,0,0)
            }
        }
        */
    }

    var fan:Int = 0
    var bulb:Int = 0
    var tv:Int = 0

    private fun getState() {

        val apiClientObject = ApiClient()
        val apiInterface: ApiInterface = apiClientObject.getApiClient()!!
            .create(ApiInterface::class.java)
        val call: Call<Device> = apiInterface.getDeviceStatus(fan, bulb, tv)

        call.enqueue(object : Callback<Device> {
            override fun onFailure(call: Call<Device>, t: Throwable) {

            }

            override fun onResponse(call: Call<Device>, response: Response<Device>) {
                val fanValue: Int = response.body()!!.getFan()
                val bulbValue: Int = response.body()!!.getBulb()
                val tvValue: Int = response.body()!!.getTV()

                if (fanValue == 0){
                    //off state
                    fanState.setImageResource(R.drawable.fanoff)
                    textFanState.text = "OFF"
                }else{
                    //on state
                    fanState.setImageResource(R.drawable.fanon)
                    textFanState.text = "ON"
                }
                if (bulbValue == 0){
                    //off state
                    bulbState.setImageResource(R.drawable.bulboff)
                    textBulbState.text = "OFF"
                }else{
                    //on state
                    bulbState.setImageResource(R.drawable.bulbon)
                    textBulbState.text = "ON"
                }
                if (tvValue == 0){
                    //off state
                    tvState.setImageResource(R.drawable.tvoff)
                    textTvState.text = "OFF"
                }else{
                    //on state
                    tvState.setImageResource(R.drawable.tvon)
                    textTvState.text = "ON"
                }
            }
        })
    }
    private fun getData(background: Int){

        val apiClientObject = ApiClient()
        val apiInterface: ApiInterface = apiClientObject.getApiClient()!!
            .create(ApiInterface::class.java)
        val call: Call<Device> = apiInterface.getDeviceStatus(fan, bulb, tv)

        call.enqueue(object : Callback<Device>{
            override fun onFailure(call: Call<Device>, t: Throwable) {

            }

            override fun onResponse(call: Call<Device>, response: Response<Device>) {
                val fanValue: Int = response.body()!!.getFan()
                val bulbValue: Int = response.body()!!.getBulb()
                val tvValue: Int = response.body()!!.getTV()

                //on the fan only
                when (background) {
                    1 -> {
                        //on the fan only
                        sendData(1,bulbValue,tvValue)
                    }
                    2 -> {
                        //off the fan only
                        sendData(0,bulbValue,tvValue)
                    }
                    3 -> {
                        //on the bulb only
                        sendData(fanValue,1,tvValue)
                    }
                    4 -> {
                        //off the bulb only
                        sendData(fanValue,0,tvValue)
                    }
                    5 -> {
                        //on the tv only
                        sendData(fanValue,bulbValue,1)
                    }
                    6 -> {
                        //off the tv only
                        sendData(fanValue,bulbValue,0)
                    }
                    7 -> {
                        //on the fan and bulb only
                        sendData(1,1,tvValue)
                    }
                    8 -> {
                        //off the fan and bulb only
                        sendData(0,0,tvValue)
                    }
                    9 -> {
                        //on the fan and tv only
                        sendData(1,bulbValue,1)
                    }
                    10 -> {
                        //off the fan and tv only
                        sendData(0,bulbValue,0)
                    }
                    11 -> {
                        //on the bulb and tv only
                        sendData(fanValue,1,1)
                    }
                    12 -> {
                        //off the bulb and tv only
                        sendData(fanValue,0,0)
                    }
                    13 -> {
                        //on the fan and bulb off
                        sendData(1,0,tvValue)
                    }
                    
                    14 -> {
                        //off the fan and on the bulb only
                        sendData(0,1,tvValue)
                    }
                    15 -> {
                        //on the fan and off the tv only
                        sendData(1,bulbValue,0)
                    }
                    16 -> {
                        //off the fan and on the tv only
                        sendData(0,bulbValue,1)
                    }
                    17 -> {
                        //on the bulb and off the tv only
                        sendData(fanValue,1,0)
                    }
                    18 -> {
                        //off the bulb and on the tv only
                        sendData(fanValue,0,1)
                    }
                    19 -> {
                        //on all the appliances
                        sendData(1,1,1)
                    }
                    20 -> {
                        //off all the appliances
                        sendData(0,0,0)
                    }
                }
            }

        })
    }

    private fun sendData(fan: Int, bulb: Int, tv: Int){

        val apiClientObject = ApiClient()
        val apiInterface: ApiInterface = apiClientObject.getApiClient()!!
            .create(ApiInterface::class.java)
        val call: Call<Device> = apiInterface.saveDeviceStatus(fan, bulb, tv)

        call.enqueue(object : Callback<Device> {
            override fun onFailure(call: Call<Device>, t: Throwable) {

            }
            override fun onResponse(call: Call<Device>, response: Response<Device>) {
                if(response.isSuccessful){
                    //makeToast("Sent")
                }
            }

        })

    }

    override fun onReadyForSpeech(p0: Bundle?) {
        layoutMic.visibility = View.GONE
        layoutState.visibility = View.GONE
        recognition_view.visibility = View.VISIBLE
    }

    override fun onRmsChanged(p0: Float) {
        //implement the visual display of users' recording moment
        //but there's no guarantee that this method will be called
        //according to android documentation
    }

    override fun onBufferReceived(p0: ByteArray?) {
    }

    override fun onPartialResults(p0: Bundle?) {
    }

    override fun onEvent(p0: Int, p1: Bundle?) {
    }

    override fun onBeginningOfSpeech() {
    }

    override fun onEndOfSpeech() {
        recognition_view.stop()
        layoutMic.visibility = View.VISIBLE
        layoutState.visibility = View.VISIBLE
        recognition_view.visibility = View.INVISIBLE
    }

    override fun onError(p0: Int) {
    }

    override fun onResults(p0: Bundle?) {
        val result = p0?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        val key: String = result!![0]
        //Log.d("RESULT", key)
        if (("on the fan" in key || "on fan" in key) && "and" !in key){
            fanOn()
            refreshMainActivity()
        }else if(("off the fan" in key || "off fan" in key) && "and" !in key){
            fanOff()
            refreshMainActivity()
        }else if(("off the bulb" in key || "off bulb" in key) && "and" !in key) {
            bulbOff()
            refreshMainActivity()
        }else if (("on the bulb" in key || "on bulb" in key) && "and" !in key){
            bulbOn()
            refreshMainActivity()
        }else if (("off the tv" in key || "off tv" in key || "off the television" in key
            || "off television" in key) && "and" !in key){
            tvOff()
            refreshMainActivity()
        }else if (("on the tv" in key || "on tv" in key || "on the television" in key
            || "on television" in key) && "and" !in key){
            tvOn()
            refreshMainActivity()
        }else if (("on the fan" in key || "on fan" in key) && ("on the bulb" in key
            || "on bulb" in key)){
            fanAndBulbOn()
            refreshMainActivity()
        }else if (("off the fan" in key || "off fan" in key) && ("off the bulb" in key
                    || "off bulb" in key)){
            fanAndBulbOff()
            refreshMainActivity()
        }else if (("on the fan" in key || "on fan" in key) && ("on the tv" in key
                    || "on tv" in key || "on the television" in key || "on television" in key)){
            fanAndTvOn()
            refreshMainActivity()
        }else if (("off the fan" in key || "off fan" in key) && ("off the tv" in key
                    || "off tv" in key || "off the television" in key || "off television" in key)){
            fanAndTvOff()
            refreshMainActivity()
        }else if (("on the bulb" in key || "on bulb" in key) && ("on the tv" in key
                    || "on tv" in key || "on the television" in key || "on television" in key)){
            bulbAndTvOn()
            refreshMainActivity()
        }else if (("off the bulb" in key || "off bulb" in key) && ("off the tv" in key
                    || "off tv" in key || "off the television" in key || "off television" in key)){
            bulbAndTvOff()
            refreshMainActivity()
        }else if (("on the fan" in key || "on fan" in key) && ("off the bulb" in key
                    || "off bulb" in key)){
            fanOnAndBulbOff()
            refreshMainActivity()
        }else if (("off the fan" in key || "off fan" in key) && ("on the bulb" in key
                    || "on bulb" in key)){
            fanOffAndBulbOn()
            refreshMainActivity()
        }else if (("on the fan" in key || "on fan" in key) && ("off the tv" in key
                    || "off tv" in key || "off the television" in key || "off television" in key)){
            fanOnAndTvOff()
            refreshMainActivity()
        }else if (("off the fan" in key || "off fan" in key) && ("on the tv" in key
                    || "on tv" in key || "on the television" in key || "on television" in key)){
            fanOffAndTvOn()
            refreshMainActivity()
        }else if (("on the bulb" in key || "on bulb" in key) && ("off the tv" in key
                    || "off tv" in key || "off the television" in key || "off television" in key)){
            bulbOnAndTvOff()
            refreshMainActivity()
        }else if (("off the bulb" in key || "off bulb" in key) && ("on the tv" in key
                    || "on tv" in key || "on the television" in key || "on television" in key)){
            bulbOffAndTvOn()
            refreshMainActivity()
        }else if ("on all" in key || "on it all" in key || "on everything" in key){
            allOn()
            refreshMainActivity()
        }else if ("off all" in key || "off it all" in key || "off everything" in key){
            allOff()
            refreshMainActivity()
        }else{
            makeToast("Please give a full command or be clearer. Thanks!")
        }
    }

    private fun fanOn() {
        val button_background: Int = 1
        getData(button_background)
        makeToast("Done")
    }
    private fun fanOff() {
        val button_background: Int = 2
        getData(button_background)
        makeToast("Done")
    }
    private fun bulbOn() {
        val button_background: Int = 3
        getData(button_background)
        makeToast("Done")
    }
    private fun bulbOff() {
        val button_background: Int = 4
        getData(button_background)
        makeToast("Done")
    }
    private fun tvOn() {
        val button_background: Int = 5
        getData(button_background)
        makeToast("Done")
    }
    private fun tvOff() {
        val button_background: Int = 6
        getData(button_background)
        makeToast("Done")
    }
    private fun fanAndBulbOn() {
        val button_background: Int = 7
        getData(button_background)
        makeToast("Done")
    }
    private fun fanAndBulbOff() {
        val button_background: Int = 8
        getData(button_background)
        makeToast("Done")
    }
    private fun fanAndTvOn() {
        val button_background: Int = 9
        getData(button_background)
        makeToast("Done")
    }
    private fun fanAndTvOff() {
        val button_background: Int = 10
        getData(button_background)
        makeToast("Done")
    }
    private fun bulbAndTvOn() {
        val button_background: Int = 11
        getData(button_background)
        makeToast("Done")
    }
    private fun bulbAndTvOff() {
        val button_background: Int = 12
        getData(button_background)
        makeToast("Done")
    }
    private fun fanOnAndBulbOff() {
        val button_background: Int = 13
        getData(button_background)
        makeToast("Done")
    }
    private fun fanOffAndBulbOn() {
        val button_background: Int = 14
        getData(button_background)
        makeToast("Done")
    }
    private fun fanOnAndTvOff() {
        val button_background: Int = 15
        getData(button_background)
        makeToast("Done")
    }
    private fun fanOffAndTvOn() {
        val button_background: Int = 16
        getData(button_background)
        makeToast("Done")
    }
    private fun bulbOnAndTvOff() {
        val button_background: Int = 17
        getData(button_background)
        makeToast("Done")
    }
    private fun bulbOffAndTvOn() {
        val button_background: Int = 18
        getData(button_background)
        makeToast("Done")
    }
    private fun allOn() {
        val button_background: Int = 19
        getData(button_background)
        makeToast("Done")
    }
    private fun allOff() {
        val button_background: Int = 20
        getData(button_background)
        makeToast("Done")
    }

    private fun makeToast(message: String){
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
    }

    private fun refreshMainActivity() {

        //recreate()
        //finish()
        overridePendingTransition(0,0)
        val mainIntent = Intent(this@MainActivity, MainActivity::class.java)
        startActivity(mainIntent)
        overridePendingTransition(0,0)
    }

}