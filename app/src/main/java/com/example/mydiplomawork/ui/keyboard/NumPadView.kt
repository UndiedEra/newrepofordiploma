package com.example.mydiplomawork.ui.keyboard

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.example.mydiplomawork.R

class NumPadView : ViewGroup {

    private val keys = ArrayList<NumPadKeyView.Key>(ROWS_BUTTONS * COL_BUTTONS)

    private var keyboardListener: OnKeyboardListener? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    /**
     * Initialize keyboard
     */
    private fun init() {
        buildKeys()
        buildKeysView()
    }

    /**
     * Set on key click listener
     */
    fun setKeyboardListener(keyboardListener: OnKeyboardListener) {
        this.keyboardListener = keyboardListener
    }

    /**
     * Set new text and icon on enter button
     */
    fun setEnterText(text: String?, icon: Int?) {
        for (i in keys.indices) {
            if (keys[i].type == NumPadKeyView.Key.TYPE_ENTER) {
                keys[i].title = text ?: ""
                keys[i].icon = icon ?: 0
                (getChildAt(i) as NumPadKeyView).updateKey(keys[i])
                break
            }
        }
    }

    /**
     * Set new text and icon on cancel button
     */
    fun setCancelText(text: String?, icon: Int?) {
        for (i in keys.indices) {
            if (keys[i].type == NumPadKeyView.Key.TYPE_CANCEL) {
                keys[i].title = text ?: ""
                keys[i].icon = icon ?: 0
                (getChildAt(i) as NumPadKeyView).updateKey(keys[i])
                break
            }
        }
    }

    /**
     * Build keys data
     */
    private fun buildKeys() {
        keys.add(NumPadKeyView.Key("1", ""))
        keys.add(NumPadKeyView.Key("2", ""))
        keys.add(NumPadKeyView.Key("3", ""))
        keys.add(NumPadKeyView.Key("4", ""))
        keys.add(NumPadKeyView.Key("5", ""))
        keys.add(NumPadKeyView.Key("6", ""))
        keys.add(NumPadKeyView.Key("7", ""))
        keys.add(NumPadKeyView.Key("8", ""))
        keys.add(NumPadKeyView.Key("9", ""))
        keys.add(NumPadKeyView.Key(NumPadKeyView.Key.TYPE_ENTER, R.drawable.ic_fingerprint_new, ""))
        keys.add(NumPadKeyView.Key("0", ""))
        keys.add(NumPadKeyView.Key(NumPadKeyView.Key.TYPE_CANCEL, R.drawable.ic_backspace_arrow, ""))
    }

    /**
     * Build views for keys
     */
    private fun buildKeysView() {
        for (i in 0 until ROWS_BUTTONS) {
            for (j in 0 until COL_BUTTONS) {
                val keyView = NumPadKeyView(context)

                keyView.setup(keys[i * (ROWS_BUTTONS - 1) + j]) {
                    if (keyboardListener == null) {
                        return@setup
                    }
                    when {
                        it.type == NumPadKeyView.Key.TYPE_SYMBOL -> keyboardListener?.onSymbolClicked(it.title)
                        it.type == NumPadKeyView.Key.TYPE_ENTER -> keyboardListener?.onEnterClicked()
                        it.type == NumPadKeyView.Key.TYPE_CANCEL -> keyboardListener?.onCancelClicked()
                    }
                }

                addView(keyView)
            }
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val count = childCount
        val parentLeft = 0
        var childTop = 0
        var childLeft = parentLeft
        for (i in 0 until count) {
            val child = getChildAt(i)

            val width = child.measuredWidth
            val height = child.measuredHeight

            child.layout(childLeft, childTop, childLeft + width, childTop + height)

            childLeft += width

            //We should warp every so many children
            if (i % COL_BUTTONS == COL_BUTTONS - 1) {
                childLeft = parentLeft
                childTop += height
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val specWidthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val specWidthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val specHeightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        val specHeightMode = View.MeasureSpec.getMode(heightMeasureSpec)

        if (specHeightMode == View.MeasureSpec.UNSPECIFIED || specWidthMode == View.MeasureSpec.UNSPECIFIED) {
            throw IllegalStateException("CalendarPagerView should never be left to decide it's size")
        }

        val measureTileWidth = specWidthSize / COL_BUTTONS
        val measureTileHeight = specHeightSize / ROWS_BUTTONS
        setMeasuredDimension(specWidthSize, specHeightSize)
        val count = childCount
        for (i in 0 until count) {
            val child = getChildAt(i)

            val childWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                measureTileWidth,
                View.MeasureSpec.EXACTLY
            )

            val childHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                measureTileHeight,
                View.MeasureSpec.EXACTLY
            )

            child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
        }
    }

    /**
     * Keyboard click listener
     */
    interface OnKeyboardListener {

        /**
         * Invoke when enter clicked
         */
        fun onEnterClicked()


        /**
         * Invoke when cancel clicked
         */
        fun onCancelClicked()

        /**
         * Invoke when symbols clicked
         */
        fun onSymbolClicked(symbol: String)
    }

    companion object {

        private const val ROWS_BUTTONS = 4
        private const val COL_BUTTONS = 3
    }
}
