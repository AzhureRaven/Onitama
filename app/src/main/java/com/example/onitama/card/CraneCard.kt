package com.example.onitama.card

class CraneCard: Card() {
    init {
        this.name = "Crane"
        this.x = arrayListOf(0, -1, 1)
        this.y = arrayListOf(-1, 1, 1)
        this.img = ""
        this.size = x.size
    }
}