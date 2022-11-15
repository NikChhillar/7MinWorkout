package com.example.a7minworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minworkout.databinding.ActivityExerciseBinding
import com.example.a7minworkout.databinding.DialogBackConfirmBinding
import kotlinx.android.synthetic.main.activity_exercise.*
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding:ActivityExerciseBinding? = null
    private var restTimer: CountDownTimer? = null
    private var restProgress = 0

    private var restTimerDuration :Long = 10
    private var exerciseTimerDuration :Long = 30
    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0

    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var exerciseAdapter : ExerciseStatusAdapter? = null
    private var tts : TextToSpeech? = null
    private var player: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)
       // setContentView(binding?.root)

        setSupportActionBar(toolbarEx)
        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        toolbarEx.setNavigationOnClickListener {
            customDialogBack()
        }
        tts = TextToSpeech(this,this)

        exerciseList = Constants.defaultExerciseList()
        setupRestView()
        setupExerciseStatusRecycleView()

    }
    private fun customDialogBack() {
        val customDialog = Dialog(this)
        val dialogBinding = DialogBackConfirmBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.btnYes.setOnClickListener {
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.btnNo.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()
    }
    private fun setupExerciseStatusRecycleView(){
        rvExStatus?.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL,false)
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        rvExStatus?.adapter = exerciseAdapter
    }
    private fun setupRestView(){

        try {
            val soundURI = Uri.parse("android.resource://com.example.a7minworkout/" + R.raw.sec_ring)
            player = MediaPlayer.create(applicationContext, soundURI)
            player?.isLooping = false
            player?.start()
        }catch (e:Exception){
            e.printStackTrace()
        }

        flRestView?.visibility= View.VISIBLE
        tvTitle?.visibility = View.VISIBLE
        tvExercise?.visibility = View.INVISIBLE
        ivImage?.visibility = View.INVISIBLE
        flExerciseView?.visibility = View.INVISIBLE
        tvUpcomingExercise?.visibility = View.VISIBLE
        tvUpcomingLabel?.visibility = View.VISIBLE

        if (restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }
        tvUpcomingExercise?.text = exerciseList!![currentExercisePosition+ 1].getName()
        setRestProgressBar()
    }
    private fun setupExerciseView(){

        flRestView?.visibility= View.INVISIBLE
        tvTitle?.visibility = View.INVISIBLE
        tvExercise?.visibility = View.VISIBLE
        ivImage?.visibility = View.VISIBLE
        flExerciseView?.visibility = View.VISIBLE
        tvUpcomingExercise?.visibility = View.INVISIBLE
        tvUpcomingLabel?.visibility = View.INVISIBLE

        if (exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }
        speakOut(exerciseList!![currentExercisePosition].getName())
        ivImage?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        tvExercise?.text = exerciseList!![currentExercisePosition].getName()
        setExerciseProgressBar()
    }
    private fun setRestProgressBar(){
        progressBar?.progress = restProgress
        restTimer = object :CountDownTimer(restTimerDuration*1000,1000){
            override fun onTick(p0: Long) {
                restProgress++
                progressBar?.progress = 11 - restProgress
                tvTimer?.text = (11-restProgress).toString()
            }
            override fun onFinish() {
                currentExercisePosition++
                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()
                setupExerciseView()
               // Toast.makeText(this@ExerciseActivity,"Time to start the exercise!!",Toast.LENGTH_SHORT).show()
            }
        }.start()
    }
    private fun setExerciseProgressBar(){
        progressBarExercise?.progress = exerciseProgress
        exerciseTimer = object :CountDownTimer(exerciseTimerDuration* 1000,1000){
            override fun onTick(p0: Long) {
                exerciseProgress++
                progressBarExercise?.progress = 31 - exerciseProgress
                tvExTimer?.text = (31-exerciseProgress).toString()
            }
            override fun onFinish() {

                if (currentExercisePosition < exerciseList?.size!! -1){
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setupRestView()
                }else{
                    finish()
                    startActivity(Intent(this@ExerciseActivity,LastActivity::class.java))
                 //   Toast.makeText(this@ExerciseActivity,"boiii!!!",Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
    override fun onDestroy() {
        super.onDestroy()
        if (restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }
        if (exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        if (tts != null){
            tts!!.stop()
            tts!!.shutdown()
        }
        if (player != null){
            player?.stop()
        }
        binding = null
    }
    override fun onBackPressed() {
        customDialogBack()
     //   super.onBackPressed()
    }
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS){
            val result = tts?.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Toast.makeText(this@ExerciseActivity,"OOPS!!",Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this@ExerciseActivity,"OOPS!!",Toast.LENGTH_SHORT).show()
        }
    }
    private fun speakOut(text :String){
        tts!!.speak(text,TextToSpeech.QUEUE_FLUSH,null ,"")
    }
}