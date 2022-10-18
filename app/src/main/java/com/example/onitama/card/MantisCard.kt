package com.example.onitama.card

class MantisCard: Card() {
    init {
        this.name = "Mantis"
        this.x = arrayListOf(-1, 1, 0)
        this.y = arrayListOf(-1, -1, 1)
        this.img = "mantis"
        this.size = x.size
        this.stamp = "P2"
    }
}