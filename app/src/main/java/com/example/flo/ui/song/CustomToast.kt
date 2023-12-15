package com.example.flo.ui.song

import android.widget.TextView
import android.widget.Toast
import com.example.flo.R

@Suppress("DEPRECATION")
fun Toast.showCustomToast(message: String, activity: SongActivity)
{
    val layout = activity.layoutInflater.inflate (
        R.layout.custom_toast,
        activity.findViewById(R.id.toast_container)
    )

    // set the text of the TextView of the message
    val textView = layout.findViewById<TextView>(R.id.toast_text)
    textView.text = message

    // use the application extension function
    this.apply {
        duration = Toast.LENGTH_SHORT
        view = layout
        show()
    }
}