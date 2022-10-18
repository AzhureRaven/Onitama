package com.example.onitama.card

class OxCard: Card() {
    init {
        this.name = "Ox"
        this.x = arrayListOf(0, 0, 1)
        this.y = arrayListOf(-1, 1, 0)
        this.img = "ox"
        this.size = x.size
        this.stamp = "P1"
    }
}