package com.example.pokemonapp.core

import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat


fun String.getColorOfType():Int {
    var color = EnumColorType.NOT_FOUND.color
    EnumColorType.values().forEach {
        if (it.type == this) {
            color = it.color
        }
    }
    return color
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}
