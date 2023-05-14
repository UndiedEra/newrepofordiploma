package com.example.mydiplomawork.ui.keyboard

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.example.mydiplomawork.R

class NumPadKeyView : FrameLayout {

    private var vTitle: TextView? = null
    private var vSecondaryText: TextView? = null
    private var vKeyIcon: AppCompatImageView? = null
    private var onKeyClickListener: (Key) -> Unit = {}
    private var key: Key? = null
    private val mHighLightPaint = Paint()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    fun setup(key: Key, onKeyClickListener: (Key) -> Unit) {
        this.onKeyClickListener = onKeyClickListener
        updateKey(key)

        frameLayout.setOnClickListener {
            this.onKeyClickListener(key)
        }
    }


    fun updateKey(key: Key) {
        this.key = key
        vTitle?.text = key.title
        if (key.type == Key.TYPE_ENTER || key.type == Key.TYPE_CANCEL) {
            vSecondaryText?.visibility = View.GONE
            vTitle?.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
            vTitle?.visibility = if (key.title.isEmpty()) View.GONE else View.VISIBLE
            vTitle?.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryText))
            vTitle?.text = vTitle?.text.toString().toUpperCase()
            if (key.icon == 0) {
                vKeyIcon?.visibility = View.GONE
            } else {
                vKeyIcon?.visibility = View.VISIBLE
                vKeyIcon?.setImageResource(key.icon)
            }
        }
        vSecondaryText?.text = key.secondary
    }

    private lateinit var frameLayout: FrameLayout

    private fun init() {
        mHighLightPaint.color = HIGH_LIGHT_COLOR
        //vTitle?.setBackgroundResource(R.drawable.green_selectable_rounded_rect)

        val linearLayout = LinearLayout(context)
        linearLayout.tag = KEY_VIEW_TAG
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.gravity = Gravity.CENTER

        vTitle = TextView(context)
        vTitle?.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryText))
        vTitle?.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24f)
        vTitle?.layoutParams = createLayoutParams()
        vTitle?.gravity = Gravity.CENTER
        vTitle?.setTextColor(resources.getColor(R.color.white))
        linearLayout.addView(vTitle)

//        vSecondaryText = TextView(context)
//        vSecondaryText?.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDarkText))
//        vSecondaryText?.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12f)
//        vSecondaryText?.layoutParams = createLayoutParams()
//        vSecondaryText?.gravity = Gravity.CENTER
//        linearLayout.addView(vSecondaryText)

        val size = 72
        val params = FrameLayout.LayoutParams(size, size)
        params.gravity = Gravity.CENTER
        vKeyIcon = AppCompatImageView(context)
        vKeyIcon?.scaleType = ImageView.ScaleType.CENTER
        vKeyIcon?.layoutParams = params
        vKeyIcon?.visibility = View.GONE
        addView(vKeyIcon)

        frameLayout = FrameLayout(context)
        frameLayout.layoutParams = createFrameLayoutParams()
        val outValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, outValue, true)
        frameLayout.setBackgroundResource(outValue.resourceId)
        frameLayout.addView(linearLayout)
        addView(frameLayout)

    }

    override fun setOnClickListener(onClickListener: OnClickListener?) {
        frameLayout.setOnClickListener(onClickListener)
    }

    private fun createFrameLayoutParams(): FrameLayout.LayoutParams {
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.MATCH_PARENT)
        val size = 72
        params.width = size
        params.height = size
        params.gravity = Gravity.CENTER
        return params
    }

    private fun createLayoutParams(): LinearLayout.LayoutParams {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.CENTER
        return params
    }

    override fun draw(canvas: Canvas) {
        if (key?.isVisible == true && isPressed) {
            val radius = Math.min(height, width) / 2
            val offset = Math.abs(height - width) / 2
            // Lollipop platform bug. Circle drawable offset needs to be half of normal offset
            val circleOffset = if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) offset / 2 else offset

            if (width >= height) {
                canvas.drawCircle((circleOffset + radius).toFloat(), radius.toFloat(), radius.toFloat(), mHighLightPaint)
            } else {
                canvas.drawCircle(radius.toFloat(), (circleOffset + radius).toFloat(), radius.toFloat(), mHighLightPaint)
            }
        }

        super.draw(canvas)
    }


    class Key(
        var type: Int = 0,
        var icon: Int = 0,
        var title: String = "",
        var secondary: String = ""
    ) {

        constructor(title: String, subtitle: String) :
                this(TYPE_SYMBOL, 0, title, subtitle)

        val isVisible: Boolean
            get() = title.isNotEmpty() ||
                    secondary.isNotEmpty() ||
                    icon != 0 && icon != 0


        companion object {
            const val TYPE_SYMBOL = 1
            const val TYPE_ENTER = 2
            const val TYPE_CANCEL = 3
        }
    }

    companion object {
        private val HIGH_LIGHT_COLOR = Color.parseColor("#FFFFFF")
        private const val KEY_VIEW_TAG = "key_view_tag"
    }
}
