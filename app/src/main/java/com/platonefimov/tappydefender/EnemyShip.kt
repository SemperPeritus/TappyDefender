package com.platonefimov.tappydefender

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.util.*

class EnemyShip(context: Context, screenX: Int, screenY: Int) {

    private val random = Random()

    val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.enemy)
    private var speed = random.nextInt(6) + 10

    private val minX = 0
    private val maxX = screenX
    private val maxY = screenY

    var x = screenX
        private set
    var y = random.nextInt(maxY) - bitmap.height
        private set

    fun update(playerSpeed: Int) {
        x -= playerSpeed
        x -= speed

        if (x < minX - bitmap.width) {
            speed = random.nextInt(10) + 10
            x = maxX
            y = random.nextInt(maxY) - bitmap.height
        }
    }
}
