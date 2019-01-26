package com.violenthoboenterprises.virtualplant

import android.content.SharedPreferences
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

//Created on 22/01/2019
class MainActivity : AppCompatActivity() {

    val TAG: String = "MainActivity"
    private val PLANT_DEAD_KEY = "plant_dead_key"
    private val PLANT_WATERED_KEY = "plant_watered_key"
    private val PLANT_HEALTH_KEY = "plant_health_key"
    private val PLANT_SIZE_KEY = "plant_size_key"
    private val TIME_PLANTED_KEY = "time_planted_key"
    var preferences: SharedPreferences? = null
    private var plantDead: Boolean = false
    var plantWatered: Boolean = false
    var plantHealth: Int = 0
    var plantSize: Int = 0
    private var timePlanted: Long = 0
    private val DAY_IN_MILLIS: Long = 86400000
    var daysOut: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(tb)
        preferences = this.getSharedPreferences("com.violenthoboenterprises.virtualplant.preferences", 0)
    }

    //method is called once per day
    private fun anotherDay() {
        val editor = preferences!!.edit()
        //only update plant size and health if it is still alive
        if (!plantDead) {
            //plant grows only if watered on previous day and not already fully grown
            if (plantSize < 4 && plantWatered) {
                plantSize++
                editor.putInt(PLANT_SIZE_KEY, plantSize)
                editor.apply()
            }
            //if plant not watered on previous day it's health is reduced. 0 == healthy, 4 == dead
            if (!plantWatered) {
                plantHealth++
                editor.putInt(PLANT_HEALTH_KEY, plantHealth)
                editor.apply()
                //plant has died
                if (plantHealth == 4) {
                    plantDead = true
                    editor.putBoolean(PLANT_DEAD_KEY, true)
                    editor.apply()
                }
            }
        }
        //water button returns every day
//        btnWater.visibility = View.VISIBLE
        plantWatered = false
        editor.putBoolean(PLANT_WATERED_KEY, false)
        editor.apply()
        updateImage()
    }

    //water button clicked
    fun waterPlant() {
        val dropSound: MediaPlayer = MediaPlayer.create(this, R.raw.water_drop)
        dropSound.start()
        val editor = preferences!!.edit()
        //remove button. can only water plant once per day
//        btnWater.visibility = View.INVISIBLE
        //watering the plant does nothing if plant has died
        if(!plantDead) {
            //mark plant as watered so size and health can be updated on following day
            plantWatered = true
            editor.putBoolean(PLANT_WATERED_KEY, true)
            editor.apply()
            //increase plant health if not already full health
            if (plantHealth != 0) {
                plantHealth--
                editor.putInt(PLANT_HEALTH_KEY, plantHealth)
                editor.apply()
            }
            //first time watering plant. set timestamp and inform user to water plant every day
            if (timePlanted == 0.toLong()) {
                timePlanted = Calendar.getInstance().timeInMillis
                editor.putLong(TIME_PLANTED_KEY, timePlanted)
                editor.apply()
                Toast.makeText(this, "Water your plant every day", Toast.LENGTH_SHORT).show()
            }
        //inform user that plant is dead
        }else{
            Toast.makeText(this, "Your plant is dead \uD83D\uDE2D", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    //reset the plant
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.miReset -> {
            val editor = preferences!!.edit()
            plantDead = false
            plantWatered = false
            plantHealth = 0
            plantSize = 0
            timePlanted = 0
            editor.putBoolean(PLANT_DEAD_KEY, false)
            editor.putBoolean(PLANT_WATERED_KEY, false)
            editor.putInt(PLANT_HEALTH_KEY, plantHealth)
            editor.putInt(PLANT_SIZE_KEY, plantSize)
            editor.putLong(TIME_PLANTED_KEY, timePlanted)
            editor.apply()
//            btnWater.visibility = View.VISIBLE
            imgPlant.setImageDrawable(resources.getDrawable(R.drawable.plant_zero))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    //determining if a day has passed
    fun getDiff(): Long {
        val now: Long = Calendar.getInstance().timeInMillis
        return now - timePlanted
    }

    override fun onResume() {
        super.onResume()
        //Getting shared preferences
        plantDead = preferences!!.getBoolean(PLANT_DEAD_KEY, false)
        plantWatered = preferences!!.getBoolean(PLANT_WATERED_KEY, false)
        plantHealth = preferences!!.getInt(PLANT_HEALTH_KEY, 0)
        plantSize = preferences!!.getInt(PLANT_SIZE_KEY, 0)
        timePlanted = preferences!!.getLong(TIME_PLANTED_KEY, 0)
        updateImage()
        val editor = preferences!!.edit()
        if (getDiff() > DAY_IN_MILLIS && timePlanted != 0.toLong()) {
            //it's possible that user hasn't visited the app for more than one day. Need to adjust the plant accordingly
            daysOut = (getDiff() / DAY_IN_MILLIS).toInt()
            for(i in 1..daysOut){
                anotherDay()
            }
            timePlanted += (DAY_IN_MILLIS * daysOut)
            editor.putLong(TIME_PLANTED_KEY, timePlanted)
            editor.apply()
        }
    }

    private fun updateImage() {
        //setting the correct plant image based on size and health
        when (plantSize) {
            1 -> when (plantHealth) {
                0 -> imgPlant.setImageDrawable(resources.getDrawable(R.drawable.plant_one_zero))
                1 -> imgPlant.setImageDrawable(resources.getDrawable(R.drawable.plant_one_one))
                2 -> imgPlant.setImageDrawable(resources.getDrawable(R.drawable.plant_one_two))
                3 -> imgPlant.setImageDrawable(resources.getDrawable(R.drawable.plant_one_three))
                else -> imgPlant.setImageDrawable(resources.getDrawable(R.drawable.plant_one_four))
            }
            2 -> when (plantHealth) {
                0 -> imgPlant.setImageDrawable(resources.getDrawable(R.drawable.plant_two_zero))
                1 -> imgPlant.setImageDrawable(resources.getDrawable(R.drawable.plant_two_one))
                2 -> imgPlant.setImageDrawable(resources.getDrawable(R.drawable.plant_two_two))
                3 -> imgPlant.setImageDrawable(resources.getDrawable(R.drawable.plant_two_three))
                else -> imgPlant.setImageDrawable(resources.getDrawable(R.drawable.plant_two_four))
            }
            3 -> when (plantHealth) {
                0 -> imgPlant.setImageDrawable(resources.getDrawable(R.drawable.plant_three_zero))
                1 -> imgPlant.setImageDrawable(resources.getDrawable(R.drawable.plant_three_one))
                2 -> imgPlant.setImageDrawable(resources.getDrawable(R.drawable.plant_three_two))
                3 -> imgPlant.setImageDrawable(resources.getDrawable(R.drawable.plant_three_three))
                else -> imgPlant.setImageDrawable(resources.getDrawable(R.drawable.plant_three_four))
            }
            4 -> when (plantHealth) {
                0 -> imgPlant.setImageDrawable(resources.getDrawable(R.drawable.plant_four_zero))
                1 -> imgPlant.setImageDrawable(resources.getDrawable(R.drawable.plant_four_one))
                2 -> imgPlant.setImageDrawable(resources.getDrawable(R.drawable.plant_four_two))
                3 -> imgPlant.setImageDrawable(resources.getDrawable(R.drawable.plant_four_three))
                else -> imgPlant.setImageDrawable(resources.getDrawable(R.drawable.plant_four_four))
            }
        }
    }

}
