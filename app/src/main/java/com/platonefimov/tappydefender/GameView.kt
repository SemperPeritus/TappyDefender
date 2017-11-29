package com.platonefimov.tappydefender

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.media.MediaPlayer
import android.os.HandlerThread
import android.view.MotionEvent
import android.view.SurfaceView

@SuppressLint("ViewConstructor")
class GameView(context: Context, private val screenX: Int, private val screenY: Int) :
        SurfaceView(context), Runnable {

    private val prefs = context.getSharedPreferences("HiScore", Context.MODE_PRIVATE)

    private var playing = true
    private var gameThread: Thread? = null

    private var gameEnded = false

    private val paint = Paint()
    private var canvas = Canvas()
    private val surfaceHolder = holder

    private val player = PlayerShip(context, screenY)

    private val enemy1 = EnemyShip(context, screenX, screenY)
    private val enemy2 = EnemyShip(context, screenX, screenY)
    private val enemy3 = EnemyShip(context, screenX, screenY)
    private val enemy4 = EnemyShip(context, screenX, screenY)
    private val enemy5 = EnemyShip(context, screenX, screenY)

    private var distanceRemaining = 10000f // 10 km
    private var timeTaken: Long = 0
    private var timeStarted = System.currentTimeMillis()
    private var fastestTime: Long = prefs.getLong("fastestTime", 100000L)

    private val bumpSound = MediaPlayer.create(context, R.raw.bump)
    private val destroyedSound = MediaPlayer.create(context, R.raw.destroyed)
    private val startSound = MediaPlayer.create(context, R.raw.start)
    private val winSound = MediaPlayer.create(context, R.raw.win)

    private val dustList = MutableList(40, {
        SpaceDust(screenX, screenY)
    })

    init {
        startSound.start()
    }

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
        if (!gameEnded) {
            // Update player and player control
            player.update()

            // Update enemies
            enemy1.update(player.speed)
            enemy2.update(player.speed)
            enemy3.update(player.speed)
            if (screenX > 1000)
                enemy4.update(player.speed)
            if (screenX > 1200)
                enemy5.update(player.speed)

            // Update dust
            for (dust in dustList)
                dust.update(player.speed)

            // Check hitBoxes
            var hitDetected = false
            if (Rect.intersects(player.hitBox, enemy1.hitBox)) {
                hitDetected = true
                enemy1.x = -9999
            }
            if (Rect.intersects(player.hitBox, enemy2.hitBox)) {
                hitDetected = true
                enemy2.x = -9999
            }
            if (Rect.intersects(player.hitBox, enemy3.hitBox)) {
                hitDetected = true
                enemy3.x = -9999
            }
            if (screenX > 1000)
                if (Rect.intersects(player.hitBox, enemy4.hitBox)) {
                    hitDetected = true
                    enemy4.x = -9999
                }
            if (screenX > 1200)
                if (Rect.intersects(player.hitBox, enemy5.hitBox)) {
                    hitDetected = true
                    enemy5.x = -9999
                }

            // if Hit detected
            if (hitDetected) {
                player.shieldStrength--
                bumpSound.start()
                if (player.shieldStrength < 0) {
                    gameEnded = true
                    destroyedSound.start()
                }
            }

            // Distance and time update
            distanceRemaining -= player.speed
            timeTaken = System.currentTimeMillis() - timeStarted

            // Is game completed?
            if (distanceRemaining < 0) {
                if (timeTaken < fastestTime)
                    fastestTime = timeTaken

                distanceRemaining = 0f

                gameEnded = true
                winSound.start()
            }
        }
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
            if (screenX > 1000)
                canvas.drawBitmap(enemy4.bitmap, enemy4.x.toFloat(), enemy4.y.toFloat(), paint)
            if (screenX > 1200)
                canvas.drawBitmap(enemy5.bitmap, enemy5.x.toFloat(), enemy5.y.toFloat(), paint)

            // Draw dust
            paint.color = Color.argb(255, 255, 255, 255)
            for (dust in dustList)
                canvas.drawPoint(dust.x.toFloat(), dust.y.toFloat(), paint)

            // Draw the HUD
            if (!gameEnded) {
                paint.textAlign = Paint.Align.LEFT
                paint.color = Color.argb(255, 255, 255, 255)
                paint.textSize = 25f

                canvas.drawText("Fastest: ${"%.3f".format(fastestTime / 1000f)}s", 10f, 20f, paint)
                canvas.drawText("Time: ${"%.3f".format(timeTaken / 1000f)}s", screenX / 2f, 20f, paint)
                canvas.drawText("Distance: ${distanceRemaining / 1000} KM",
                        screenX / 3f, screenY - 20f, paint)

                canvas.drawText("Shield: ${player.shieldStrength}",
                        10f, screenY - 20f, paint)

                canvas.drawText("Speed: ${player.speed * 60} MPS",
                        screenX / 1.5f, screenY - 20f, paint)
            } else {
                paint.textSize = 80f
                paint.textAlign = Paint.Align.CENTER

                canvas.drawText("Game Over", screenX / 2f, 100f, paint)

                paint.textSize = 25f
                canvas.drawText("Fastest: ${"%.3f".format(fastestTime / 1000f)}s", screenX / 2f, 160f, paint)
                canvas.drawText("Time: ${"%.3f".format(timeTaken / 1000f)}s",
                        screenX / 2f, 200f, paint)
                canvas.drawText("Distance remaining: ${distanceRemaining / 1000} KM",
                        screenX / 2f, 240f, paint)

                paint.textSize = 80f
                canvas.drawText("Tap to replay!", screenX / 2f, 350f, paint)
            }

            // Unlock
            surfaceHolder.unlockCanvasAndPost(canvas)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        performClick()
        when (event.action) {
            MotionEvent.ACTION_UP -> player.boosting = false
            MotionEvent.ACTION_DOWN -> {
                player.boosting = true
                if (gameEnded)
                    context.startActivity(Intent(context, GameActivity::class.java))
            }
        }
        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}
