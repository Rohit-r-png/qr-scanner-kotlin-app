package com.example.qrcodescanner.ui.mainActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.qrcodescanner.R
import com.example.qrcodescanner.database.db.QrResultDatabase
import com.example.qrcodescanner.database.entities.QrResult
import com.example.qrcodescanner.databinding.ActivityMainBinding
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setViewPagerAdapter()
        setBottomNavigation()
        setViewPagerListener()

        val qrResult = QrResult(result = "Dummy text", resultType = "Text", favorite = false, calendar = Calendar.getInstance())
        QrResultDatabase.getDatabase(this)?.getQrDao()?.insertQrResult(qrResult)
    }

    private fun setBottomNavigation() {
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
           binding.viewPager.currentItem =  when (it.itemId) {
                R.id.scanIdMenu ->  0
                R.id.recentScanMenuId ->  1
                R.id.favoritesMenuId ->  2
               else -> 0
           }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun setViewPagerAdapter() {
        binding.viewPager.adapter = MainPagerAdapter(supportFragmentManager)
    }

    private fun setViewPagerListener() {
        binding.viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int
            ) {}
            override fun onPageSelected(position: Int) {
               binding.bottomNavigationView.selectedItemId = when (position) {
                    0 -> R.id.scanIdMenu
                    1 -> R.id.recentScanMenuId
                    2 -> R.id.favoritesMenuId
                   else -> R.id.scanIdMenu
                }
            }
            override fun onPageScrollStateChanged(state: Int) {}
        })
    }
}