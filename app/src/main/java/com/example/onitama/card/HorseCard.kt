package com.example.onitama.card

class HorseCard : Card() {
    init {
        this.name = "Horse"
        this.x = arrayListOf(-1, 0, 0)
        this.y = arrayListOf(0, -1, 1)
        this.img = ""
        this.size = x.size
    }
}