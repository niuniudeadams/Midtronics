package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    val buttons = ArrayList<Button>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val items = resources.getStringArray(R.array.countries_array)
        val linearLayout = findViewById<LinearLayout>(R.id.countryList)

        for (item in items) {
            val button = Button(this)
            button.text = item
            button.setOnClickListener {
                // Jump to countryDetail
                val intent = Intent(this, CountryDetail::class.java)
                intent.putExtra("countrySelected", item)
                startActivity(intent)
            }
            buttons.add(button)
            linearLayout.addView(button)
        }
        // get search button
        val searchButton = findViewById<Button>(R.id.search_button)
        // get profile button
        val profileButton = findViewById<Button>(R.id.profileButton)
        // get EditText Object
        val editText = findViewById<EditText>(R.id.searchEditText)
        // get manger object
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager


        // add click event for search button
        searchButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // click evead
                imm.hideSoftInputFromWindow(searchButton.windowToken, 0)

                val searchContent = editText.text
                val regex = Regex(searchContent.toString(), RegexOption.IGNORE_CASE)
                for (button in buttons){
                    button.visibility=View.VISIBLE
                    if(!regex.containsMatchIn(button.text.toString())){
                        button.visibility=View.GONE
                    }
                }
            }
        })

        // add click event for profile button
        profileButton.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }




    }





}