package com.example.onitama.card

class CobraCard: Card() {
    init {
        this.name = "Cobra"
        this.x = arrayListOf(-1, 1, 1)
        this.y = arrayListOf(0, -1, 1)
        this.img = ""
        this.size = x.size
    }
}