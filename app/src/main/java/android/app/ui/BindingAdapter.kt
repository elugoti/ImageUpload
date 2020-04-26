package android.app.ui

import android.app.R
import android.app.utils.AppConstants
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

@BindingAdapter("loadImageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
    if (imageUrl!=null&& imageUrl.isNotEmpty()) {
        Glide.with(view.context)
            .load(getUrl(imageUrl))
            .error(R.drawable.ic_photo)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    } else {
        view.setImageResource(R.drawable.ic_photo)
    }
}

fun getUrl(imageUrl: String): String {
   return "https://firebasestorage.googleapis.com/v0/b/task-48bc5.appspot.com/o/" +
            AppConstants.FOLDER_NAME + "%2F"+imageUrl+"?alt=media"
}
