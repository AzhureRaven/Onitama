package com.example.onitama.card

class BoarCard: Card() {
    init {
        this.name = "Boar"
        this.x = arrayListOf(0, -1, 1)
        this.y = arrayListOf(-1, 0, 0)
        this.img = "boar"
        this.size = x.size
        this.stamp = "P2"
    }
}