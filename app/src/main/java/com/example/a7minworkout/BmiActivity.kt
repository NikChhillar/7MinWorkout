package com.example.a7minworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_bmi.*
import kotlinx.android.synthetic.main.activity_exercise.*
import java.math.BigDecimal
import java.math.RoundingMode

class BmiActivity : AppCompatActivity() {

    companion object{
        private const val METRIC_UNITS_VIEW = "METRIC_UNITS_VIEW"
        private const val US_UNITS_VIEW = "US_UNITS_VIEW"
    }
    private var currentVisibleView:String = METRIC_UNITS_VIEW
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi)

        setSupportActionBar(toolbarBmi)
        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "CALCULATE BMI"
        }
        toolbarBmi.setNavigationOnClickListener {
            onBackPressed()
        }
        makeVisibleMetricsUnitsView()
        rgUnits?.setOnCheckedChangeListener { _, checkedId :Int ->
            if (checkedId == R.id.rbMetrics){
                makeVisibleMetricsUnitsView()
            }else{
                makeVisibleUsUnitsView()
            }
        }
        btnCalculateUnits?.setOnClickListener {
           calculateUnits()
        }
    }


    private fun makeVisibleMetricsUnitsView(){
        currentVisibleView = METRIC_UNITS_VIEW
        tilMetricUnitHeight?.visibility = View.VISIBLE
        tilMetricUnitWeight?.visibility = View.VISIBLE
        tilMetricUsUnitHeightFeet?.visibility = View.GONE
        tilMetricUsUnitHeightInch?.visibility = View.GONE
        tilUsMetricUnitWeight?.visibility = View.GONE

        etMetricUnitHeight?.text!!.clear()
        etMetricUnitWeight?.text!!.clear()
        llDisplayBMIResult?.visibility = View.INVISIBLE
    }
    private fun makeVisibleUsUnitsView(){
        currentVisibleView = US_UNITS_VIEW
        tilMetricUnitHeight?.visibility = View.INVISIBLE
        tilMetricUnitWeight?.visibility = View.INVISIBLE
        tilMetricUsUnitHeightFeet?.visibility = View.VISIBLE
        tilMetricUsUnitHeightInch?.visibility = View.VISIBLE
        tilUsMetricUnitWeight?.visibility = View.VISIBLE

        etUsMetricUnitHeightFeet?.text!!.clear()
        etUsMetricUnitHeightInch?.text!!.clear()
        etUsMetricUnitWeight?.text!!.clear()
        llDisplayBMIResult?.visibility = View.INVISIBLE
    }
    private fun displayBmi(bmi: Float){
        val bmiLabel : String
        val bmiDescription : String
        if (bmi.compareTo(15f) <= 0){
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You should eat more! Like seriously dude!!!"
        }else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0){
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops! You should eat more! Like seriously dude!!!"
        }else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0
        ) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0
        ) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0
        ) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0
        ) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0
        ) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2,RoundingMode.HALF_EVEN).toString()
        llDisplayBMIResult?.visibility = View.VISIBLE
        tvBMIValue?.text = bmiValue
        tvBMIType?.text = bmiLabel
        tvBMIDescription?.text = bmiDescription

    }
    private fun validateMetricUnits(): Boolean{
        var isValid = true
        if (etMetricUnitWeight?.text.toString().isEmpty()){
            isValid = false
        }else if (etMetricUnitHeight?.text.toString().isEmpty()){
            isValid = false
        }
        return isValid
    }
    private fun validateUsUnits(): Boolean{
        var isValid = true
        when{
            etUsMetricUnitHeightFeet?.text.toString().isEmpty() ->{
                isValid = false
            }
            etUsMetricUnitWeight?.text.toString().isEmpty() ->{
                isValid = false
            }
            etUsMetricUnitHeightInch?.text.toString().isEmpty() ->{
                isValid = false
            }
        }
        return isValid
    }
    private fun calculateUnits(){
        if (currentVisibleView == METRIC_UNITS_VIEW){
            if (validateMetricUnits()){
                val height : Float = etMetricUnitHeight?.text.toString().toFloat() / 100
                val weight : Float = etMetricUnitWeight?.text.toString().toFloat()
                val bmi = weight / (height*height)
                displayBmi(bmi)
            }else{
                Toast.makeText(this,"Please enter valid values", Toast.LENGTH_SHORT).show()
            }
        }else{
            if (validateUsUnits()){
                val usWeight : Float = etUsMetricUnitWeight?.text.toString().toFloat()
                val feet : String = etUsMetricUnitHeightFeet?.text.toString()
                val inch : String = etUsMetricUnitHeightInch?.text.toString()
                val heightValue = inch.toFloat() + feet.toFloat() * 12
                val bmi = 703 * (usWeight / (heightValue * heightValue))
                displayBmi(bmi)
            }else{
                Toast.makeText(this,"Please enter valid values", Toast.LENGTH_SHORT).show()
            }
        }
    }
}