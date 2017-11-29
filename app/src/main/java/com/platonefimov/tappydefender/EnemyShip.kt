package com.platonefimov.tappydefender

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import java.util.*

class EnemyShip(private val context: Context, screenX: Int, screenY: Int) {

    private val random = Random()

    private val minX = 0
    private val maxX = screenX
    private val maxY = screenY

    var bitmap: Bitmap = randomBitmap()

    var x = screenX
    var y = random.nextInt(maxY) - bitmap.height
        private set

    val hitBox = Rect(x, y, bitmap.width, bitmap.height)

    private var speed = random.nextInt(6) + 10

    fun update(playerSpeed: Int) {
        // Update position
        x -= playerSpeed
        x -= speed

        // Reset position (aka. create new enemy)
        if (x < minX - bitmap.width) {
            bitmap = randomBitmap()
            speed = random.nextInt(10) + 10
            x = maxX
            y = random.nextInt(maxY) - bitmap.height
        }

        // Update hitBox
        hitBox.set(x, y, x + bitmap.width, y + bitmap.height)
    }

    private fun randomBitmap() = when (random.nextInt(3)) {
        0 -> BitmapFactory.decodeResource(context.resources, R.drawable.enemy)
        1 -> BitmapFactory.decodeResource(context.resources, R.drawable.enemy2)
        else -> BitmapFactory.decodeResource(context.resources, R.drawable.enemy3)
    }
}
