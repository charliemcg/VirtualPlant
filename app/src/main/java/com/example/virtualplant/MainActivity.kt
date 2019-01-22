package com.example.virtualplant

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    var plantDead: Boolean = false
    var plantWatered: Boolean = false
    var daysNotWatered: Int = 0
    var plantSize: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvDaysNotWatered = findViewById<TextView>(R.id.tvDaysNotWatered)
        val tvPlantSize = findViewById<TextView>(R.id.tvPlantSize)
        val btnWaterPlant = findViewById<Button>(R.id.btnWater)

        anotherDay()
    }

    fun anotherDay(){
        if(!plantDead){
            if(daysNotWatered == 0){
                when (plantSize) {
                    0 -> plantSize++
                    1 -> plantSize++
                    2 -> plantSize++
                    3 -> plantSize++
                }
            }
            if(plantWatered && daysNotWatered != 0){
                daysNotWatered--
            } else if(!plantWatered){
                daysNotWatered++
                if(daysNotWatered == 4){
                    plantDead = true
                }
            }
        }
        plantWatered = true

    }

    fun waterPlant(view: View){
        println("Watering plant")
    }

}
