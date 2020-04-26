package android.app.ui

interface CallbackListeners {
    fun onSuccess()
    fun onFailure(message: String)
}