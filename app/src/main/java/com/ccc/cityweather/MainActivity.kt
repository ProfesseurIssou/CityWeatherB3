package com.ccc.cityweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

data class WeatherForeCast(
    val day: Int,
    val temperature: String,
    val wind: String
)

data class WeatherData(
    val temperature: String,
    val wind: String,
    val description: String,
    val forecast: MutableList<WeatherForeCast>
)

// {
//  "temperature":"+10 °C",
//  "wind":"35 km/h",
//  "description":"Light rain",
//  "forecast":[
//      {
//          "day":"1",
//          "temperature":"+9 °C",
//          "wind":"28 km/h"
//      },
//      {
//          "day":"2",
//          "temperature":"8 °C",
//          "wind":"21 km/h"
//      },
//      {
//          "day":"3",
//          "temperature":"+7 °C",
//          "wind":"21 km/h"
//      }
//  ]
// }

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FetchWeatherData("paris")
    }

    private val client = OkHttpClient()
    public lateinit var weatherData: WeatherData

    fun FetchWeatherData(city: String){
        val request = Request.Builder()
            .url("https://goweather.herokuapp.com/weather/" + city)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val forecast: MutableList<WeatherForeCast> = mutableListOf()
                weatherData = WeatherData("", "", "", forecast)
            }
            override fun onResponse(call: Call, response: Response) {
                var rawWeatherData = (response.body()?.string())
                //var rawWeatherData = "{\"temperature\":\"+10 °C\",\"wind\":\"35 km/h\",\"description\":\"Light rain\",\"forecast\":[{\"day\":\"1\",\"temperature\":\"+9 °C\",\"wind\":\"28 km/h\"},{\"day\":\"2\",\"temperature\":\"8 °C\",\"wind\":\"21 km/h\"},{\"day\":\"3\",\"temperature\":\"+7 °C\",\"wind\":\"21 km/h\"}]}"
                var gson = Gson()
                weatherData = gson.fromJson(rawWeatherData, WeatherData::class.java)
            }
        })
    }
}