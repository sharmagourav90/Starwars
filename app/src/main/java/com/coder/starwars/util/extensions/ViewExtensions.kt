package com.coder.starwars.util.extensions

import android.view.View

/**
 * Expension for view visibility
 */
fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}
