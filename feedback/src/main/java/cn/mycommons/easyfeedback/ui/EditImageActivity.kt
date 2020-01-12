package cn.mycommons.easyfeedback.ui

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.mycommons.easyfeedback.R
import cn.mycommons.easyfeedback.util.logInfo


class EditImageActivity : AppCompatActivity() {

    companion object {

        private const val EXTRA_IMAGE_PATH = "image_path"

        fun gotoEdit(activity: Activity, path: String) {
            val intent = Intent(activity, EditImageActivity::class.java)
            intent.putExtra(EXTRA_IMAGE_PATH, path)
            activity.startActivityForResult(intent, 12345)
        }
    }

    private lateinit var ivImage: ImageView

    private var imagePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_image)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        imagePath = intent.getStringExtra(EXTRA_IMAGE_PATH)
        if (imagePath.isNullOrBlank()) {
            Toast.makeText(this, "image path is empty", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        logInfo("imagePath = $imagePath")
        ivImage = findViewById(R.id.ivImage)
        ivImage.setImageBitmap(BitmapFactory.decodeFile(imagePath))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.fb_edit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.next -> gotoSubmit()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun gotoSubmit() {
        SubmitActivity.gotoSubmit(this, imagePath!!)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}