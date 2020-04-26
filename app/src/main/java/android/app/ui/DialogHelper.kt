package android.app.ui

import android.app.Activity
import android.app.AlertDialog
import android.app.R
import android.view.View
import android.widget.TextView

class DialogHelper {
    fun openDialog(activity : Activity) {
        val dialogBuilder = AlertDialog.Builder(activity)
        val inflater = activity.getLayoutInflater()
        val dialogView = inflater.inflate(R.layout.alert_dialog, null)
        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()
        alertDialog.show()

        dialogView.findViewById<TextView>(R.id.tvClose).setOnClickListener(View.OnClickListener {
            alertDialog.dismiss()
        })
    }
}