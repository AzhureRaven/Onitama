package com.example.onitama.card

class ElephantCard: Card() {
    init {
        this.name = "Elephant"
        this.x = arrayListOf(-1, 1, -1, 1)
        this.y = arrayListOf(-1, -1, 0, 0)
        this.img = "elephant"
        this.size = x.size
    }
}