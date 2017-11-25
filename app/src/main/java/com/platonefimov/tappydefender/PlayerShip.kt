package com.platonefimov.tappydefender

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class PlayerShip(context: Context?) {

    val bitmap: Bitmap = BitmapFactory.decodeResource(context?.resources, R.drawable.ship)
    var x: Float = 50f
        private set
    var y: Float = 50f
        private set
    private val speed: Float = 1f

    fun update() {
        x += speed
    }


}
