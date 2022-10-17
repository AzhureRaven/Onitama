package com.example.onitama.card

class EelCard: Card() {
    init {
        this.name = "Eel"
        this.x = arrayListOf(-1, -1, 1)
        this.y = arrayListOf(-1, 1, 0)
        this.img = "eel"
        this.size = x.size
    }
}