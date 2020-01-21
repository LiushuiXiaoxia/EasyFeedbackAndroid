package cn.mycommons.easyfeedback.example

import android.Manifest.permission.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import cn.mycommons.easyfeedback.FeedbackHelper
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE_ASK_PERMISSIONS = 123
    }

    private val permissionList = arrayOf(
            READ_EXTERNAL_STORAGE,
            WRITE_EXTERNAL_STORAGE,
            INTERNET,
            "MediaStore.Images.Media.INTERNAL_CONTENT_URI",
            "MediaStore.Images.Media.EXTERNAL_CONTENT_URI"
    )

    private val random = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.btnShowDialog).setOnClickListener {
            // just test
            FeedbackHelper.showDialog(this)
        }

        val s = SimpleDateFormat("yyyyMMddHHmmss.sss", Locale.getDefault()).format(Date())
        findViewById<TextView>(R.id.text).text = "Hello world\n$s"

        checkPermission()
    }


    override fun onResume() {
        super.onResume()

        rlRoot.setBackgroundColor(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)))
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val list = mutableListOf<String>()

            for (s in permissionList) {
                val permission = ActivityCompat.checkSelfPermission(this, s)
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    list.add(s)
                }
            }
            if (list.isNotEmpty()) {
                requestPermissions(list.toTypedArray(), REQUEST_CODE_ASK_PERMISSIONS)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_ASK_PERMISSIONS -> {
                for (it in grantResults) {
                    if (it != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Get Permission Denied", Toast.LENGTH_SHORT).show()
                        finish()
                        break
                    }
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}