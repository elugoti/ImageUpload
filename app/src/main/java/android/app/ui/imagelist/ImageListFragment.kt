package android.app.ui.imagelist

import android.app.ProgressDialog
import android.app.R
import android.app.di.Injectable
import android.app.ui.CallbackListeners
import android.app.ui.HomeFragmentDirections
import android.app.ui.imageupload.ImageUploadViewModel
import android.app.ui.injectViewModel
import android.app.utils.AppConstants
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

class ImageListFragment : Fragment(), Injectable, CallbackListeners, OnImageClick {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: ImageListViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var tvNoImagesFound: TextView
    var storage: FirebaseStorage? = null
    var progressDialog: ProgressDialog? = null
    var adapter: ImageListAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater?.inflate(
            R.layout.fragment_image_list,
            container, false
        )
        progressDialog = ProgressDialog(activity)
        storage = FirebaseStorage.getInstance()
        viewModel = injectViewModel(viewModelFactory)
        viewModel.storage = storage
        viewModel.callbackListeners = this
        recyclerView = view.findViewById(R.id.rvImages)
        tvNoImagesFound = view.findViewById(R.id.tvNoImagesFound)
        initViews()

        viewModel.fetchImageList()

        return view
    }

    private fun initViews() {
        recyclerView.layoutManager = GridLayoutManager(activity, 3)
        progressDialog?.setTitle(getString(R.string.fetching_images))
        progressDialog?.setMessage(getString(R.string.please_wait))
        progressDialog?.show()
    }

    override fun onSuccess() {
        progressDialog?.dismiss()
        var list = AppConstants.IMAGE_LIST as ArrayList<String>
        list.reverse()
        if(list!=null&&list.size>0){
            recyclerView.visibility = View.VISIBLE
            tvNoImagesFound.visibility = View.GONE
            adapter = ImageListAdapter(activity!!, list,this);
            recyclerView.adapter = adapter
        }else{
            tvNoImagesFound.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        }
    }

    override fun onFailure(message: String) {
        progressDialog?.dismiss()
    }

    override fun onClick(url: String?) {
        if(url!=null&&!url.isEmpty()){
            val bundle = bundleOf(activity?.getString(R.string.image_url)!! to url)
            Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(
                R.id.action_fullscreen_fragment,bundle
            )
        }
    }
}