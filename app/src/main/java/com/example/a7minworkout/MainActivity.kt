package com.example.a7minworkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.a7minworkout.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var binding:ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        flS?.setOnClickListener {
            startActivity(Intent(this , ExerciseActivity::class.java))
           // Toast.makeText(this, ".............",Toast.LENGTH_SHORT).show()
        }

        flBMI?.setOnClickListener {
            startActivity(Intent(this , BmiActivity::class.java))
        }
        flHistory?.setOnClickListener {
            startActivity(Intent(this , HistoryActivity::class.java))
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        binding =  null
    }
}