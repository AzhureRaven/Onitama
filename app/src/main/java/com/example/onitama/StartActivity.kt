package com.example.onitama

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Spinner

class StartActivity : AppCompatActivity() {

    lateinit var btnP2: Button
    lateinit var btnAI: Button
    lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        btnP2 = findViewById(R.id.btnP2)
        btnAI = findViewById(R.id.btnAI)
        spinner = findViewById(R.id.spinner)

        btnP2.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("mode", "P2")
            startActivity(intent)
        }

        btnAI.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("mode", "AI")
            intent.putExtra("ply", spinner.selectedItem.toString().toInt())
            startActivity(intent)
        }
    }
}