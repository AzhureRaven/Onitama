package com.example.onitama.card

class CraneCard: Card() {
    init {
        this.name = "Crane"
        this.x = arrayListOf(0, -1, 1)
        this.y = arrayListOf(-1, 1, 1)
        this.img = "crane"
        this.size = x.size
        this.stamp = "P1"
    }
}