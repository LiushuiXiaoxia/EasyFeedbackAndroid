package cn.mycommons.easyfeedback.shake

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import cn.mycommons.easyfeedback.FeedbackHelper
import cn.mycommons.easyfeedback.util.FeedbackHandler
import cn.mycommons.easyfeedback.util.logInfo
import kotlin.math.abs

/**
 * ShakeManager <br></br>
 * Created by xiaqiulei on 2020-01-11.
 */
object ShakeManager : SensorEventListener {

    private const val SHAKE_DIST = 50

    private val mSensorManager: SensorManager

    private val mAccelerometerSensor: Sensor?

    private var shaked: Boolean = false

    var onShakeCallback: OnShakeCallback? = null

    init {
        val context: Context = FeedbackHelper.context
        mSensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        // 获取加速度传感器
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    fun start() {
        if (!shaked) {
            mSensorManager.registerListener(
                this,
                mAccelerometerSensor,
                SensorManager.SENSOR_DELAY_UI
            )
        }
    }

    fun stop() {
        mSensorManager.unregisterListener(this)
    }

    fun restart() {
        if (shaked) {
            shaked = false
            start()
        }
    }

    private fun markSharked() {
        shaked = true
        stop()
        logInfo(">>> sharked <<<")
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.sensor?.let {
            if (it.type == Sensor.TYPE_ACCELEROMETER) {
                // 获取三个方向值
                val values = event.values
                val x = values[0]
                val y = values[1]
                val z = values[2]

                // logDebug("x = %s, y = %s, z = %s", x, y, z)
                if ((abs(x) > SHAKE_DIST || abs(y) > SHAKE_DIST || abs(z) > SHAKE_DIST) && !shaked) {
                    markSharked()

                    FeedbackHandler.post {
                        onShakeCallback?.onShake()
                        // start()
                    }
                }
            }
        }
    }
}