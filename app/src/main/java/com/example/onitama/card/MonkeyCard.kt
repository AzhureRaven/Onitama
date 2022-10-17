package com.example.onitama.card

class MonkeyCard: Card() {
    init {
        this.name = "Monkey"
        this.x = arrayListOf(-1, 1, -1, 1)
        this.y = arrayListOf(-1, -1, 1, 1)
        this.img = "monkey"
        this.size = x.size
    }
}