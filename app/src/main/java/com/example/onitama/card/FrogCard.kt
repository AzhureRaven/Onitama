package com.example.onitama.card

class FrogCard: Card() {
    init {
        this.name = "Frog"
        this.x = arrayListOf(-2, -1, 1)
        this.y = arrayListOf(0, -1, 1)
        this.img = "frog"
        this.size = x.size
    }
}