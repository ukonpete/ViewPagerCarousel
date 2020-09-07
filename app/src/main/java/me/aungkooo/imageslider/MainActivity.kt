package me.aungkooo.imageslider

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import me.aungkooo.imageslider.databinding.ActivityMainBinding
import me.aungkooo.imageslider.fragment.BoldFragment
import me.aungkooo.imageslider.fragment.MetaphorFragment
import me.aungkooo.imageslider.fragment.MotionFragment
import me.aungkooo.slider.DepthPageTransformer
import me.aungkooo.slider.DotIndicator
import me.aungkooo.slider.FragmentAdapter
import me.aungkooo.slider.SliderBase
import java.util.*

class MainActivity : AppCompatActivity() {

    private val showViewSlider = true
    private val useRecycler = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val itemList = mutableListOf<HeaderItem>()
        val titles = arrayOf(
                getString(R.string.material_design_principles_bold),
                getString(R.string.material_design_principles_metaphor),
                getString(R.string.material_design_principles_motion)
        )

        val images = intArrayOf(R.drawable.materialdesign_principles_bold,
                R.drawable.materialdesign_principles_metaphor,
                R.drawable.materialdesign_principles_motion)

        titles.forEachIndexed { index, title ->
            itemList.add(HeaderItem(title, images[index]))
        }

        val recyclerSlider = binding.recyclerSlider
        val slider = binding.slider
        val visibleSlider = getFragmentSlider(binding)

        if (showViewSlider) {
            if (useRecycler) {
                slider.visibility = GONE
                recyclerSlider.visibility = VISIBLE
                recyclerSlider.recyclerViewAdapter = SliderRecyclerAdapter(this, itemList)
            } else {
                recyclerSlider.visibility = GONE
                slider.visibility = VISIBLE
                slider.viewAdapter = SliderAdapter(this, itemList)
            }
        } else {
            showFragmentSlider(visibleSlider)
        }
        visibleSlider.setIndicator(DotIndicator(this))
        visibleSlider.setPageTransformer(DepthPageTransformer())
    }

    private fun getFragmentSlider(binding: ActivityMainBinding): SliderBase {
        return if (useRecycler) {
            binding.recyclerSlider
        } else {
            binding.slider
        }
    }

    private fun showFragmentSlider(slider: SliderBase) {
        val fragmentList = ArrayList<Fragment>()
        val fragments = arrayOf(BoldFragment(), MetaphorFragment(), MotionFragment())
        Collections.addAll(fragmentList, *fragments)
        slider.fragmentAdapter = FragmentAdapter(supportFragmentManager, fragmentList)
    }
}