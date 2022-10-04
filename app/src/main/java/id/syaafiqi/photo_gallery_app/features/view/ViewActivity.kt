package id.syaafiqi.photo_gallery_app.features.view

import android.os.Bundle
import android.os.PersistableBundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import id.syaafiqi.photo_gallery_app.R
import id.syaafiqi.photo_gallery_app.databinding.ActivityViewBinding
import id.syaafiqi.photo_gallery_app.extensions.getExtra
import id.syaafiqi.photo_gallery_app.utils.Constants
import id.syaafiqi.photo_gallery_app.utils.Utility

class ViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewBinding
    private lateinit var scaleGestureDetector: ScaleGestureDetector

    private var scaleFactor: Float = 1.0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean =
        event?.let { scaleGestureDetector.onTouchEvent(event) }.run { false }

    private fun initView() {
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = getString(R.string.view_photo)
        }

        scaleGestureDetector = ScaleGestureDetector(
            this,
            object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    scaleFactor *= detector.scaleFactor
                    binding.ivPhotoView.scaleX = scaleFactor
                    binding.ivPhotoView.scaleY = scaleFactor
                    return true
                }

                override fun onScaleBegin(detector: ScaleGestureDetector): Boolean = true
                override fun onScaleEnd(detector: ScaleGestureDetector) {
                    super.onScaleEnd(detector)
                }
            })

        Glide.with(this).load(getExtra<String>(Constants.PHOTO_URL))
            .placeholder(Utility.createLoaderPlaceholder(this))
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(binding.ivPhotoView)
    }
}