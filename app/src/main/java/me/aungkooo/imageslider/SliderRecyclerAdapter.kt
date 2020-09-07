package me.aungkooo.imageslider

import android.content.Context
import android.view.ViewGroup
import android.widget.Toast
import com.squareup.picasso.Picasso
import me.aungkooo.slider.RecyclerViewAdapter

class SliderRecyclerAdapter(private val context: Context, private val itemList: List<HeaderItem>) : RecyclerViewAdapter<HeaderItemView, SliderRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(context, HeaderItemView(context))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = itemList[position]
        viewHolder.setData(item)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(private val context: Context, view: HeaderItemView) : RecyclerViewAdapter.ViewHolder<HeaderItemView>(view) {
        fun setData(item: HeaderItem) {
            Picasso.with(context).load(item.imageResource)
                    .centerCrop().resize(720, 720).into(view.imageHeader)
            view.textHeader.text = item.title
            view.cardViewHeader.setOnClickListener { Toast.makeText(context, item.title, Toast.LENGTH_SHORT).show() }
        }
    }
}
