package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.myapplication.model.Country
import org.json.JSONArray
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import android.view.MenuItem

class CountryDetail : AppCompatActivity() {
    lateinit var capitalView: TextView
    lateinit var populationView: TextView
    lateinit var areaView: TextView
    lateinit var subRegionView: TextView
    lateinit var regionView: TextView
    lateinit var headline: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // return button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_country_detail)

        // initialize all textview
        headline = findViewById(R.id.headline)
        capitalView = findViewById<TextView>(R.id.countryCapital)
        populationView = findViewById<TextView>(R.id.countryPopulation)
        areaView = findViewById<TextView>(R.id.countryArea)
        subRegionView = findViewById<TextView>(R.id.countrySubRegion)
        regionView = findViewById<TextView>(R.id.countryRegion)

        // get the selected countryName
        val countryName = intent.getStringExtra("countrySelected").toString()
        // Call the countrydetail function to send http request and display the detail of that country
        getCountryDetail(countryName)



    }
   // return button event
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun getCountryDetail(item:String){

        try {


            Thread {
                // create a apiRequest
                val apiRequest="https://restcountries.com/v3.1/name/${item}"
                val url = URL(apiRequest)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                val inputStream = BufferedInputStream(connection.inputStream)
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                val stringBuilder = StringBuilder()
                // add all response to stringbuilder
                bufferedReader.forEachLine {
                    stringBuilder.append(it)
                }
                // change string to JSONArray
                val jsonArr = JSONArray(stringBuilder.toString())

                // get the first element
                val jsonObject = jsonArr.getJSONObject(0)

                Log.w("response",jsonObject.toString())

                // handle capital string if we cannot get the capital, show N/A
                var capital = if (jsonObject.has("capital")) jsonObject.optString("capital") else "[N/A]"
                // remove [ ]
                capital = capital.substring(1,capital.length-1)
                // remove ""
                capital = capital.replace("\"","")

                Log.w("response",capital)

                val population = if (jsonObject.has("population")) jsonObject.optString("population") else "N/A"

                val area = if (jsonObject.has("area")) jsonObject.optString("area") else "N/A"

                val region = if (jsonObject.has("region")) jsonObject.optString("region") else "N/A"

                val subregion = if (jsonObject.has("subregion")) jsonObject.optString("subregion") else "N/A"

                // create a country object
                val country = Country(capital,population,area,region,subregion)

                // changing the view at MainThread
                this@CountryDetail.runOnUiThread{

                    capitalView.text=country.capital
                    Log.w("132",capitalView.text.toString())
                    populationView.text=country.population

                    areaView.text=country.area

                    regionView.text=country.region

                    subRegionView.text=country.subregion

                    headline.text= item
                }
            }.start()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }


    }
}