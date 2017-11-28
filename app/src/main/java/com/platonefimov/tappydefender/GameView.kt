package com.platonefimov.tappydefender

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.HandlerThread
import android.view.MotionEvent
import android.view.SurfaceView

@SuppressLint("ViewConstructor")
class GameView(context: Context, screenX: Int, screenY: Int) : SurfaceView(context), Runnable {

    private var playing = true
    private var gameThread: Thread? = null

    private val paint = Paint()
    private var canvas = Canvas()
    private val surfaceHolder = holder

    private val player = PlayerShip(context, screenY)

    private val enemy1 = EnemyShip(context, screenX, screenY)
    private val enemy2 = EnemyShip(context, screenX, screenY)
    private val enemy3 = EnemyShip(context, screenX, screenY)

    private val dustList = MutableList(40, {
        SpaceDust(screenX, screenY)
    })

    override fun run() {
        while (playing) {
            update()
            draw()
            limitFps()
        }
    }

    fun pause() {
        playing = false
        try {
            gameThread?.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun resume() {
        playing = true
        gameThread = Thread(this)
        gameThread?.start()
    }

    private fun limitFps() {
        HandlerThread.sleep(17)
    }

    private fun update() {
        // Update player and player control
        player.update()

        // Update enemies
        enemy1.update(player.speed)
        enemy2.update(player.speed)
        enemy3.update(player.speed)

        // Update dust
        for (dust in dustList)
            dust.update(player.speed)

        // Check hitBoxes
        if (Rect.intersects(player.hitBox, enemy1.hitBox))
            enemy1.x = -100
        if (Rect.intersects(player.hitBox, enemy2.hitBox))
            enemy2.x = -100
        if (Rect.intersects(player.hitBox, enemy3.hitBox))
            enemy3.x = -100
    }

    private fun draw() {
        if (surfaceHolder.surface.isValid) {
            // Lock
            canvas = surfaceHolder.lockCanvas()

            // Clear scene
            canvas.drawColor(Color.argb(255, 0, 0, 0))

            // Draw player
            canvas.drawBitmap(player.bitmap, player.x.toFloat(), player.y.toFloat(), paint)

            // Draw enemies
            canvas.drawBitmap(enemy1.bitmap, enemy1.x.toFloat(), enemy1.y.toFloat(), paint)
            canvas.drawBitmap(enemy2.bitmap, enemy2.x.toFloat(), enemy2.y.toFloat(), paint)
            canvas.drawBitmap(enemy3.bitmap, enemy3.x.toFloat(), enemy3.y.toFloat(), paint)

            // Draw dust
            paint.color = Color.argb(255, 255, 255, 255)
            for (dust in dustList)
                canvas.drawPoint(dust.x.toFloat(), dust.y.toFloat(), paint)

            // Unlock
            surfaceHolder.unlockCanvasAndPost(canvas)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        performClick()
        when (event.action) {
            MotionEvent.ACTION_UP -> player.boosting = false
            MotionEvent.ACTION_DOWN -> player.boosting = true
        }
        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}
