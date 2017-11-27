package com.platonefimov.tappydefender

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.util.*

class EnemyShip(context: Context, screenX: Int, screenY: Int) {

    private val random = Random()

    val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.enemy)
    private var speed = (random.nextInt(6) + 10).toFloat()

    private val minX = 0
    private val maxX = screenX
//    private val minY = 0
    private val maxY = screenY

    var x = screenX.toFloat()
        private set
    var y = (random.nextInt(maxY) - bitmap.height).toFloat()
        private set

    fun update(playerSpeed: Float) {
        x -= playerSpeed
        x -= speed

        if (x < minX - bitmap.width) {
            speed = (random.nextInt(10) + 10).toFloat()
            x = maxX.toFloat()
            y = (random.nextInt(maxY) - bitmap.height).toFloat()
        }
    }
}
