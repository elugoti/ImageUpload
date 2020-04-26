package android.app.ui

import android.Manifest
import android.app.Activity
import android.app.FileUtils
import android.app.ProgressDialog
import android.app.R
import android.app.databinding.FragmentHomeBinding
import android.app.di.Injectable
import android.app.ui.imageupload.ImageUploadViewModel
import android.app.utils.AppConstants
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.io.File
import javax.inject.Inject

class HomeFragment : Fragment(), Injectable, CallbackListeners {
    var TAG = "HomeFragment"
    private lateinit var binding: FragmentHomeBinding
    private val STORAGE_PERMISSIONS = 101
    var TAKE_PHOTO = "Take Photo"
    var CHOOSE_FROM_STORAGE = "Choose from Library"
    var CANCEL = "Cancel"
    private var userChoosenTask: String? = null
    private val PICK_IMAGE_REQUEST = 71

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ImageUploadViewModel
    private var imageReference: StorageReference? = null
    var progressDialog: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        bindListeners(upload(), showImageList())
        viewModel = injectViewModel(viewModelFactory)
        imageReference = FirebaseStorage.getInstance().reference.child(AppConstants.FOLDER_NAME)
        progressDialog = ProgressDialog(activity)
        return binding.root
    }

    private fun bindListeners(onClick: View.OnClickListener, onClick2: View.OnClickListener) {
        binding.apply {
            uploadImage = onClick
            viewList = onClick2
        }
    }

    fun upload(): View.OnClickListener {
        return View.OnClickListener {
            checkForPermissions()
        }
    }

    fun showImageList(): View.OnClickListener {
        return View.OnClickListener {
            showList()
        }
    }

    fun checkForPermissions() {
        Log.d("opening_gallery", "adding new 2");
        val permission = ContextCompat.checkSelfPermission(
            activity!!,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
//        val camPermission = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permission to storage denied")

            makeRequest()
        } else {
            selectImage()
            //launchGallery()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            activity!!,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            STORAGE_PERMISSIONS
        )
        Log.d("opening_gallery", "making request");

    }

    private fun selectImage() {
        val items = arrayOf<CharSequence>(TAKE_PHOTO, CHOOSE_FROM_STORAGE, CANCEL)

        val builder = android.app.AlertDialog.Builder(activity!!)
        builder.setTitle("Add Photo!")
        builder.setItems(items) { dialog, item ->
            if (items[item] == TAKE_PHOTO) {
                userChoosenTask = TAKE_PHOTO
                uploadImage()

            } else if (items[item] == CHOOSE_FROM_STORAGE) {
                userChoosenTask = CHOOSE_FROM_STORAGE
                launchGallery()
            } else if (items[item] == CANCEL) {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    fun uploadImage() {
        Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(
            HomeFragmentDirections.actionHomeToImageUpload()
        )
    }

    fun showList() {
        Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(
            HomeFragmentDirections.actionHomeToImageList()
        )
    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }
            try {
                var file = File(FileUtils.getRealPath(activity!!, data.data))
                var uri = Uri.fromFile(file)
                uploadImageToFirebase(uri)
            } catch (exc: java.lang.Exception) {

            }
        }
    }

    private fun uploadImageToFirebase(fileUri: Uri?) {
        if (fileUri != null) {
            showProgressBar()
            viewModel.callbackListeners = this
            viewModel.fileUri = fileUri
            viewModel.imageReference = imageReference
            viewModel.uploadFileImage()
        } else {
            Toast.makeText(activity, "No File!", Toast.LENGTH_LONG).show()
        }
    }

    private fun showProgressBar() {
        progressDialog?.setTitle(getString(R.string.uploading_image))
        progressDialog?.setMessage(getString(R.string.please_wait))
        progressDialog?.show()
    }

    override fun onRequestPermissionsResult(
        permsRequestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {

        Log.d("opening_gallery", "onResults request");

        if (permsRequestCode == STORAGE_PERMISSIONS) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Log.d("opening_gallery", "denied request");
                Log.d(TAG, "Permission has been denied by user")
            } else {
                Log.d(TAG, "Permission has been granted by user")
                Log.d("opening_gallery", "granted request");

                //launchGallery()
                selectImage()
            }
        }else{

        }
    }

    override fun onSuccess() {
        progressDialog?.dismiss()
        DialogHelper().openDialog(activity!!)
    }

    override fun onFailure(message: String) {
        progressDialog?.dismiss()
        Toast.makeText(activity, "Error uploading: " + message, Toast.LENGTH_LONG).show()
    }
}