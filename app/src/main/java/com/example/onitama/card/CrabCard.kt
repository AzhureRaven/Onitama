package com.example.onitama.card

class CrabCard:Card() {
    init {
        this.name = "Crab"
        this.x = arrayListOf(-2, 0, 2)
        this.y = arrayListOf(0, -1, 0)
        this.img = "crab"
        this.size = x.size
        this.stamp = "P1"
    }
}