package android.app.ui.imagelist

import android.app.data.FirebaseRepository
import android.app.ui.CallbackListeners
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ImageListViewModel  @Inject constructor(
    private val repository: FirebaseRepository
) : ViewModel() {

    //email and password for the input
    var storage : FirebaseStorage ? =null
    //auth listener
    var callbackListeners: CallbackListeners? = null

    //disposable to dispose the Completable
    private val disposables = CompositeDisposable()

    //function to perform login
    fun fetchImageList() {

        //calling login from repository to perform the actual authentication
        val disposable = repository.fetchImagesList(storage)
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