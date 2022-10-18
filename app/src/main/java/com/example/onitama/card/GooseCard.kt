package com.example.onitama.card

class GooseCard: Card() {
    init {
        this.name = "Goose"
        this.x = arrayListOf(-1, -1, 1, 1)
        this.y = arrayListOf(-1, 0, 0, 1)
        this.img = "goose"
        this.size = x.size
        this.stamp = "P1"
    }
}