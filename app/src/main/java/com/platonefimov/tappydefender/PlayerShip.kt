package com.platonefimov.tappydefender

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect

class PlayerShip(context: Context, screenY: Int) {

    var x = 50
        private set
    var y = 50
        private set

    val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ship)
    val hitBox = Rect(x, y, bitmap.width, bitmap.height)

    var speed = 1
        private set
    var boosting = false

    private val gravity = -12

    private var maxY = screenY - bitmap.height
    private var minY = 0

    private val minSpeed = 1
    private val maxSpeed = 20

    fun update() {
        // Are we boosting?
        if (boosting)
            speed += 2
        else
            speed -= 5

        // Control speed
        if (speed > maxSpeed)
            speed = maxSpeed
        if (speed < minSpeed)
            speed = minSpeed

        // Gravity
        y -= speed + gravity

        // Control height of player
        if (y < minY)
            y = minY
        if (y > maxY)
            y = maxY

        // Update hitBox
        hitBox.set(x, y, x + bitmap.width, y + bitmap.height)
    }
}
