package me.aungkooo.slider

import android.content.Context
import android.util.AttributeSet

/**
 * Created by Ko Oo on 26/5/2018.
 */
@Suppress("unused")
class DotIndicator : Indicator {

    constructor(context: Context) : super(context, R.drawable.dot_active, R.drawable.dot_inactive) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setIconDrawables(R.drawable.dot_active, R.drawable.dot_inactive)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)
}