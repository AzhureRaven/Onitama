package com.example.onitama.card

class RoosterCard: Card() {
    init {
        this.name = "Rooster"
        this.x = arrayListOf(-1, -1, 1, 1)
        this.y = arrayListOf(1, 0, 0, -1)
        this.img = "rooster"
        this.size = x.size
    }
}