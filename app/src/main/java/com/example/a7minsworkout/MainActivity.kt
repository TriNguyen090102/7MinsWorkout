package com.example.a7minsworkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.Toast
import com.example.a7minsworkout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var bindind : ActivityMainBinding? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindind?.root)

        bindind?.flStart?.setOnClickListener{
            startActivity(Intent(this@MainActivity,ExercisesActivity::class.java))
        }

        bindind?.flBMI?.setOnClickListener {
            startActivity(Intent(this@MainActivity,BMI_activity::class.java))
        }
        bindind?.flHistory?.setOnClickListener {
            startActivity(Intent(this@MainActivity,HistoryActivity::class.java))
        }
    }
}