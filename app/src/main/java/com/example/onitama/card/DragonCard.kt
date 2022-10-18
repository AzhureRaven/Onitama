package com.example.onitama.card

class DragonCard: Card() {
    init {
        this.name = "Dragon"
        this.x = arrayListOf(-2, 2, -1, 1)
        this.y = arrayListOf(-1, -1, 1, 1)
        this.img = "dragon"
        this.size = x.size
        this.stamp = "P2"
    }
}