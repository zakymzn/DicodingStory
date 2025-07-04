package com.example.dicodingstory.ui.upload

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.dicodingstory.R
import com.example.dicodingstory.data.Result
import com.example.dicodingstory.data.remote.retrofit.ApiConfig
import com.example.dicodingstory.databinding.ActivityUploadBinding
import com.example.dicodingstory.ui.main.MainActivity
import com.example.dicodingstory.utils.getImageUri
import com.example.dicodingstory.utils.reduceFileImage
import com.example.dicodingstory.utils.uriToFile
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.materialToolbar
        val buttonGallery = binding.buttonGallery
        val buttonCamera = binding.buttonCamera
        val buttonAdd = binding.buttonAdd
        val edAddDescription = binding.edAddDescription
        val pbUpload = binding.pbUpload

        val uploadViewModelFactory: UploadViewModelFactory = UploadViewModelFactory.getInstance(this)
        val uploadViewModel: UploadViewModel by viewModels {
            uploadViewModelFactory
        }

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        buttonGallery.setOnClickListener { startGallery() }
        buttonCamera.setOnClickListener { startCamera() }
        buttonAdd.setOnClickListener {
            val description = edAddDescription.text.toString()

            currentImageUri?.let { uri ->
                val imageFile = uriToFile(uri, this).reduceFileImage()
                Log.d("Image File", "showImage: ${imageFile.path}")

                val requestBody = description.toRequestBody("text/plain".toMediaType())
                val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
                val multipartBody = MultipartBody.Part.createFormData(
                    "photo",
                    imageFile.name,
                    requestImageFile
                )
                
                uploadViewModel.addNewStory(multipartBody, requestBody).observe(this) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                pbUpload.visibility = View.VISIBLE
                            }
                            
                            is Result.Success -> {
                                pbUpload.visibility = View.GONE
                                val response = result.data

                                if (response.error == false) {
                                    Toast.makeText(this, getString(R.string.text_upload_success), Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(this, "Error : ${response.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                            
                            is Result.Error -> {
                                pbUpload.visibility = View.GONE
                                Toast.makeText(this, "Error : ${result.error}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            } ?: Toast.makeText(this, getString(R.string.text_warning_image_empty), Toast.LENGTH_SHORT).show()
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        } else {
            currentImageUri = null
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivPreview.setImageURI(it)
        }
    }
}