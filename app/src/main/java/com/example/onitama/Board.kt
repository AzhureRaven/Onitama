package com.example.onitama

import com.example.onitama.card.*

class Board(
    var board: ArrayList<ArrayList<String>>,
    var cardP1: ArrayList<Card>,
    var cardP2: ArrayList<Card>,
    var cardM: Card,
    var turn: String
) {

    override fun toString(): String {
        return "Board(board=$board, cardP1=$cardP1, cardP2=$cardP2, cardM=$cardM, turn='$turn')"
    }
}

//card1 = card2.also {card2 = card1}