package com.example.virtualplant

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    var plantDead: Boolean = false
    var plantWatered: Boolean = false
    var daysNotWatered: Int = 0
    var plantSize: Int = 0
    var timePlanted: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                tvPlantSize.setText("Plant size $plantSize")
            }
            if(plantWatered && daysNotWatered != 0){
                daysNotWatered--
            } else if(!plantWatered){
                daysNotWatered++
                if(daysNotWatered == 4){
                    plantDead = true
                }
            }
            tvDaysNotWatered.setText("Days not watered $daysNotWatered")
        }
        plantWatered = true
    }

    fun waterPlant(view: View){
        btnWater.visibility = View.INVISIBLE
        plantWatered = true
        if(timePlanted == 0.toLong()){
            timePlanted = Calendar.getInstance().timeInMillis
        }else{
            timePlanted += 10000
            while(getDiff() > 1000){
                timePlanted += 10000
            }
        }
        anotherDay()
    }

    override fun onResume() {
        super.onResume()
            if (getDiff() > 10000) {
                btnWater.visibility = View.VISIBLE
            }
    }

    fun getDiff() : Long {
        val now: Long = Calendar.getInstance().timeInMillis
        return now - timePlanted
    }

}
