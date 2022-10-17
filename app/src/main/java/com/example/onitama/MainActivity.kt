package com.example.onitama

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.onitama.card.*

class MainActivity : AppCompatActivity() {
    //lateinit var card:Card
    lateinit var board: Board
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startGame()
        //card = HorseCard()
        //Toast.makeText(this, card.toString(), Toast.LENGTH_LONG).show()
    }

    fun startGame(){
        val cards = ArrayList<Card>()
        cards.add(TigerCard())
        cards.add(CrabCard())
        cards.add(MonkeyCard())
        cards.add(CraneCard())
        cards.add(DragonCard())
        cards.add(ElephantCard())
        cards.add(MantisCard())
        cards.add(BoarCard())
        cards.add(FrogCard())
        cards.add(GooseCard())
        cards.add(HorseCard())
        cards.add(EelCard())
        cards.add(RabbitCard())
        cards.add(RoosterCard())
        cards.add(OxCard())
        cards.add(CobraCard())
        val b = arrayListOf<ArrayList<String>>(
            arrayListOf<String>("N","N","N","N","N","N","N","N","N"),
            arrayListOf<String>("N","N","N","N","N","N","N","N","N"),
            arrayListOf<String>("N","N","S2","S2","M2","S2","S2","N","N"),
            arrayListOf<String>("N","N"," "," "," "," "," ","N","N"),
            arrayListOf<String>("N","N"," "," "," "," "," ","N","N"),
            arrayListOf<String>("N","N"," "," "," "," "," ","N","N"),
            arrayListOf<String>("N","N","S1","S1","M1","S1","S1","N","N"),
            arrayListOf<String>("N","N","N","N","N","N","N","N","N"),
            arrayListOf<String>("N","N","N","N","N","N","N","N","N"),
        )
        val c = ArrayList<Card>()
        for (i in 1..5){
            val idx = (0..cards.size-1).random()
            c.add(cards.get(idx))
            cards.removeAt(idx)
        }
        board = Board(b, arrayListOf(c[0],c[1]),arrayListOf(c[2],c[3]),c[4],"P1")
    }
}