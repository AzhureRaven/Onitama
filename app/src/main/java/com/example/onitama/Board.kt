package com.example.onitama

import com.example.onitama.card.*
import java.lang.Math.abs

class Board(
    var board: ArrayList<ArrayList<String>>,//array 2d papan
    var cardP1: ArrayList<Card>,//array 2 kartu dipengang player 1
    var cardP2: ArrayList<Card>,//array 2 kartu dipengang player 2
    var cardM: Card,//kartu di sebelah papan
    var turn: String,//turn player sekarang
    var histCard: Int = -1,//index card yang dipilih untuk mendapatkan state Board ini
    var histTile1: Int = -1,//index tile yang dipilih untuk mendapatkan state Board ini
    var histTile2: Int = -1,//index tile next yang dipilih untuk mendapatkan state Board ini
    var sbe:Int = 0//nilai simpanan sbe
) {

    override fun toString(): String {
        return "Board(board=$board, cardP1=$cardP1, cardP2=$cardP2, cardM=$cardM, turn='$turn')"
    }

    fun nextTurn(){
        if(this.turn == "P1") this.turn == "P2"
        else this.turn == "P1"
    }

    fun cekKondisi(): String{
        //cek siapa menang

        //apakah p1 menang?
        var isAlive = false
        for (i in 2..6) {
            for (j in 2..6) {
               //apakah master p2 mati?
               if(this.board[i][j] == "M2"){
                   isAlive = true
                   break
               }
            }
        }
        if(!isAlive) return "P1"
        if(board[2][4] == "M1") return "P1" //apakah M1 mencapai temple M2?

        //apakah p2 menang?
        isAlive = false
        for (i in 2..6) {
            for (j in 2..6) {
                //apakah master p2 mati?
                if(this.board[i][j] == "M1"){
                    isAlive = true
                    break
                }
            }
        }
        if(!isAlive) return "P2"
        if(board[6][4] == "M2") return "P2" //apakah M1 mencapai temple M2?

        return "Draw"
    }

    fun cekLegal(): Boolean{
        //cek apa player masih bisa gerak
        var ctr = 0
        if(turn == "P1"){
            for (i in 2..6) {
                for (j in 2..6) {
                    if(this.board[i][j] == "M1" || this.board[i][j] == "S1"){
                        ctr += legals(cardP1[0],j,i,"1")
                        ctr += legals(cardP1[1],j,i,"1")
                        if(ctr > 0){
                            return true
                        }
                    }
                }
            }
        }
        else{
            for (i in 2..6) {
                for (j in 2..6) {
                    if(this.board[i][j] == "M2" || this.board[i][j] == "S2"){
                        ctr += legals(cardP2[0],j,i,"2")
                        ctr += legals(cardP2[1],j,i,"2")
                        if(ctr > 0){
                            return true
                        }
                    }
                }
            }
        }
        return false
    }

    fun legals(card: Card, x:Int, y:Int, p: String): Int{
        //lakukan cek legal moves
        var flip = 1
        if(p == "2") flip = -1
        for (i in 0..card.size-1){
            if(board[y+card.y[i]*flip][x+card.x[i]*flip] != "M$p" && board[y+card.y[i]*flip][x+card.x[i]*flip] != "S$p"){
                return 1
            }
        }
        return 0
    }

    fun sbe(): Int{
        //lakukan sbe
        //fitur 1: manhattan distance master ke temple enemy
        //P2
        var f1p2 = 0
        var mx2 = -1
        var my2 = -1
        for (i in 2..6) {
            for (j in 2..6) {
                if(this.board[i][j] == "M2"){
                    mx2 = j
                    my2 = i
                    break
                }
            }
        }
        f1p2 = abs(6 - my2) + abs(4 - mx2)

        //P1
        var f1p1 = 0
        var mx1 = -1
        var my1 = -1
        for (i in 2..6) {
            for (j in 2..6) {
                if(this.board[i][j] == "M1"){
                    mx1 = j
                    my1 = i
                    break
                }
            }
        }
        f1p1 = abs(2 - my1) + abs(4 - mx1)

        //fitur 2: jumlah student
        //fitur 3: average manhattan distance student ke enemy master
        var f2p2 = 0
        var f2p1 = 0
        var f3p2 = 0
        var f3p1 = 0
        for (i in 2..6) {
            for (j in 2..6) {
                if(this.board[i][j] == "S2"){
                    f2p2++
                    f3p2 += abs(my1 - i) + abs(mx1 - j)
                }
                else if(this.board[i][j] == "S1"){
                    f2p1++
                    f3p1 += abs(my2 - i) + abs(mx2 - j)
                }
                if(f2p2 == 5 && f2p1 == 5) break
            }
            if(f2p2 == 5 && f2p1 == 5) break
        }

        //menghitung average distance student-enemy master. Jika tidak ada student, otomatis highest averga (8)
        if(f2p1 > 0)f3p1 /= f2p1 else f3p1 = 8
        if(f2p2 > 0)f3p2 /= f2p2 else f3p2 = 8

        //apply weight dan return value
        return (f1p1 - f1p2)*2 + (f2p2*6 - f2p1*2) + (f3p1 - f3p2)*8
    }


}

//card1 = card2.also {card2 = card1}