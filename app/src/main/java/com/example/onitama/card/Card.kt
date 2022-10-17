package com.example.onitama.card

open class Card() {
    lateinit var name: String
    lateinit var x:ArrayList<Int>
    lateinit var y:ArrayList<Int>
    lateinit var img:String
    var size = 0
    override fun toString(): String {
        return "Card(name='$name', x=$x, y=$y, img='$img', size=$size)"
    }


}

