package me.aungkooo.slider

import android.content.Context
import android.util.AttributeSet
import java.security.InvalidParameterException

/**
 * Created by Ko Oo on 25/5/2018.
 */

@Suppress("MemberVisibilityCanBePrivate", "unused")
class Slider : SliderBase {

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var viewAdapter: ViewAdapter<*, *>? = null
        get() {
            if (field == null) {
                throw NullPointerException("Adapter must not be null")
            }
            return field
        }
        set(viewAdapter) {
            viewAdapter?.let { adapter ->
                viewPager?.apply {
                    this@apply.adapter = adapter
                }
            } ?: throw InvalidParameterException("Adapter must not be null")
            field = viewAdapter
        }
}