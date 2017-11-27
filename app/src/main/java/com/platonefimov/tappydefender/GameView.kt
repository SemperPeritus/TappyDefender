package com.platonefimov.tappydefender

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
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
        player.update()
        enemy1.update(player.speed)
        enemy2.update(player.speed)
        enemy3.update(player.speed)
    }

    private fun draw() {
        if (surfaceHolder.surface.isValid) {
            // Lock
            canvas = surfaceHolder.lockCanvas()

            // Clear scene
            canvas.drawColor(Color.argb(255, 0, 0, 0))

            // Draw player
            canvas.drawBitmap(player.bitmap, player.x, player.y, paint)

            // Draw enemies
            canvas.drawBitmap(enemy1.bitmap, enemy1.x, enemy1.y, paint)
            canvas.drawBitmap(enemy2.bitmap, enemy2.x, enemy2.y, paint)
            canvas.drawBitmap(enemy3.bitmap, enemy3.x, enemy3.y, paint)

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
