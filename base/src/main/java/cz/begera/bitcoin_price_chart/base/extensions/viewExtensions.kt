package cz.begera.bitcoin_price_chart.base.extensions

import android.view.View

/**
 * Created by Jakub Begera (jakub@begera.cz) on 07/04/2019.
 */

/**
 * Shows or hides the view by putting View.VISIBLE/GONE due to @param.
 * @param shouldBeVisible
 */
fun View.visibleOrGone(shouldBeVisible: Boolean) {
    this.visibility = if (shouldBeVisible) View.VISIBLE else View.GONE
}

/**
 * Shows or hides the view by putting View.VISIBLE/INVISIBLE due to @param.
 * @param shouldBeVisible
 */
fun View.visibleOrInvisible(shouldBeVisible: Boolean) {
    this.visibility = if (shouldBeVisible) View.VISIBLE else View.INVISIBLE
}

/**
 * Set view visible.
 */
fun View.visible() {
    visibility = View.VISIBLE
}

/**
 * Set view invisible.
 */
fun View.invisible() {
    visibility = View.INVISIBLE
}

/**
 * Set view gone.
 */
fun View.gone() {
    visibility = View.GONE
}

/**
 * Returns true if view has visibility VISIBLE.
 */
fun View.isVisible() = visibility == View.VISIBLE