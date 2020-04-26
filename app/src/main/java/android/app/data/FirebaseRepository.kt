package android.app.data

import android.graphics.Bitmap
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import javax.inject.Inject

class FirebaseRepository @Inject constructor (
    private val firebase: FirebaseSource
){
    fun uploadImage(bitmap: Bitmap?, imageReference: StorageReference?) = firebase.uploadImage(bitmap,imageReference)
    fun uploadImage(fileUri: Uri?, imageReference: StorageReference?) = firebase.uploadImage(fileUri,imageReference)
    fun fetchImagesList(storage: FirebaseStorage?) = firebase.fetchImagesList(storage)
}