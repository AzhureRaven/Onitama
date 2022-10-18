package com.example.onitama

import com.example.onitama.card.*

class Board(
    var board: ArrayList<ArrayList<String>>,//array 2d papan
    var cardP1: ArrayList<Card>,//array 2 kartu dipengang player 1
    var cardP2: ArrayList<Card>,//array 2 kartu dipengang player 2
    var cardM: Card,//kartu di sebelah papan
    var turn: String//turn player sekarang
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

}

//card1 = card2.also {card2 = card1}