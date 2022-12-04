package com.example.onitama

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.onitama.card.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    //lateinit var card:Card
    lateinit var iv_player2_card1: ImageView
    lateinit var iv_player2_card2: ImageView
    lateinit var iv_player1_card1: ImageView
    lateinit var iv_player1_card2: ImageView
    lateinit var iv_middle_card: ImageView
    lateinit var board: Board
    val tiles:ArrayList<Button> = ArrayList()
    var mode = "P2"
    var ply = 3
    var game = true
    val coroutine = CoroutineScope(Dispatchers.IO)
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
        for(i in 1..5){
            for(j in 1..5){
                val tileID = "x$j"+"y$i"
                val resourcesID = this.resources.getIdentifier(tileID, "id", packageName)
                tiles.add(findViewById(resourcesID))
            }
        }
        //Toast.makeText(this, "${tiles.size}", Toast.LENGTH_SHORT).show()

        mode = intent.getStringExtra("mode").toString();
        ply = intent.getIntExtra("ply",3).toString().toInt();
        startGame()
        //card = HorseCard()
        //Toast.makeText(this, card.toString(), Toast.LENGTH_LONG).show()
    }

    var choiceAI: ArrayList<Board> = ArrayList()
    fun AIMove(){
        if(mode == "AI" && board.turn == "P2" && game){
            Toast.makeText(this, "AI Moving", Toast.LENGTH_SHORT).show()
            coroutine.launch {
                choiceAI.clear()
                val choice = minimaxab(board,-999 - (ply - 1),999 + (ply - 1),ply)
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "$choice", Toast.LENGTH_SHORT).show()
                    var histTile1=0
                    var histTile2=0
                    //val choices = ArrayList<Board>()
                    for(ch in choiceAI){
                        if(ch.sbe == choice){
                            //choices.add(ch)
                            pickedCard = ch.histCard
                            histTile1 = ch.histTile1
                            histTile2 = ch.histTile2
                            //copy(ch)
                            break
                        }
                    }
                    /*val pick = (0..(choices.size-1)).random()
                    pickedCard = choices[pick].histCard
                    histTile1 = choices[pick].histTile1
                    histTile2 = choices[pick].histTile2*/
                    choiceAI.clear()

                    //lakukan simulated click
                    val handler = Handler()
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    handler.postDelayed(Runnable {
                        colorCard()
                    }, 750)
                    handler.postDelayed(Runnable {
                        if(histTile1>-1 && histTile1<tiles.size) tiles[histTile1].performClick()
                    }, 1750)
                    handler.postDelayed(Runnable {
                        if(histTile2>-1 && histTile2<tiles.size) tiles[histTile2].performClick()
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    }, 2750)
                }
            }
        }
    }

    fun copy(ch:Board){
        var newB = arrayListOf<ArrayList<String>>(
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
        for(i in 0..8){
            for(j in 0..8){
                newB[i][j] = ch.board[i][j]
            }
        }
        board = Board(newB, arrayListOf(ch.cardP1[0],ch.cardP1[1]), arrayListOf(ch.cardP2[0],ch.cardP2[1]), ch.cardM, ch.turn)
    }

    fun minimaxab(board: Board, a:Int, b:Int, ply:Int): Int{
        //val board = Board(bd.board,bd.cardP1,bd.cardP2,bd.cardM,bd.turn,bd.histCard,bd.histTile,bd.sbe)
        var alpha = a
        var beta = b
        //cek jika terminal node
        val terminal = board.cekKondisi()
        if(terminal == "P1") return -999 - ply
        else if(terminal == "P2") return 999 + ply
        else if(ply <= 0) return board.sbe()
        else{
            //buka node
            var noMove = true
            if(board.turn == "P1"){ //minimizing
                //mulai dari setiap master student
                for (i in 2..6) {
                    for (j in 2..6) {
                        if(board.board[i][j] == "M1" || board.board[i][j] == "S1"){
                            //telusuri kedua card
                            for(c in 0..1){
                                //telusuri setiap legal move card
                                for (m in 0..board.cardP1[c].size-1){
                                    val newX = j + board.cardP1[c].x[m]
                                    val newY = i + board.cardP1[c].y[m]
                                    if(board.board[newY][newX] != "N" && board.board[newY][newX] != "M1" && board.board[newY][newX] != "S1") {
                                        //lakukan expand node
                                        noMove = false
                                        val newB = nextPlace(board.board,newX,newY,j,i)
                                        var pickedCard = c
                                        val curCard = pickedCard
                                        pickedCard = 1 - pickedCard
                                        val newBoard = Board(newB, arrayListOf(board.cardP1[pickedCard],board.cardM),
                                                arrayListOf(board.cardP2[0],board.cardP2[1]),board.cardP1[curCard],"P2",curCard,getTile(j,i),getTile(newX,newY))
                                        val value = minimaxab(newBoard,alpha,beta,ply-1)
                                        beta = min(beta,value)
                                        if(ply == this.ply){
                                            newBoard.sbe = alpha
                                            choiceAI.add(newBoard)
                                        }
                                        if(beta <= alpha) return beta
                                    }
                                }
                            }
                        }
                    }
                }
                if(noMove){ //jika tidak ada legal move, buka node ke turn berikutnya
                    val newBoard = Board(board.board, arrayListOf(board.cardP1[0],board.cardP1[1]),
                        arrayListOf(board.cardP2[0],board.cardP2[1]),board.cardM,"P2")
                    val value = minimaxab(newBoard,alpha,beta,ply-1)
                    beta = min(beta,value)
                    if(beta <= alpha) return beta
                }
                return beta
            }
            else{ //maximizing
                //mulai dari setiap master student
                for (i in 2..6) {
                    for (j in 2..6) {
                        if(board.board[i][j] == "M2" || board.board[i][j] == "S2"){
                            //telusuri kedua card
                            for(c in 0..1){
                                //telusuri setiap legal move card
                                for (m in 0..board.cardP2[c].size-1){
                                    val newX = j + board.cardP2[c].x[m]*-1
                                    val newY = i + board.cardP2[c].y[m]*-1
                                    if(board.board[newY][newX] != "N" && board.board[newY][newX] != "M2" && board.board[newY][newX] != "S2") {
                                        //lakukan expand node
                                        noMove = false
                                        val newB = nextPlace(board.board,newX,newY,j,i)
                                        var pickedCard = c
                                        val curCard = pickedCard
                                        pickedCard = 1 - pickedCard

                                        val newBoard = Board(newB, arrayListOf(board.cardP1[0],board.cardP1[1]),
                                            arrayListOf(board.cardP2[pickedCard],board.cardM),
                                            board.cardP2[curCard],"P1",curCard,getTile(j,i),getTile(newX,newY))
                                        val value = minimaxab(newBoard,alpha,beta,ply-1)
                                        alpha = max(alpha,value)
                                        if(ply == this.ply){
                                            newBoard.sbe = alpha
                                            choiceAI.add(newBoard)
                                        }

                                        if(beta <= alpha) return alpha
                                    }
                                }

                            }
                        }
                    }
                }
                if(noMove){ //jika tidak ada legal move, buka node ke turn berikutnya
                    val newBoard = Board(board.board, arrayListOf(board.cardP1[0],board.cardP1[1]),
                        arrayListOf(board.cardP2[0],board.cardP2[1]),board.cardM,"P2")
                    val value = minimaxab(newBoard,alpha,beta,ply-1)
                    alpha = max(beta,value)
                    if(beta <= alpha) return alpha
                }
                return alpha
            }
        }
        return 0
    }

    fun printBoard(b: ArrayList<ArrayList<String>>){
        for (i in 2..6) {
            for (j in 2..6) {
                if(b[i][j] == " ") print("  ")
                else print(b[i][j])
            }
            println()
        }
    }

    fun startGame(){
        //function untuk start game
        game = true
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
        AIMove()
    }
    fun print_to_board(){
        //fungsi untuk print board ke UI

        //print board
        var counter=0
        for (i in 2..6) {
            for (j in 2..6) {
                val tile = board.board[i][j]
                when(tile){
                    "M1" -> {
                        tiles.get(counter).setText("♝")
                        tiles.get(counter).setTextColor(resources.getColor(R.color.d1))
                    }
                    "S1" -> {
                        tiles.get(counter).setText("♟")
                        tiles.get(counter).setTextColor(resources.getColor(R.color.d1))
                    }
                    "M2" -> {
                        tiles.get(counter).setText("♝")
                        tiles.get(counter).setTextColor(resources.getColor(R.color.d2))
                    }
                    "S2" -> {
                        tiles.get(counter).setText("♟")
                        tiles.get(counter).setTextColor(resources.getColor(R.color.d2))
                    }
                    else -> {
                        tiles.get(counter).setText("")
                        tiles.get(counter).setTextColor(resources.getColor(R.color.black))
                    }
                }
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
        if(game && (board.turn =="P1" || mode=="P2")){
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
    }

    fun colorCard(){
        printTurn()
        highlightedTiles.clear()
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
    val highlightedTiles = ArrayList<Button>()

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
            }
            else{ //highlight legal tiles
                colorTile()
                highlightedTiles.clear()
                val tile = board.board[lokasi[0].toInt()][lokasi[1].toInt()]
                if(board.turn == "P1"){
                    if(tile == "M1" || tile == "S1"){
                        highlight(board.cardP1[pickedCard],lokasi[1].toInt(),lokasi[0].toInt(),"1")
                        tileP = getTile(lokasi[1].toInt(),lokasi[0].toInt())
                    }
                }
                else{
                    if(tile == "M2" || tile == "S2"){
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
        highlightedTiles.clear()
        var flip = 1
        var ctr = 0
        var tile = ""
        if(p == "2") flip = -1
        for (i in 0..card.size-1){
            val mx = x+card.x[i]*flip
            val my = y+card.y[i]*flip
            ctr = getTile(mx,my)
            tile = board.board[my][mx]
            if(ctr > -1 && tile != "M$p" && tile != "S$p") {
                tiles[ctr].setBackgroundColor(resources.getColor(R.color.highlight))
                highlightedTiles.add(tiles[ctr])
            }
        }
        //higlight self
        ctr = getTile(x,y)
        tiles[ctr].setBackgroundColor(resources.getColor(R.color.self))
    }

    fun nextPlace(b: ArrayList<ArrayList<String>>, x:Int, y:Int):ArrayList<ArrayList<String>>{
        //gerakan pawn ke tile yang dipilih
        var curTile = tiles[tileP].tag.toString().split(",")
        var xc = curTile[1].toInt()
        var yc = curTile[0].toInt()
        var newB = arrayListOf<ArrayList<String>>(
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
        for(i in 0..8){
            for(j in 0..8){
                newB[i][j] = b[i][j]
            }
        }
        newB[y][x] = newB[yc][xc]
        newB[yc][xc] = " "
        return newB
    }

    fun nextPlace(b: ArrayList<ArrayList<String>>, x:Int, y:Int, xc:Int, yc:Int):ArrayList<ArrayList<String>>{
        //gerakan pawn ke tile yang dipilih buat AI
        var newB = arrayListOf<ArrayList<String>>(
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
        //buat baru, dicopy value b satu persatu. Semua cara lain untuk copy data tetep referens yang original sehingga ikut terubah
        for(i in 0..8){
            for(j in 0..8){
                newB[i][j] = b[i][j]
            }
        }
        //var newB: ArrayList<ArrayList<String>> = arrayListOf(b[0],b[1],b[2],b[3],b[4],b[5],b[6],b[7],b[8])
        newB[y][x] = newB[yc][xc]
        newB[yc][xc] = " "
        return newB
    }

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult ->
        if(result.resultCode == RESULT_OK){
            finish()
        }
        else{
            startGame()
        }
    }

    fun cekMenang(x:Int, y:Int){
        //lakukan cek kondisi menang
        var kondisi = board.cekKondisi()
        if(kondisi == "P1" || kondisi == "P2"){
            game = false
            tiles[getTile(x,y)].setBackgroundColor(resources.getColor(R.color.win))
            Handler().postDelayed({
                val intent = Intent(this, ResultActivity::class.java)
                if(kondisi == "P2" && mode=="AI") kondisi = "AI"
                intent.putExtra("win", kondisi)
                launcher.launch(intent)
            },1500)
        }
        else{
            pickedCard = -1
            highlightedTiles.clear()
            cekLegal()
        }
    }

    var legal = 0
    fun cekLegal(){
        //lakukan pengecekan apabila turn player masih ada move legal
        if(!board.cekLegal()){
            Toast.makeText(this, "No Legal Moves! Next Turn...", Toast.LENGTH_SHORT).show()
            legal++
            if(legal>=2){ //kalau kedua player gak bisa gerak
                game = false
                Toast.makeText(this, "No More Legal Moves! Game Over!", Toast.LENGTH_SHORT).show()
                Handler().postDelayed({
                    val intent = Intent(this, ResultActivity::class.java)
                    intent.putExtra("win", "No One")
                    launcher.launch(intent)
                },1500)
            }
            else{
                Handler().postDelayed({
                    if(board.turn == "P1"){
                        board.turn = "P2"
                    }
                    else{
                        board.turn = "P1"
                    }
                    print_to_board()
                    AIMove()
                },1500)
            }
        }
        else{
            AIMove()
            legal = 0
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.optMenu->{
                finish()
            }
            R.id.optRestart->{
                startGame()
            }
            R.id.optPlayer->{
                mode = "P2"
                startGame()
            }
            R.id.opt1->{
                mode = "AI"
                ply = 1
                startGame()
            }
            R.id.opt2->{
                mode = "AI"
                ply = 2
                startGame()
            }
            R.id.opt3->{
                mode = "AI"
                ply = 3
                startGame()
            }
            R.id.opt4->{
                mode = "AI"
                ply = 4
                startGame()
            }
            R.id.opt5->{
                mode = "AI"
                ply = 5
                startGame()
            }
            R.id.opt6->{
                mode = "AI"
                ply = 6
                startGame()
            }
            R.id.opt7->{
                mode = "AI"
                ply = 7
                startGame()
            }
            R.id.opt8->{
                mode = "AI"
                ply = 8
                startGame()
            }
            else->{
            }
        }
        return super.onOptionsItemSelected(item)
    }

}