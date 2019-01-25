package com.example.virtualplant

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Messenger
import android.os.PersistableBundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import java.util.*
import java.util.concurrent.TimeUnit
import android.content.ComponentName



//Created on 22/01/2019
class MainActivity : AppCompatActivity() {

    var plantDead: Boolean = false
    var plantWatered: Boolean = false
    var plantWateredToday: Boolean = false
    var plantHealth: Int = 0
    var plantSize: Int = 0
    var timePlanted: Long = 0
    val dayInMillis: Long = 86400000
    val TAG: String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(tb)
    }

    //method is called once per day
    fun anotherDay() {
        //only update plant size and health if it is still alive
        if (!plantDead) {
            //plant grows only if watered on previous day and not already fully grown
            if (plantSize < 4 && plantWatered) {
                plantSize++
            }
//            tvPlantSize.setText("Plant size $plantSize")
            //if plant not watered on previous day it's health is reduced. 0 == healthy, 4 == dead
            if (!plantWatered) {
                plantHealth++
//                tvDaysNotWatered.setText("Days not watered $plantHealth")
                //plant has died
                if (plantHealth == 4) {
                    plantDead = true
//                    tvPlantDead.setText("Plant dead $plantDead")
                }
            }
        }
        //water button returns every day
        btnWater.visibility = View.VISIBLE
        plantWatered = false
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

    //water button clicked
    fun waterPlant(view: View) {
        //remove button. can only water plant once per day
        btnWater.visibility = View.INVISIBLE
        //watering the plant does nothing if plant has died
        if(!plantDead) {
            //mark plant as watered so size and health can be updated on following day
            plantWatered = true
            //increase plant health if not already full health
            if (plantHealth != 0) {
                plantHealth--
//                tvDaysNotWatered.setText("Days not watered $plantHealth")
            }
            //first time watering plant. set timestamp and inform user to water plant every day
            if (timePlanted == 0.toLong()) {
                timePlanted = Calendar.getInstance().timeInMillis
                Toast.makeText(this, "Water your plant every day", Toast.LENGTH_SHORT).show()
            } else {
//                timePlanted += dayInMillis
//                while (getDiff() > dayInMillis) {
//                    timePlanted += dayInMillis
//                }
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
            plantDead = false
            plantWatered = false
            plantHealth = 0
            plantSize = 0
            timePlanted = 0
//            tvPlantSize.setText("Plant size $plantSize")
//            tvDaysNotWatered.setText("Days not watered $plantHealth")
//            tvPlantDead.setText("Plant dead $plantDead")
            btnWater.visibility = View.VISIBLE
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
        if (timePlanted == 0.toLong()) {
//            tvPlantSize.setText("Plant size $plantSize")
//            tvDaysNotWatered.setText("Days not watered $plantHealth")
//            tvPlantDead.setText("Plant dead $plantDead")
        } else if (getDiff() > dayInMillis) {
            anotherDay()
            timePlanted += dayInMillis
        }
    }

}
