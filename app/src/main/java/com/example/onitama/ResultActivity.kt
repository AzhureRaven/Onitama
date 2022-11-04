package com.example.onitama

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class ResultActivity : AppCompatActivity() {

    lateinit var tvWin : TextView
    lateinit var btnPlay: Button
    lateinit var btnMenu: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        tvWin = findViewById(R.id.tvWin)
        btnPlay = findViewById(R.id.btnPlay)
        btnMenu = findViewById(R.id.btnMenu)
        val win = intent.getStringExtra("win")
        tvWin.text = "$win Wins!"

        btnPlay.setOnClickListener {
            finish()
        }

        btnMenu.setOnClickListener {
            val intent = Intent()
            setResult(Activity.RESULT_OK,intent)
            finish()
        }

    }

}