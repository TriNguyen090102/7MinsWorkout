package com.example.a7minsworkout

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
import com.example.a7minsworkout.databinding.ActivityExercisesBinding
import com.example.a7minsworkout.databinding.DialogCustomBackConfirmationBinding
import java.util.*
import kotlin.collections.ArrayList


class ExercisesActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding : ActivityExercisesBinding? =null
    private var restTimer: CountDownTimer? = null
    private var restTime: Long = 2
    private var mRestTimerIsRunning : Boolean = false
    private var mExerciseTimerIsRunning :Boolean = false
    private var exerciseTime: Long = 2
    private var exerciseTImer: CountDownTimer?=null
    private var exerciseProgress = 0
    private var restProgress = 0
    private var tts: TextToSpeech? = null
    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = 0
    private var player: MediaPlayer? = null
    private var exerciseStatusAdapter : ExerciseStatusAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExercisesBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarExercise)
        tts = TextToSpeech(this, this)
        //set up go Back Button

        if(supportActionBar != null)
        {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolbarExercise?.setNavigationOnClickListener{
            customDialogForBackButton()
        }

        //get the exerciseList
        exerciseList = Contants.defaultExercisesList()
        setUpExerciseStatusRecyclerView()
        setupRestView()
    }

    private fun setUpExerciseStatusRecyclerView()
    {
        exerciseStatusAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding!!.rvExerciseStatus.adapter = exerciseStatusAdapter
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS)
        {
            // set US English as language for tts
            val result = tts?.setLanguage(Locale.US)
            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
            {
                Toast.makeText(
                    this@ExercisesActivity,
                    "Your language is not supported",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun setupRestView() {
        binding?.ivImage?.visibility = View.GONE
        binding?.tvUpcomingExercise?.text = exerciseList!![currentExercisePosition].getName()

        binding?.tvUpcomingExercise?.visibility = View.VISIBLE
        binding?.tvUpcomingExerciseLabel?.visibility = View.VISIBLE
        binding?.tvTitle?.text = "Get Ready For"
        try {
            val soundURI =
                Uri.parse("android.resource://com.example.a7minsworkout/" + R.raw.press_start)
            player = MediaPlayer.create(applicationContext, soundURI)
            player?.isLooping = false // Sets the player to be looping or non-looping.
            player?.start() // Starts Playback.
        } catch (e: Exception) {
            e.printStackTrace()
        }

        /**mer is running the and it is not null then cancel the running timer and start the new one.
         * Here firstly we will check if the ti
         * And set the progress to initial which is 0.
         */
        binding?.ivImage?.visibility = View.GONE
        if (restTimer != null) {
            restTimer!!.cancel()
            restProgress = 0
        }

        starTimer()

    }

    private fun setUpExerciseView()

    {
        binding?.tvUpcomingExercise?.visibility = View.GONE
        binding?.tvUpcomingExerciseLabel?.visibility = View.GONE
        binding?.ivImage?.visibility = View.VISIBLE
        binding?.ivImage?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvTitle?.text = (exerciseList!![currentExercisePosition].getName())

        if (exerciseTImer != null) {
            exerciseTImer!!.cancel()
            exerciseProgress = 0
        }

        startExerciseTimer()
    }

    private fun starTimer() {
        mRestTimerIsRunning = true
        restProgress = 0
        binding?.progressBar?.max = restTime.toInt()
        binding?.progressBar?.progress = restProgress

        restTimer = object : CountDownTimer(restTime*1000,1000)

        {
            override fun onTick(millisUntilFinished: Long) {

                restProgress++
                binding?.tvTimer?.text = (restTime - restProgress).toString()
                binding?.progressBar?.progress =  restTime.toInt() - restProgress
                //wait a bit for tts initialize
                if(restProgress == 3)
                {
                    var speakString = exerciseList!![currentExercisePosition].getName() + "is coming"
                    speakout(speakString)
                }
            }

            override fun onFinish() {
                mRestTimerIsRunning = false
                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseStatusAdapter!!.notifyDataSetChanged()
                setUpExerciseView()
            }
        }.start()
    }



    private fun startExerciseTimer()
    {
        mExerciseTimerIsRunning = true
        binding?.progressBar?.max = exerciseTime.toInt()
        exerciseProgress = 0
        binding?.progressBar?.progress = exerciseProgress
        exerciseTImer = object : CountDownTimer(exerciseTime*1000, 1000)
        {
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                binding?.tvTimer?.text = (exerciseTime - exerciseProgress).toString()
                binding?.progressBar?.progress = exerciseTime.toInt() - exerciseProgress
            }
            override fun onFinish() {
                mExerciseTimerIsRunning = false
                exerciseList!![currentExercisePosition].setIsSelected(false)
                exerciseList!![currentExercisePosition].setIsCompleted(true)
                exerciseStatusAdapter!!.notifyDataSetChanged()
                if(currentExercisePosition!! < exerciseList!!.size - 1) {
                    currentExercisePosition++
                    setupRestView()
                } else {
                    speakout("Congrats, You have completed 7 Mins Workout")
                    finish()
                    showActivityFinish()
                }
            }

        }.start()
    }
    public override fun onDestroy() {
        if (restTimer != null) {
            restTimer?.cancel()
            restProgress = 0

        }


        if (tts != null) {
                tts!!.stop()
                tts!!.shutdown()
        }

        if(player != null)
        {
            player!!.stop()
        }

        super.onDestroy()
        binding = null
    }

    private fun speakout(text: String) {
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    private fun showActivityFinish()
    {
        startActivity(
            Intent(this,FinishActivity::class.java)
        )
    }

    private fun customDialogForBackButton()
    {
        val dialogbinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        val customDialog = Dialog(this)
        customDialog.setContentView(dialogbinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        customDialog.show()
        dialogbinding.tvYes.setOnClickListener {
            this.finish()
            player?.stop()
            customDialog.dismiss()
        }
        dialogbinding.tvNo.setOnClickListener {
            customDialog.dismiss()
        }
    }


}