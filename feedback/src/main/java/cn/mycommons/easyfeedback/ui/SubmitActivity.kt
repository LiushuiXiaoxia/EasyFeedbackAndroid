package cn.mycommons.easyfeedback.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.mycommons.easyfeedback.R
import cn.mycommons.easyfeedback.api.FeedbackDto
import cn.mycommons.easyfeedback.internal.FbUtil
import com.bumptech.glide.Glide

class SubmitActivity : AppCompatActivity() {

    companion object {

        private const val EXTRA_IMAGE_PATH = "image_path"

        fun gotoSubmit(activity: Activity, path: String) {
            val intent = Intent(activity, SubmitActivity::class.java)
            intent.putExtra(EXTRA_IMAGE_PATH, path)
            activity.startActivityForResult(intent, 12345)
        }
    }

    private lateinit var edtContact: EditText
    private lateinit var edtDesc: EditText
    private lateinit var ivImage: ImageView

    private var imagePath: String? = null
    private var contact: String? = null
    private var desc: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        imagePath = intent.getStringExtra(EXTRA_IMAGE_PATH)
        if (imagePath.isNullOrBlank()) {
            Toast.makeText(this, "image path is empty", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        edtContact = findViewById(R.id.edtContact)
        edtDesc = findViewById(R.id.edtDesc)
        ivImage = findViewById(R.id.ivImage)

        if (FbUtil.feedbackManager().getConfig().debug) {
            edtContact.setText("10086")
            edtDesc.setText("垃圾软件，毁我青春")
        }

        Glide.with(this).load(imagePath).into(ivImage)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.fb_submit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.submit -> submit()
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun submit() {
        if (check()) {
            doSubmit()
        }
    }

    private fun check(): Boolean {
        contact = edtContact.text.toString()
        desc = edtDesc.text.toString()

        // if (contact.isNullOrBlank()) {
        //     return false
        // }
        if (desc.isNullOrBlank()) {
            showToast("请添加必要的描述")
            return false
        }
        return true
    }


    private fun doSubmit() {
        val dto = FeedbackDto()
        dto.let {
            it.contact = contact
            it.desc = desc
            it.images = listOf(imagePath!!)
        }

        FbUtil.feedbackManager().submit(dto, null)

        showToast("感谢您的反馈~")
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}