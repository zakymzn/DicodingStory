package com.example.dicodingstory.ui.upload

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.dicodingstory.R
import com.example.dicodingstory.data.Result
import com.example.dicodingstory.databinding.ActivityUploadBinding
import com.example.dicodingstory.ui.main.MainActivity
import com.example.dicodingstory.utils.getImageUri
import com.example.dicodingstory.utils.reduceFileImage
import com.example.dicodingstory.utils.uriToFile
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double? = null
    private var longitude: Double? = null
    private var currentImageUri: Uri? = null
    private var isFetchingLocation: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.materialToolbar
        val buttonGallery = binding.buttonGallery
        val buttonCamera = binding.buttonCamera
        val toggleCurrentLocation = binding.toggleCurrentLocation
        val buttonAdd = binding.buttonAdd
        val edAddDescription = binding.edAddDescription
        val pbUpload = binding.pbUpload

        val uploadViewModelFactory: UploadViewModelFactory = UploadViewModelFactory.getInstance(this)
        val uploadViewModel: UploadViewModel by viewModels {
            uploadViewModelFactory
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        buttonGallery.setOnClickListener { startGallery() }
        buttonCamera.setOnClickListener { startCamera() }
        toggleCurrentLocation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                getMyLocation()
            } else {
                latitude = null
                longitude = null
            }
        }
        buttonAdd.setOnClickListener {

            if (isFetchingLocation && toggleCurrentLocation.isChecked) {
                Toast.makeText(this, "Fetching location, please wait...", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val description = edAddDescription.text.toString()

            currentImageUri?.let { uri ->
                val imageFile = uriToFile(uri, this).reduceFileImage()
                Log.d("Image File", "showImage: ${imageFile.path}")

                val requestBodyDesc = description.toRequestBody("text/plain".toMediaType())
                val latRequestBody = latitude.toString().toRequestBody("text/plain".toMediaType())
                val lonRequestBody = longitude.toString().toRequestBody("text/plain".toMediaType())
                val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
                val multipartBody = MultipartBody.Part.createFormData(
                    "photo",
                    imageFile.name,
                    requestImageFile
                )

                uploadViewModel.addNewStory(multipartBody, requestBodyDesc, latRequestBody, lonRequestBody).observe(this) { result ->
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

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        isGranted: Boolean ->
        if (isGranted) {
            getMyLocation()
        } else {
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            binding.toggleCurrentLocation.isChecked = false
            isFetchingLocation = false
        }
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            isFetchingLocation = true

            fusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                isFetchingLocation = false
                val location: Location? = task.result
                if (location != null) {
                    latitude = location.latitude
                    longitude = location.longitude
                    Log.d("Location", "getMyLocation: $latitude, $longitude")
                    Toast.makeText(this, "Location fetched!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        this,
                        "Could not fetch location. Please ensure location services are enabled",
                        Toast.LENGTH_LONG
                    ).show()
                    latitude = null
                    longitude = null
                }
            }
                .addOnFailureListener { exception ->
                    isFetchingLocation = false
                    Log.e("Location", "Failed to get location: ${exception.message}")
                    Toast.makeText(this, "Failed to get location", Toast.LENGTH_SHORT).show()
                    latitude = null
                    longitude = null
                }
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}