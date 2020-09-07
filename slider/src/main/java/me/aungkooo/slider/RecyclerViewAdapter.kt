package me.aungkooo.slider

import android.os.Bundle
import android.os.Parcelable
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

@Suppress("unused", "MemberVisibilityCanBePrivate")
abstract class RecyclerViewAdapter<V : View, VH : RecyclerViewAdapter.ViewHolder<V>> : PagerAdapter() {

    companion object {
        private const val TAG = "RecyclerViewAdapter"
    }

    private val recycleTypeCaches = SparseArray<RecycleCache<V, VH>>()
    private var savedStates = SparseArray<Parcelable>()

    final override fun instantiateItem(parent: ViewGroup, position: Int): Any {
        val viewType: Int = getItemViewType(position)
        if (recycleTypeCaches.get(viewType) == null) {
            recycleTypeCaches.put(viewType, RecycleCache(this))
        }
        val viewHolder: VH = recycleTypeCaches.get(viewType).getFreeViewHolder(parent, viewType)
        viewHolder.attach(parent, position)
        onBindViewHolder(viewHolder, position)
        viewHolder.onRestoreInstanceState(savedStates.getSaveState(position))
        return viewHolder
    }

    private fun SparseArray<Parcelable>.getSaveState(position: Int): Parcelable {
        return get(getItemId(position)) ?: Bundle()
    }

    final override fun destroyItem(parent: ViewGroup, position: Int, item: Any) {
        if (item is ViewHolder<*>) {
            item.detach(parent)
        }
    }

    final override fun getCount(): Int {
        return getItemCount()
    }

    final override fun isViewFromObject(view: View, item: Any): Boolean {
        return item is ViewHolder<*> && item.view === view
    }

    final override fun notifyDataSetChanged() {
        super.notifyDataSetChanged()
        for (viewHolder in getAttachedViewHolders()) {
            onNotifyItemChanged(viewHolder)
        }
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {
        if (state is Bundle) {
            state.classLoader = loader
            val currentSavedStates = if (state.containsKey(TAG)) state.getSparseParcelableArray<Parcelable>(TAG) else null
            savedStates = currentSavedStates ?: SparseArray<Parcelable>()
        }
        super.restoreState(state, loader)
    }

    private fun getAttachedViewHolders(): List<ViewHolder<V>> {
        val attachedViewHolders: MutableList<ViewHolder<V>> = mutableListOf()
        val n: Int = recycleTypeCaches.size()
        for (i in 0 until n) {
            for (viewHolder in recycleTypeCaches.get(recycleTypeCaches.keyAt(i)).viewHolderCache) {
                if (viewHolder.isAttached) {
                    attachedViewHolders.add(viewHolder)
                }
            }
        }

        return attachedViewHolders
    }

    // Methods to override
    protected open fun onNotifyItemChanged(viewHolder: ViewHolder<V>?) {}

    override fun getItemPosition(item: Any): Int {
        return POSITION_NONE
    }

    open fun getItemViewType(position: Int): Int {
        return 0
    }

    open fun getItemId(position: Int): Int {
        return position
    }

    abstract fun onCreateViewHolder(container: ViewGroup, viewType: Int): VH
    abstract fun onBindViewHolder(viewHolder: VH, position: Int)
    abstract fun getItemCount(): Int


    abstract class ViewHolder<V : View>(val view: V) {
        companion object {
            private const val TAG = "ViewHolder"
        }

        var isAttached: Boolean = false
        var position: Int = 0

        fun attach(parent: ViewGroup, position: Int) {
            isAttached = true
            this.position = position
            parent.addView(view)
        }

        fun detach(parent: ViewGroup) {
            parent.removeView(view)
            isAttached = false
            this.position = -1
        }

        fun onRestoreInstanceState(state: Parcelable) {
            if (state is Bundle) {
                val bundle: Bundle = state
                var array: SparseArray<Parcelable>? = null
                if (bundle.containsKey(TAG)) {
                    array = bundle.getSparseParcelableArray(TAG)
                }
                if (array != null) {
                    view.restoreHierarchyState(array)
                }
            }
        }

        fun onSaveInstanceState(): Parcelable {
            val state = SparseArray<Parcelable>()
            view.saveHierarchyState(state)
            val bundle = Bundle()
            bundle.putSparseParcelableArray(TAG, state)
            return bundle
        }
    }

    class RecycleCache<V : View, VH : ViewHolder<V>> internal constructor(val adapter: RecyclerViewAdapter<V, VH>) {

        val viewHolderCache: MutableList<VH> = mutableListOf()

        fun getFreeViewHolder(parent: ViewGroup, viewType: Int): VH {
            viewHolderCache.forEach { cachedViewHolder ->
                if (!cachedViewHolder.isAttached) {
                    return cachedViewHolder
                }
            }
            val viewHolder: VH = adapter.onCreateViewHolder(parent, viewType)
            viewHolderCache.add(viewHolder)
            return viewHolder
        }
    }
}