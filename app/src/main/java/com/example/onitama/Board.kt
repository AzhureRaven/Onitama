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
}

//card1 = card2.also {card2 = card1}