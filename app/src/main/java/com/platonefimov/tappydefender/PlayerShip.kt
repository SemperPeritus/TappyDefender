package com.platonefimov.tappydefender

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class PlayerShip(context: Context, screenY: Int) {

    val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ship)
    var x: Float = 50f
        private set
    var y: Float = 50f
        private set

    var speed: Float = 1f
        private set
    var boosting = false

    private val gravity = -12f

    private var maxY = (screenY - bitmap.height).toFloat()
    private var minY = 0f

    private val minSpeed = 1f
    private val maxSpeed = 20f

    fun update() {
        if (boosting)
            speed += 2f
        else
            speed -= 5f

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
