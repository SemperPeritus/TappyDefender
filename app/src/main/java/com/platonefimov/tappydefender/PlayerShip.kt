package com.platonefimov.tappydefender

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class PlayerShip(context: Context, screenY: Int) {

    val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ship)
    var x = 50
        private set
    var y = 50
        private set

    var speed = 1
        private set
    var boosting = false

    private val gravity = -12

    private var maxY = screenY - bitmap.height
    private var minY = 0

    private val minSpeed = 1
    private val maxSpeed = 20

    fun update() {
        if (boosting)
            speed += 2
        else
            speed -= 5

        if (speed > maxSpeed)
            speed = maxSpeed
        if (speed < minSpeed)
            speed = minSpeed

        y -= speed + gravity

        if (y < minY)
            y = minY
        if (y > maxY)
            y = maxY
    }
}
