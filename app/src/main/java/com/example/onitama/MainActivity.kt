package com.example.onitama

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.example.onitama.card.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    //lateinit var card:Card
    lateinit var iv_player2_card1: ImageView
    lateinit var iv_player2_card2: ImageView
    lateinit var iv_player1_card1: ImageView
    lateinit var iv_player1_card2: ImageView
    lateinit var iv_middle_card: ImageView
    lateinit var board: Board
    lateinit var tiles:ArrayList<Button>
    //lateinit var linearP1: LinearLayout
    //lateinit var linearP2: LinearLayout

    var pickedCard = -1 //pilih kartu yang mana sesuai turn
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        iv_player1_card1 = findViewById(R.id.iv_player1_card1)
        iv_player1_card2 = findViewById(R.id.iv_player1_card2)
        iv_player2_card1 = findViewById(R.id.iv_player2_card1)
        iv_player2_card2 = findViewById(R.id.iv_player2_card2)
        iv_middle_card = findViewById(R.id.iv_middle_card)
        //linearP1 = findViewById(R.id.linearLayout3)
        //linearP2 = findViewById(R.id.linearLayout2)
        tiles= ArrayList()
        for(i in 1..5){
            for(j in 1..5){
                val tileID = "x$j"+"y$i"
                val resourcesID = this.resources.getIdentifier(tileID, "id", packageName)
                tiles.add(findViewById(resourcesID))
            }
        }

        startGame()
        print_to_board()
        //card = HorseCard()
        //Toast.makeText(this, card.toString(), Toast.LENGTH_LONG).show()
    }
    fun startGame(){
        //function untuk start game
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
        val randomGenerator = Random(System.currentTimeMillis())
        val c = ArrayList<Card>()
        for (i in 1..5){
            val idx = randomGenerator.nextInt(0, cards.size)
            c.add(cards.get(idx))
            cards.removeAt(idx)
        }
        board = Board(b, arrayListOf(c[0],c[1]),arrayListOf(c[2],c[3]),c[4],"P1")
    }
    fun print_to_board(){
        //fungsi untuk print board ke UI

        //print board
        var counter=0
        for (i in 2..6) {
            for (j in 2..6) {
                tiles.get(counter).setText(board.board[i][j])
                counter++
            }
        }
        colorTile()

        //print cards
        var resourcesID = this.resources.getIdentifier(board.cardP1[0].img, "drawable", packageName)
        iv_player1_card1.setImageResource(resourcesID)
        resourcesID = this.resources.getIdentifier(board.cardP1[1].img, "drawable", packageName)
        iv_player1_card2.setImageResource(resourcesID)
        resourcesID = this.resources.getIdentifier(board.cardP2[0].img, "drawable", packageName)
        iv_player2_card1.setImageResource(resourcesID)
        resourcesID = this.resources.getIdentifier(board.cardP2[1].img, "drawable", packageName)
        iv_player2_card2.setImageResource(resourcesID)
        resourcesID = this.resources.getIdentifier(board.cardM.img, "drawable", packageName)
        iv_middle_card.setImageResource(resourcesID)

        //print turn
        printTurn()
    }

    fun printTurn(){
        //print highlight turn siapa
        if(board.turn == "P1"){
            iv_player1_card1.setBackgroundColor(resources.getColor(R.color.p1))
            iv_player1_card2.setBackgroundColor(resources.getColor(R.color.p1))
            iv_player2_card1.setBackgroundColor(resources.getColor(R.color.background))
            iv_player2_card2.setBackgroundColor(resources.getColor(R.color.background))
        }
        else{
            iv_player1_card1.setBackgroundColor(resources.getColor(R.color.p2))
            iv_player1_card2.setBackgroundColor(resources.getColor(R.color.p2))
            iv_player1_card1.setBackgroundColor(resources.getColor(R.color.background))
            iv_player1_card2.setBackgroundColor(resources.getColor(R.color.background))
        }
    }

    fun colorTile(){
        //color tile default
        var counter=0
        for (i in 2..6) {
            for (j in 2..6) {
                if(counter == 2) tiles.get(counter).setBackgroundColor(resources.getColor(R.color.p2))
                else if (counter == 22) tiles.get(counter).setBackgroundColor(resources.getColor(R.color.p1))
                else tiles.get(counter).setBackgroundColor(resources.getColor(R.color.normal))
                counter++
            }
        }
    }

    fun getTile(x: Int, y: Int): Int{
        //mendapatkan index tile dari koordinat di board.board
        var counter=0
        for (i in 2..6) {
            for (j in 2..6) {
                if(i == x && j == y) return counter
                else counter++
            }
        }
        return -1
    }

    fun pickCard(v: View){
        var card = findViewById<ImageView>(v.id)
        if(board.turn == "P1"){
            if(card.id == iv_player1_card1.id){
                pickedCard = 0
            }
            else if(card.id == iv_player1_card2.id){
                pickedCard = 1
            }
            else{
                pickedCard = -1
                Toast.makeText(this, "Not P2 Turn", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            if(card.id == iv_player2_card1.id){
                pickedCard = 0
            }
            else if(card.id == iv_player2_card2.id){
                pickedCard = 1
            }
            else{
                pickedCard = -1
                Toast.makeText(this, "Not P1 Turn", Toast.LENGTH_SHORT).show()
            }
        }
        colorCard()
        colorTile()
    }

    fun colorCard(){
        printTurn()
        if(board.turn == "P1"){
            if(pickedCard == 0){
                iv_player1_card1.setBackgroundColor(resources.getColor(R.color.highlight))
            }
            else if(pickedCard == 1){
                iv_player1_card2.setBackgroundColor(resources.getColor(R.color.highlight))
            }
        }
        else{
            if(pickedCard == 0){
                iv_player2_card1.setBackgroundColor(resources.getColor(R.color.highlight))
            }
            else if(pickedCard == 1){
                iv_player2_card2.setBackgroundColor(resources.getColor(R.color.highlight))
            }
        }
    }

    fun clickTile(v: View){
        colorTile()
        if(pickedCard > -1){
            var button = findViewById<Button>(v.id)
            var lokasi = button.tag.toString().split(",")
            var tileP = getTile(lokasi[0].toInt(),lokasi[1].toInt())
            if(board.turn == "P1"){
                if(button.text == "M1" || button.text == "S1"){

                }
            }
            else{
                if(button.text == "M2" || button.text == "S2"){

                }
            }
        }
        else{
            Toast.makeText(this, "Pick Card First", Toast.LENGTH_SHORT).show()
        }

    }
}