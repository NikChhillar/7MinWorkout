package com.example.a7minworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_bmi.*
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        setSupportActionBar(toolbarHistory)
        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "HISTORY"
        }
        toolbarHistory.setNavigationOnClickListener {
            onBackPressed()
        }
        val dao = (application as WorkOutApp).db.historyDao()
        getAllCompletedDates(dao)

    }
    private fun getAllCompletedDates(historyDao: HistoryDao){
        lifecycleScope.launch {
            historyDao.fetchAllDates().collect { completedDates ->
               if (completedDates.isNotEmpty()){
                   tvHistory?.visibility = View.VISIBLE
                   rvHistory?.visibility = View.VISIBLE
                   tvNoDataAvailable?.visibility = View.INVISIBLE

                   rvHistory?.layoutManager = LinearLayoutManager(this@HistoryActivity)
                   val dates = ArrayList<String>()
                   for (date in completedDates){
                      dates.add(date.date)
                   }
                   val historyAdapter = HistoryAdapter(dates)
                   rvHistory?.adapter = historyAdapter


               }else{
                   tvHistory?.visibility = View.INVISIBLE
                   rvHistory?.visibility = View.INVISIBLE
                   tvNoDataAvailable?.visibility = View.VISIBLE
               }

            }
        }
    }

}