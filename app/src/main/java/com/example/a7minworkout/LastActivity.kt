package com.example.a7minworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_exercise.*
import kotlinx.android.synthetic.main.activity_last.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class LastActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_last)

        setSupportActionBar(toolbarFinishActivity)
        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        toolbarFinishActivity.setNavigationOnClickListener {
            onBackPressed()
        }
        btnFinish.setOnClickListener {
            finish()
        }
        val dao = (application as WorkOutApp).db.historyDao()
        addDateToDb(dao)
    }
    private fun addDateToDb(historyDao: HistoryDao){
        val cal = Calendar.getInstance()
        val dateTime = cal.time
        Log.e("Date: ","" +dateTime)
        val sdf = SimpleDateFormat("dd MM yyyy HH:mm:ss", Locale.getDefault())
        val date = sdf.format(dateTime)
        Log.e("Formatted Date: ","" +date)


        lifecycleScope.launch {
            historyDao.insert(HistoryEntity(date))
        }

    }
}