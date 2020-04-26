package android.app.ui.imagelist

import android.app.Activity
import android.app.R
import android.app.databinding.ItemImageBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView


class ImageListAdapter(cxt: Activity, var imageListResp: ArrayList<String>, onImageClick: OnImageClick) : RecyclerView.Adapter<ImageListAdapter.ViewHolder>
    () {
    private lateinit var recyclerView: RecyclerView
    var activity: Activity = cxt
    var callback : OnImageClick = onImageClick
    var list: ArrayList<String>? = imageListResp
    //this method is returning the view for each item in the list

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = list?.get(position)
        holder.bind(activity,openFullImage(item),item!!)
    }

    private fun openFullImage(string: String?): View.OnClickListener {
        return View.OnClickListener {
            if(callback!=null){
                callback.onClick(string!!)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemImageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }
    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return imageListResp.size
    }

    //the class is hodling the list view
    class ViewHolder(private val binding: ItemImageBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(act: Activity, onClick: View.OnClickListener, item: String) {

            binding.apply {
                 imageUrl = item
                onImageClick = onClick
            }
        }
    }
}