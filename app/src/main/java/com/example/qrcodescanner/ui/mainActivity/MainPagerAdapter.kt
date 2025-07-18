package com.example.qrcodescanner.ui.mainActivity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.qrcodescanner.ui.scanned_history.ScannedHistoryFragment
import com.example.qrcodescanner.ui.scanner.QrScannerFragment

class MainPagerAdapter(var fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> QrScannerFragment.newInstance()
            1 -> ScannedHistoryFragment.newInstance(ScannedHistoryFragment.ResultLisType.ALL_RESULT)
            2 -> ScannedHistoryFragment.newInstance(ScannedHistoryFragment.ResultLisType.FAVORITE_RESULT)
            else -> QrScannerFragment.newInstance()
        }
    }
}