package android.app.data

import android.app.utils.AppConstants
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import io.reactivex.Completable
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class FirebaseSource @Inject
constructor() {
    val TAG = "FirebaseSource"


    fun uploadImage(bitmap: Bitmap?, imageReference: StorageReference?) =
        Completable.create { emitter ->

            var fileName = "" + System.currentTimeMillis()

            val fileRef = imageReference!!.child(fileName + ".jpg")
            val stream = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.PNG, 70, stream)
            val byteArray: ByteArray = stream.toByteArray()
            fileRef.putBytes(byteArray)
                .addOnSuccessListener { taskSnapshot ->
                    var url = taskSnapshot.metadata?.path
                    var urlName = taskSnapshot.metadata?.name

                    Log.d(TAG, "url: " + Gson().toJson(url) + " \nName: " + urlName)
                    emitter.onComplete()
                    AppConstants.UPLOADED_URL = url

                    //tvFileName.text = taskSnapshot.metadata!!.path + " - " + taskSnapshot.metadata!!.sizeBytes / 1024 + " KBs"
                    //Toast.makeText(activity, "File Uploaded ", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "exception: " + exception.message)
                    emitter.onError(exception)
                }
                .addOnProgressListener { taskSnapshot ->
                    // progress percentage
                    val progress =
                        100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount

                    // percentage in progress dialog
                    val intProgress = progress.toInt()
                    //tvFileName.text = "Uploaded " + intProgress + "%..."
                }
                .addOnPausedListener { System.out.println("Upload is paused!") }
        }

    fun uploadImage(fileUri: Uri?, imageReference: StorageReference?) =
        Completable.create { emitter ->

            var fileName = "" + System.currentTimeMillis()

            val fileRef = imageReference!!.child(fileName + ".jpg")

            fileRef.putFile(fileUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    var url = taskSnapshot.metadata?.path
                    var urlName = taskSnapshot.metadata?.name

                    Log.d(TAG, "url: " + Gson().toJson(url) + " \nName: " + urlName)
                    emitter.onComplete()
                    AppConstants.UPLOADED_URL = url

                    //tvFileName.text = taskSnapshot.metadata!!.path + " - " + taskSnapshot.metadata!!.sizeBytes / 1024 + " KBs"
                    //Toast.makeText(activity, "File Uploaded ", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "exception: " + exception.message)
                    emitter.onError(exception)
                }
                .addOnProgressListener { taskSnapshot ->
                    // progress percentage
                    val progress =
                        100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount

                    // percentage in progress dialog
                    val intProgress = progress.toInt()
                    //tvFileName.text = "Uploaded " + intProgress + "%..."
                }
                .addOnPausedListener { System.out.println("Upload is paused!") }
        }

    fun fetchImagesList(storage: FirebaseStorage?) = Completable.create { emitter ->

        val listRef = storage?.reference?.child(AppConstants.FOLDER_NAME)
        AppConstants.IMAGE_LIST = ArrayList<String>()
        listRef?.listAll()
            ?.addOnSuccessListener { listResult ->

                listResult.items.forEach { item ->
                    // All the items under listRef.
                    Log.d(TAG,"item name: "+item.name)
                    AppConstants.IMAGE_LIST.add(item.name)
                }
                Log.d(TAG,"imagelist: "+Gson().toJson(AppConstants.IMAGE_LIST))
                emitter.onComplete()
            }?.addOnFailureListener {
                // Uh-oh, an error occurred!
                emitter.onError(it)
            }
    }


}