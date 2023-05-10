package com.example.caqao.models

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.caqao.network.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

enum class CaqaoApiStatus { LOADING, ERROR, DONE }
var USER_TOKEN: String? = null
class CacaoDetectionViewModel: ViewModel() {

    private val _selectedImage = MutableLiveData<Uri?>()
    val selectedImage: LiveData<Uri?>
        get() = _selectedImage

    private val _status = MutableLiveData<CaqaoApiStatus>()
    val status: LiveData<CaqaoApiStatus> = _status

    private val _cacaoDetection = MutableLiveData<CacaoDetection>()
    val cacaoDetection: LiveData<CacaoDetection>
        get() = _cacaoDetection

    private val _detections = MutableLiveData<List<CacaoDetection>>()
    val detections: LiveData<List<CacaoDetection>> = _detections

    private val _imageValidationStatus = MutableLiveData<ImageValidationStatus>()
    val imageValidationStatus: LiveData<ImageValidationStatus> = _imageValidationStatus

    private val _detectionStatus = MutableLiveData<String>()
    val detectionStatus: LiveData<String> = _detectionStatus

    private val _userToken = MutableLiveData<String>()
    val userToken: LiveData<String> = _userToken

    private val _userAccountCreationStatus = MutableLiveData<UserAccountCreationStatus>()
    val userAccountCreationStatus: LiveData<UserAccountCreationStatus> = _userAccountCreationStatus

    private val _userLoginStatus = MutableLiveData<UserLoginStatus?>()
    val userLoginStatus: LiveData<UserLoginStatus?> = _userLoginStatus

    init {
        resetCacaoDetection()
    }

    fun selectImage(uri: Uri?) {
        _selectedImage.value = uri
    }

    fun assessCacaoBeans(context: Context, contentResolver: ContentResolver, beanSize: Int) {
        val filesDir = context.filesDir
        val file = File(filesDir, "image.jpg")

        val inputSteam = _selectedImage.value?.let { contentResolver.openInputStream(it) }
        val outputStream = FileOutputStream(file)
        inputSteam!!.copyTo(outputStream)

        val imageFileRequestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageFilePart  = MultipartBody.Part.createFormData("image", file.name,
            imageFileRequestBody)

        viewModelScope.launch {
            _status.value = CaqaoApiStatus.LOADING
            try {
                _cacaoDetection.value = CacaoApi.retrofitService.assess(imageFilePart, beanSize)
                Log.d("BeanGrade", "BeanGrade: ${_cacaoDetection.value!!.beanGrade}")

                if (_cacaoDetection.value!!.beanGrade == "--") {
                    Toast.makeText(context, "No Cacao Beans Detected!", Toast.LENGTH_SHORT).show()
                }

                _status.value = CaqaoApiStatus.DONE
            } catch (e: retrofit2.HttpException) {
                Log.d("HTTPError", "Error: ${e}")
                _status.value = CaqaoApiStatus.ERROR
            } catch (e: Exception) {
                Log.d("AssessmentFailed", "Error: ${e}")
                _status.value = CaqaoApiStatus.ERROR
            }
        }
    }

    fun saveAssessmentResults() {
        viewModelScope.launch {
            try {
                USER_TOKEN?.let {
                    CacaoApi.retrofitService.saveDetectionResults(
                        token = it,
                        imgSrcUrl = _cacaoDetection.value?.imgSrcUrl.toString())
                }
            } catch (e: Exception) {
                Log.d("Saving Results Failed", "Error: ${e}")
            }
        }
    }

    fun resetCacaoDetection() {
        _cacaoDetection.value = CacaoDetection(
            -1, null, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, "--", "--")
    }

    fun getCacaoDetections() {
        viewModelScope.launch {
            try {
                _detections.value = USER_TOKEN?.let { CacaoApi.retrofitService.getDetections(it) }
                _detectionStatus.value = "Success: CAQAO detections retrieved"
                _savedImagesCount.value = _detections.value?.size
            } catch (e: Exception) {
                _detectionStatus.value = "Failure: ${e.message}"
            }
        }
    }

    fun validateImage(context: Context, contentResolver: ContentResolver) {
        val filesDir = context.filesDir
        val file = File(filesDir, "image.jpg")

        val inputSteam = _selectedImage.value?.let { contentResolver.openInputStream(it) }
        val outputStream = FileOutputStream(file)
        inputSteam!!.copyTo(outputStream)

        val imageFileRequestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageFilePart  = MultipartBody.Part.createFormData("image", file.name,
            imageFileRequestBody)

        viewModelScope.launch {
            try {
                // TODO: send image to the server and check if the image contains cacao beans or not
                _imageValidationStatus.value = CacaoApi.retrofitService.validateImage(imageFilePart)
                val response = _imageValidationStatus.value
                if (response?.status == 200) {
                    Log.d("ImageValidationSuccess",
                        "HTTP status code: ${response.status} Message: ${response.message}")
                }
                else {
                    Log.d("ImageValidationFailed",
                        "HTTP status code: ${response?.status} Message: ${response?.message}")
                    Toast.makeText(context, "${response?.message}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Log.d("ImageValidationFailed", "Error: ${e}")
            }
        }

    }

    suspend fun registerUser(
        first_name: String, last_name: String, email: String,
        username: String, password: String
    ): Int {

        val user = User(first_name, last_name, email, username, password)
        val deferred = CompletableDeferred<Int>()

        viewModelScope.launch {
            try {
                _userAccountCreationStatus.value = CacaoApi.retrofitService.createUser(user)
                _userAccountCreationStatus.value!!.status?.let { deferred.complete(it) }
            } catch (e: Exception) {
                Log.d("UserCreationFailed", "${e.message}")
            }
        }
        return deferred.await()
    }

    suspend fun loginUser(username: String, password: String): Int {
        val user = User(username=username, password=password)
        val deferred = CompletableDeferred<Int>()

        viewModelScope.launch {
            try {
                _userLoginStatus.value = CacaoApi.retrofitService.loginUser(user)
                _userLoginStatus.value?.status?.let { deferred.complete(it) }
                USER_TOKEN = _userLoginStatus.value?.token
                Log.d("UserToken", "${_userLoginStatus.value?.token}")
            } catch (e: Exception) {
                Log.d("UserLoginFailed", "${e.message}")
            }
        }
        return deferred.await()
    }

    private val _savedImagesCount = MutableLiveData<Int>()
    val savedImagesCount: LiveData<Int> = _savedImagesCount


}
