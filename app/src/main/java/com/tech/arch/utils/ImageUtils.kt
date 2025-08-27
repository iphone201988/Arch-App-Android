package com.tech.arch.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Environment
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.BindingAdapter
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.tech.arch.R
import com.tech.arch.data.api.Constants
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.material.imageview.ShapeableImageView
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.MalformedURLException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.cancellation.CancellationException

object ImageUtils {
    fun navigateWithSlideAnimations(navController: NavController, destinationId: Int) {
        val navOptions = NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_right) // Define enter animation
            .setExitAnim(R.anim.slide_out_left) // Define exit animation
            .setPopEnterAnim(R.anim.slide_in_left) // Define pop enter animation
            .setPopExitAnim(R.anim.slide_out_right) // Define pop exit animation
            .build()

        navController.navigate(destinationId, null, navOptions)
    }

    fun goActivity(context: Context, activity: Activity) {
        val intent = Intent(context, activity::class.java)
        startActivity(context, intent, null)


    }
//    fun getStatusBarColor(activity: Activity, intColor: Int) {
//        activity.window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
//        activity.window.statusBarColor = ContextCompat.getColor(activity, intColor)
//
//
//    }

    fun hasPermissions(context: Context?, permissions: Array<String>?): Boolean {
        if (context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context, permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }

    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.CAMERA,
        )
    } else {
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
    }


    /** create image file j**/
    fun createImageFile(context: Context): File? {
        val timeStamp = SimpleDateFormat(
            "yyyyMMdd_HHmmss", Locale.US
        ).format(Date())
        val imageFileName = "IMG_" + timeStamp + "_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, ".png", storageDir
        )
        return image
    }
    fun getStatusBarColor(activity: Activity) {
        // Get the current UI mode (light or dark)
        val nightModeFlags = activity.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        // Resolve the color from the theme attribute (backgroundColor)
        val typedValue = TypedValue()
        val theme = activity.theme
        theme.resolveAttribute(R.attr.ss_backgroundBottomColor, typedValue, true)

        // Apply the status bar color from the resolved attribute
        val statusBarColor = typedValue.data
        activity.window.statusBarColor = statusBarColor

        // Optionally set the system UI visibility flag for light status bar text in light mode
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_NO) {
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            activity.window.decorView.systemUiVisibility = 0 // Reset for dark theme
        }
    }



    @BindingAdapter("circleImage")
    @JvmStatic
    fun circleImage(imageView: ShapeableImageView, imgUrl: String?) {
        if (!imgUrl.isNullOrEmpty()) {
            try {

                Glide.with(imageView.context).load(Constants.BASE_URL_IMAGE+imgUrl)
                    .placeholder(R.drawable.place_holder_pc).into(imageView)
            } catch (e: MalformedURLException) {
                imageView.setImageResource(R.drawable.place_holder_pc) // Set placeholder if URL is malformed
            }
        } else {
            imageView.setImageResource(R.drawable.place_holder_pc) // Set placeholder if imgUrl is null or empty
        }
    }
    @BindingAdapter("loadSimpleImage")
    @JvmStatic
    fun loadSimpleImage(imageView: AppCompatImageView, imgUrl: String?) {
        if (!imgUrl.isNullOrEmpty()) {
            try {


                Glide.with(imageView.context).load(Constants.BASE_URL_IMAGE+imgUrl)
                    .placeholder(R.drawable.place_holder_pc).into(imageView)
            } catch (e: MalformedURLException) {
                imageView.setImageResource(R.drawable.place_holder_pc) // Set placeholder if URL is malformed
            }
        } else {
            imageView.setImageResource(R.drawable.place_holder_pc) // Set placeholder if imgUrl is null or empty
        }
    }
    @BindingAdapter("setIcon")
    @JvmStatic
    fun setIcon(image: ImageView, i: Int) {
        image.setImageResource(i)
    }
    inline fun <reified T> parseJson(json: String): T? {
        return try {
            val gson = Gson()
            gson.fromJson(json, T::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    @JvmStatic
    suspend fun getMarkerBitmapDescriptor(context: Context, url: String?): BitmapDescriptor? = withContext(
        Dispatchers.IO) {
        if (url.isNullOrBlank()) return@withContext null

        try {
            val safeContext = context.applicationContext

            val futureTarget = Glide.with(safeContext)
                .asBitmap()
                .load(url)
                .override(40, 40)
                .submit()

            val bitmap = futureTarget.get()

            // Always clear Glide to prevent memory leaks
            Glide.with(safeContext).clear(futureTarget)

            // Return BitmapDescriptor
            BitmapDescriptorFactory.fromBitmap(bitmap)
        } catch (e: CancellationException) {
            // Coroutine was cancelled, do not log
            null
        } catch (e: Exception) {
            // Log other exceptions safely
            e.printStackTrace()
            null
        }
    }


    @BindingAdapter("setCertificationsText")
    @JvmStatic
    fun setCertificationsText(textView: AppCompatTextView, certifications: List<String>?) {
        textView.text = if (!certifications.isNullOrEmpty()) {
            certifications.joinToString(", ")
        } else {
            "N/A"
        }
    }


}