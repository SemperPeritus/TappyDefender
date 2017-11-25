package com.platonefimov.tappydefender

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.HandlerThread
import android.util.Log
import android.view.SurfaceView

class GameView(context: Context?) : SurfaceView(context), Runnable {

    @Volatile
    var playing: Boolean = true
    private var gameThread: Thread? = null

    private val player = PlayerShip(context)

    private val paint = Paint()
    private var canvas = Canvas()
    private val surfaceHolder = holder

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
        Log.v(javaClass.name, "x: ${player.x} | y: ${player.y}.")
    }

    private fun draw() {
        setWillNotDraw(false)
        if (surfaceHolder.surface.isValid) {
            Log.v(javaClass.name, "Drawing...")

            canvas = surfaceHolder.lockCanvas()!!

            canvas.drawCircle(50f, 50f, 25f, paint)
            canvas.drawBitmap(player.bitmap, player.x, player.y, paint)

            surfaceHolder.unlockCanvasAndPost(canvas)
        } else {
            Log.v(javaClass.name, "Not drawing!")
        }
    }
}
