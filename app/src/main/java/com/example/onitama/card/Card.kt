package com.example.onitama.card

open class Card() {
    lateinit var name: String//nama kartu
    lateinit var x:ArrayList<Int>
    lateinit var y:ArrayList<Int>
    //x y array pergerakan yang dimungkinkan oleh kartu
    lateinit var img:String//isi string referens image kartu
    var size = 0//simpanan ukuran x y
    override fun toString(): String {
        return "Card(name='$name', x=$x, y=$y, img='$img', size=$size)"
    }


}

