package com.example.smart_hog

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.ByteArrayOutputStream

class ProfileActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var tvFullName: TextView
    private lateinit var tvContact: TextView
    private lateinit var tvAddress: TextView
    private lateinit var tvUsername: TextView
    private lateinit var btnBack: ImageButton
    private lateinit var btnEditPhoto: View

    private val CAMERA_REQUEST = 100
    private val GALLERY_REQUEST = 102
    private val CROP_REQUEST = 103
    private val CAMERA_PERMISSION_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)

        imageView = findViewById(R.id.profileImage)
        tvUsername = findViewById(R.id.tvUsername)
        tvFullName = findViewById(R.id.tvFullName)
        tvContact = findViewById(R.id.tvContact)
        tvAddress = findViewById(R.id.tvAddress)
        btnBack = findViewById(R.id.btnBack)
        btnEditPhoto = findViewById(R.id.btnEditPhoto)

        loadProfileData()

        btnBack.setOnClickListener { finish() }

        btnEditPhoto.setOnClickListener {
            showModernImagePicker()
        }
    }

    private fun showModernImagePicker() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_image_picker, null)
        
        view.findViewById<LinearLayout>(R.id.option_camera).setOnClickListener {
            if (checkCameraPermission()) openCamera()
            dialog.dismiss()
        }
        
        view.findViewById<LinearLayout>(R.id.option_gallery).setOnClickListener {
            openGallery()
            dialog.dismiss()
        }
        
        dialog.setContentView(view)
        dialog.show()
    }

    private fun checkCameraPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
            return false
        }
        return true
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_REQUEST)
    }

    private fun startCrop(uri: Uri) {
        try {
            val cropIntent = Intent("com.android.camera.action.CROP")
            cropIntent.setDataAndType(uri, "image/*")
            cropIntent.putExtra("crop", "true")
            cropIntent.putExtra("aspectX", 1)
            cropIntent.putExtra("aspectY", 1)
            cropIntent.putExtra("outputX", 512)
            cropIntent.putExtra("outputY", 512)
            cropIntent.putExtra("return-data", true)
            startActivityForResult(cropIntent, CROP_REQUEST)
        } catch (e: Exception) {
            // Fallback: If crop intent fails, load image directly
            try {
                val inputStream = contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                if (bitmap != null) {
                    saveProfileImage(bitmap)
                }
            } catch (ex: Exception) {
                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST -> {
                    val photo = data?.extras?.get("data") as? Bitmap
                    photo?.let { saveProfileImage(it) }
                }
                GALLERY_REQUEST -> {
                    data?.data?.let { uri ->
                        startCrop(uri)
                    }
                }
                CROP_REQUEST -> {
                    val photo = data?.extras?.getParcelable<Bitmap>("data")
                    photo?.let { saveProfileImage(it) }
                }
            }
        }
    }

    private fun saveProfileImage(bitmap: Bitmap) {
        val loading = LoadingUtils.showLoading(this)
        Handler(Looper.getMainLooper()).postDelayed({
            imageView.setImageBitmap(bitmap)
            val prefs = getSharedPreferences("user_profile", Context.MODE_PRIVATE)
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val encodedImage = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT)
            prefs.edit().putString("image", encodedImage).apply()
            loading.dismiss()
            Toast.makeText(this, "Profile Picture Updated!", Toast.LENGTH_SHORT).show()
        }, 1000)
    }

    private fun loadProfileData() {
        val prefs = getSharedPreferences("user_profile", Context.MODE_PRIVATE)
        tvUsername.text = prefs.getString("username", "Silas_SmartHog")
        tvFullName.text = prefs.getString("name", "Silas Farm Owner")
        tvContact.text = prefs.getString("contact", "+63 912 345 6789")
        tvAddress.text = prefs.getString("address", "Purok 5, Smart Hog Village")
        
        val imageString = prefs.getString("image", null)
        if (imageString != null) {
            val imageBytes = Base64.decode(imageString, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            imageView.setImageBitmap(bitmap)
        }
    }
}
