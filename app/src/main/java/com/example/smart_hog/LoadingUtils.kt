package com.example.smart_hog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.animation.AnimationUtils
import android.widget.ImageView

object LoadingUtils {
    fun showLoading(context: Context): Dialog {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_loading)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        val logo = dialog.findViewById<ImageView>(R.id.loading_logo)
        val rotate = AnimationUtils.loadAnimation(context, R.anim.rotate_logo)
        logo.startAnimation(rotate)

        dialog.show()
        return dialog
    }
}
