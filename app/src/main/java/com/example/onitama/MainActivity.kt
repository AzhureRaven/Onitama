package com.example.onitama

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
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
    var mode = "P2"
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
        board = Board(b, arrayListOf(c[0],c[1]),arrayListOf(c[2],c[3]),c[4],c[4].stamp)

        print_to_board()
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
            iv_player2_card1.setBackgroundColor(resources.getColor(R.color.p2))
            iv_player2_card2.setBackgroundColor(resources.getColor(R.color.p2))
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
                if(j == x && i == y) return counter
                else counter++
            }
        }
        return -1
    }

    fun pickCard(v: View){
        //highlight dan pilih kartu
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

    var tileP = -1
    var highlightedTiles = ArrayList<Button>()

    fun clickTile(v: View){
        if(pickedCard > -1){ //lakukan move
            var button = findViewById<Button>(v.id)
            var lokasi = button.tag.toString().split(",")//tag: y,x
            if(highlightedTiles.contains(button)){
                //Toast.makeText(this, "highlight", Toast.LENGTH_SHORT).show()
                val newB = nextPlace(board.board,lokasi[1].toInt(),lokasi[0].toInt())
                val curCard = pickedCard
                pickedCard = 1 - pickedCard
                if(board.turn == "P1"){
                    board = Board(newB, arrayListOf(board.cardP1[pickedCard],board.cardM),
                        arrayListOf(board.cardP2[0],board.cardP2[1]),board.cardP1[curCard],"P2")
                }
                else{
                    board = Board(newB, arrayListOf(board.cardP1[0],board.cardP1[1]),
                        arrayListOf(board.cardP2[pickedCard],board.cardM),
                        board.cardP2[curCard],"P1")
                }
                print_to_board()
                tileP = -1
                cekMenang(lokasi[1].toInt(),lokasi[0].toInt())
                cekLegal()
                pickedCard = -1
            }
            else{ //highlight legal tiles
                colorTile()
                highlightedTiles = ArrayList<Button>()
                if(board.turn == "P1"){
                    if(button.text == "M1" || button.text == "S1"){
                        highlight(board.cardP1[pickedCard],lokasi[1].toInt(),lokasi[0].toInt(),"1")
                        tileP = getTile(lokasi[1].toInt(),lokasi[0].toInt())
                    }
                }
                else{
                    if(button.text == "M2" || button.text == "S2"){
                        highlight(board.cardP2[pickedCard],lokasi[1].toInt(),lokasi[0].toInt(),"2")
                        tileP = getTile(lokasi[1].toInt(),lokasi[0].toInt())
                    }
                }
            }
        }
        else{
            Toast.makeText(this, "Pick Card First", Toast.LENGTH_SHORT).show()
            colorTile()
        }
    }

    fun highlight(card: Card, x:Int, y:Int, p: String){
        //lakukan highlight dan masukkan button ke highligted button
        var flip = 1
        if(p == "2") flip = -1
        for (i in 0..card.size-1){
            var ctr = getTile(x+card.x[i]*flip,y+card.y[i]*flip)
            if(ctr > -1 && tiles[ctr].text != "M$p" && tiles[ctr].text != "S$p") {
                tiles[ctr].setBackgroundColor(resources.getColor(R.color.highlight))
                highlightedTiles.add(tiles[ctr])
            }
        }
    }

    fun nextPlace(b: ArrayList<ArrayList<String>>, x:Int, y:Int):ArrayList<ArrayList<String>>{
        //gerakan pawn ke tile yang dipilih
        var curTile = tiles[tileP].tag.toString().split(",")
        var xc = curTile[1].toInt()
        var yc = curTile[0].toInt()
        var newB =  ArrayList<ArrayList<String>>(b)
        newB[y][x] = newB[yc][xc]
        newB[yc][xc] = " "
        return newB
    }

    fun cekMenang(x:Int, y:Int){
        //lakukan cek kondisi menang
        var kondisi = board.cekKondisi()
        if(kondisi == "P1" || kondisi == "P2"){
            tiles[getTile(x,y)].setBackgroundColor(resources.getColor(R.color.teal_200))
            Handler().postDelayed({
                val intent = Intent(this, ResultActivity::class.java)
                if(kondisi == "P2" && mode=="AI") kondisi = "AI"
                intent.putExtra("win", kondisi)
                startActivity(intent)
            },2000)
        }
    }

    fun cekLegal(){
        //lakukan pengecekan apabila turn player masih ada move legal
        if(!board.cekLegal()){
            Toast.makeText(this, "No Legal Moves! Next Turn...", Toast.LENGTH_SHORT).show()
            Handler().postDelayed({
                if(board.turn == "P1"){
                    board.turn = "P2"
                }
                else{
                    board.turn = "P1"
                }
                print_to_board()
            },1500)
        }
    }

    override fun onRestart() {
        //waktu finish kembali sini ulangi game
        super.onRestart()
        startGame()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.optRestart->{
                startGame()
            }
            R.id.optPlayer->{
                mode = "P2"
                startGame()
            }
            R.id.optAI->{
                mode = "AI"
                startGame()
            }
            else->{
            }
        }
        return super.onOptionsItemSelected(item)
    }

}