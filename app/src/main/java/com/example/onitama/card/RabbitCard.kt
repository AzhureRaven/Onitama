package com.example.onitama.card

class RabbitCard: Card() {
    init {
        this.name = "Rabbit"
        this.x = arrayListOf(-1, 1, 2)
        this.y = arrayListOf(1, -1, 0)
        this.img = ""
        this.size = x.size
    }
}