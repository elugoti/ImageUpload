package android.app.ui.imageupload

import android.app.data.FirebaseRepository
import android.app.ui.CallbackListeners
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import androidx.lifecycle.ViewModel
import com.google.firebase.storage.StorageReference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ImageUploadViewModel  @Inject constructor(
    private val repository: FirebaseRepository
) : ViewModel() {

    //email and password for the input
    var imageReference: StorageReference? = null
    var bitmap : Bitmap? = null
    var fileUri: Uri ? = null
    //auth listener
    var callbackListeners: CallbackListeners? = null

    //disposable to dispose the Completable
    private val disposables = CompositeDisposable()

    //function to perform login
    fun uploadImage() {

        //calling login from repository to perform the actual authentication
        val disposable = repository.uploadImage(bitmap,imageReference)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //sending a success callback
                callbackListeners?.onSuccess()
            }, {
                //sending a failure callback
                callbackListeners?.onFailure(it.message!!)
            })
        disposables.add(disposable)
    }

    fun uploadFileImage() {

        //calling login from repository to perform the actual authentication
        val disposable = repository.uploadImage(fileUri,imageReference)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //sending a success callback
                callbackListeners?.onSuccess()
            }, {
                //sending a failure callback
                callbackListeners?.onFailure(it.message!!)
            })
        disposables.add(disposable)
    }


    //disposing the disposables
    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

}