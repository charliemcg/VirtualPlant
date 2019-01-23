package com.example.virtualplant

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import java.util.*

class MainActivity : AppCompatActivity() {

    var plantDead: Boolean = false
    var plantWatered: Boolean = false
    var plantHealth: Int = 0
    var plantSize: Int = 0
    var timePlanted: Long = 0
    val dayInMillis: Long = 86400000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(tb)
    }

    fun anotherDay() {
        if (!plantDead) {
//            if (daysNotWatered == 0) {
            if (plantSize < 4 && plantWatered) {
                plantSize++
            }
            tvPlantSize.setText("Plant size $plantSize")
//            }
//            if (plantWatered && daysNotWatered != 0) {
//                daysNotWatered--
            if (!plantWatered) {
                plantHealth++
                tvDaysNotWatered.setText("Days not watered $plantHealth")
                if (plantHealth == 4) {
                    plantDead = true
                    tvPlantDead.setText("Plant dead $plantDead")
                }
            }
        }
        btnWater.visibility = View.VISIBLE
        plantWatered = false
//        if (getDiff() > dayInMillis) {
//            Log.d("MainActivity", "diff: " + getDiff())
//                timePlanted += dayInMillis
//                daysNotWatered += (getDiff() / dayInMillis).toInt()
//                daysNotWatered--
//                tvDaysNotWatered.setText("Days not watered $daysNotWatered")
//        }
//        plantWatered = true
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

    fun waterPlant(view: View) {
        btnWater.visibility = View.INVISIBLE
        if(!plantDead) {
            plantWatered = true
            if (plantHealth != 0) {
                plantHealth--
                tvDaysNotWatered.setText("Days not watered $plantHealth")
            }
            if (timePlanted == 0.toLong()) {
                timePlanted = Calendar.getInstance().timeInMillis
            } else {
                timePlanted += dayInMillis
                while (getDiff() > dayInMillis) {
                    timePlanted += dayInMillis
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.miReset -> {
            plantDead = false
            plantWatered = false
            plantHealth = 0
            plantSize = 0
            timePlanted = 0
            tvPlantSize.setText("Plant size $plantSize")
            tvDaysNotWatered.setText("Days not watered $plantHealth")
            tvPlantDead.setText("Plant dead $plantDead")
            btnWater.visibility = View.VISIBLE
            imgPlant.setImageDrawable(resources.getDrawable(R.drawable.plant_zero))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    fun getDiff(): Long {
        val now: Long = Calendar.getInstance().timeInMillis
        return now - timePlanted
    }

    override fun onResume() {
        super.onResume()
        if (timePlanted == 0.toLong()) {
            tvPlantSize.setText("Plant size $plantSize")
            tvDaysNotWatered.setText("Days not watered $plantHealth")
            tvPlantDead.setText("Plant dead $plantDead")
        } else if (getDiff() > dayInMillis) {
            anotherDay()
        }
    }

}
