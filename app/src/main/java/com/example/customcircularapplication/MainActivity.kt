package com.example.customcircularapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var circularProgressBar: CircularProgressBar
    lateinit var lineChart: SimpleLineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lineChart = findViewById(R.id.lineChart)
        val xItems = arrayOf("1","2","3","4","5","6","7")
        val yItems = arrayOf("10k","20k","30k","40k","50k")
        lineChart.setYItem(yItems)
        lineChart.setXItem(xItems)
        val pointMap: HashMap<Int, Int> = HashMap()
        for (i in xItems.indices) {
            pointMap[i] = (Math.random() * 5).toInt()
        }
        lineChart.setPointMap(pointMap)
//        circularProgressBar = findViewById<CircularProgressBar>(R.id.circularProgress)
//        Handler(Looper.getMainLooper()).postDelayed({
//            setProgress()
//        }, 3000)
    }

    private fun setProgress() {
        object : Thread() {
            override fun run() {
                for (i in 1..100) {
                    circularProgressBar.setProgress(i)
                    try {
                        sleep(100)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
        }.start()
    }
}