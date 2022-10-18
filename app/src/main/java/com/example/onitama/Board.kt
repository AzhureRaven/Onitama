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
        if(board[2][4] == "M1") return "P1" //apakah M1 mencapai tempat M2?

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
        if(board[6][4] == "M2") return "P2" //apakah M1 mencapai tempat M2?

        return "Draw"
    }
}

//card1 = card2.also {card2 = card1}