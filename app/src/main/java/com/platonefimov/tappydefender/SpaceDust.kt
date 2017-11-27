package com.platonefimov.tappydefender

import java.util.*

class SpaceDust(screenX: Int, screenY: Int) {

    private val random = Random()

    private val maxX = screenX
    private val maxY = screenY

    var x = random.nextInt(maxX)
        private set
    var y = random.nextInt(maxY)
        private set

    private var speed = random.nextInt(10)

    fun update(playerSpeed: Int) {
        x -= playerSpeed
        x -= speed

        if (x < 0) {
            x = maxX
            y = random.nextInt(maxY)
            speed = random.nextInt(15)
        }
    }
}
