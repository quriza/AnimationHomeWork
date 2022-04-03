package com.logsis.animationhomework

import android.animation.Animator
import android.animation.ObjectAnimator
import android.graphics.Interpolator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageView
import java.lang.StrictMath.round

class MainActivity : AppCompatActivity(), Animator.AnimatorListener {
    var isFly = false;
    var aeroToStartAnimation: ObjectAnimator? = null
    var aeroplanTopPostition = 200f;
    lateinit var parashutView: ImageView
    lateinit var aeroplanView: ImageView
    var screenHeight = 0
    var screenWidth = 0
    var aeroplanWidth = 400
    var aeroplanHeight = 200
    val verticalStep = 50f
    val verticalStepDuration: Long = 500
    val horizontalFlyDuration: Long = 8000
    val parashutFlyDuration: Long = 4000
    val parashutOffset = 100f

    lateinit var btnParashut: Button
    var isParashutEnabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        parashutView = findViewById(R.id.parashut)
        aeroplanView = findViewById(R.id.aeroplan)
        btnParashut = findViewById(R.id.parashutBtn)


        findViewById<Button>(R.id.startBtn).setOnClickListener {
            startFly()
        }
        findViewById<Button>(R.id.topBtn).setOnClickListener {
            moveTop()
        }
        findViewById<Button>(R.id.bottomBtn).setOnClickListener {
            moveBottom()
        }

        findViewById<Button>(R.id.parashutBtn).setOnClickListener {
            makeParashut()
        }

    }



    fun makeAeroAnimation() {
        if (aeroToStartAnimation === null) {
            screenHeight = (aeroplanView.parent as View).height
            screenWidth = (aeroplanView.parent as View).width
            aeroplanView.visibility = View.VISIBLE
            aeroplanTopPostition = aeroplanView.top.toFloat()
            parashutView.visibility = View.VISIBLE
            parashutView.animate().x(screenWidth.toFloat()).setDuration(0).start();


            aeroToStartAnimation = ObjectAnimator.ofFloat(
                aeroplanView,
                View.TRANSLATION_X,
                screenWidth.toFloat(),
                -aeroplanWidth.toFloat()
            ).apply {
                interpolator = LinearInterpolator()
                duration = horizontalFlyDuration
                repeatCount = Animation.INFINITE
            }
        }
    }

   

    fun makeParashut() {
        if (aeroplanView.visibility != View.VISIBLE) {
            return;
        }
        if (!isParashutEnabled) {
            return;
        }

        isParashutEnabled = false
        val  aeroplanLeftPosition = round(aeroToStartAnimation?.animatedValue as Float)
        parashutView.visibility = View.VISIBLE

        parashutView.animate().x(aeroplanLeftPosition + parashutOffset).setDuration(0).start();
        val parashutAnimation = ObjectAnimator.ofFloat(
            parashutView,
            View.TRANSLATION_Y,
            aeroplanTopPostition + aeroplanHeight,
            screenHeight.toFloat()

        ).apply {
            interpolator = LinearInterpolator()
            duration = parashutFlyDuration
        }
        parashutAnimation.addListener(this)
        parashutAnimation.start()


    }
    fun moveTop() {
        aeroplanTopPostition -= verticalStep
        aeroplanView.animate().setDuration(verticalStepDuration).y(aeroplanTopPostition).start()
 
    }
    fun moveBottom() {
        aeroplanTopPostition += verticalStep
        aeroplanView.animate().setDuration(verticalStepDuration).y(aeroplanTopPostition).start()
    }

    fun startFly() {
        isFly = !isFly;

        makeAeroAnimation();
        if (isFly) {
            findViewById<ImageView>(R.id.aeroplan).visibility = View.VISIBLE
            aeroToStartAnimation?.start()

            findViewById<Button>(R.id.startBtn).setText(R.string.end_fly)
        } else {
            findViewById<ImageView>(R.id.aeroplan).visibility = View.GONE

            aeroToStartAnimation?.end()
            findViewById<Button>(R.id.startBtn).setText(R.string.start_fly)

        }
    }

    override fun onAnimationStart(p0: Animator?) {
    }

    override fun onAnimationEnd(p0: Animator?) {
        isParashutEnabled = true
    }

    override fun onAnimationCancel(p0: Animator?) {
    }

    override fun onAnimationRepeat(p0: Animator?) {
    }

}