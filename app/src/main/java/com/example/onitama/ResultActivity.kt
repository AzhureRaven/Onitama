package com.example.onitama

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class ResultActivity : AppCompatActivity() {

    lateinit var tvWin : TextView
    lateinit var btnPlay: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        tvWin = findViewById(R.id.tvWin)
        btnPlay = findViewById(R.id.btnPlay)
        val win = intent.getStringExtra("win")
        tvWin.text = "$win Wins!"

        btnPlay.setOnClickListener {
            finish()
        }
    }
}