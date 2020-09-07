package me.aungkooo.slider

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import androidx.viewpager.widget.ViewPager
import java.security.InvalidParameterException

@Suppress("MemberVisibilityCanBePrivate", "unused")
open class SliderBase : RelativeLayout {

    protected var viewPager: ViewPager? = null
        private set

    private val childLayoutParams: LayoutParams
        get() = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

    constructor(context: Context?) : super(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        viewPager = ViewPager(context!!)
        // int viewpagerId = View.generateViewId();
        viewPager?.apply {
            this@apply.id = R.id.view_pager_slider
        }
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var fragmentAdapter: FragmentAdapter? = null
        get() {
            if (field == null) {
                throw NullPointerException("Adapter must not be null")
            }
            return field
        }
        set(fragmentAdapter) {
            fragmentAdapter?.let { adapter ->
                viewPager?.apply {
                    this@apply.adapter = adapter
                }
            } ?: throw InvalidParameterException("Adapter must not be null")
            field = fragmentAdapter
        }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        addView(viewPager, childLayoutParams)
    }

    fun setIndicator(indicator: Indicator, align: Indicator.ALIGN) {
        indicator.attachToViewPager(viewPager)
        val params = childLayoutParams
        params.addRule(align.POSITION)
        params.setMargins(0, 36, 0, 36)
        addView(indicator, params)
    }

    fun setIndicator(indicator: Indicator) {
        setIndicator(indicator, Indicator.ALIGN.BOTTOM)
    }

    fun setPageTransformer(pageTransformer: ViewPager.PageTransformer?) {
        viewPager?.apply {
            this@apply.setPageTransformer(true, pageTransformer)
        }
    }

    fun setOnPageChangeListener(listener: ViewPager.OnPageChangeListener?) {
        listener?.let { onPageChangeListener ->
            viewPager?.apply {
                this@apply.addOnPageChangeListener(onPageChangeListener)
            }
        }
    }
}