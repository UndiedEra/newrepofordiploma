package com.example.mydiplomawork.ui.utils

import android.view.View

internal fun View.show(show: Boolean) {
    if (show) {
        show()
    } else {
        hide()
    }
}

internal fun View.show() {
    visibility = View.VISIBLE
}

internal fun View.invisible() {
    visibility = View.INVISIBLE
}

internal fun View.hide() {
    visibility = View.GONE
}