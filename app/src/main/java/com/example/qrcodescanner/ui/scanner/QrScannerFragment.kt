package com.example.qrcodescanner.ui.scanner

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.qrcodescanner.R
import com.example.qrcodescanner.database.DBHelper
import com.example.qrcodescanner.database.DBHelperI
import com.example.qrcodescanner.database.db.QrResultDatabase
import com.example.qrcodescanner.databinding.FragmentScannerBinding
import com.example.qrcodescanner.ui.dialog.QrCodeResultDialog
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.ViewfinderView

class QrScannerFragment : Fragment() {

    private lateinit var binding: FragmentScannerBinding
    private lateinit var scannerView: DecoratedBarcodeView
    private var hasScanned = false

    private lateinit var qrResultDialog: QrCodeResultDialog
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initViews()
        onClick()
    }

    private fun init() {
        dbHelper = context?.let { QrResultDatabase.getDatabase(it)?.let { DBHelperI(it) } }!!
    }

    private fun initViews() {
        initializeQrScanner()
        setResultDialog()
    }

    private fun setResultDialog() {
        qrResultDialog = context?.let { QrCodeResultDialog(it) }!!
        qrResultDialog.setOnDismissListener(object : QrCodeResultDialog.onDismisListener {
            override fun onDismiss() {
                scannerView.resume()
            }

        })
    }

    private fun onClick() {
        binding.flashToggle.setOnClickListener {
            if (it.isSelected) {
                offFlashLight()
            } else {
                onFlashLight()
            }
        }
    }

    private fun onFlashLight() {
        scannerView.setTorchOn()
        binding.flashToggle.setImageResource(R.drawable.ic_flash_on)
        binding.flashToggle.isSelected = true
    }

    private fun offFlashLight() {
        scannerView.setTorchOff()
        binding.flashToggle.setImageResource(R.drawable.ic_flash_off)
        binding.flashToggle.isSelected = false
    }

    private val barcodeCallback: BarcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: com.journeyapps.barcodescanner.BarcodeResult?) {
            if (hasScanned) return

            result?.text?.let { scannedText ->
                hasScanned = true
                scannerView.pause()

                onQrResult(result.text)

                scannerView.postDelayed({
                    hasScanned = false

                }, 2000)
            }
        }

        private fun onQrResult(text: String?) {
            if (text.isNullOrEmpty()) {
                Toast.makeText(context, "Empty Qr Code", Toast.LENGTH_LONG).show()
            } else {
                saveToDatabase(text)
            }
        }

        private fun saveToDatabase(result: String) {
            val inserterRowId = dbHelper.insertQrResult(result)
            val qrResult = dbHelper.getQrResult(inserterRowId)
            qrResultDialog.show(qrResult)
        }

        override fun possibleResultPoints(resultPoints: MutableList<com.google.zxing.ResultPoint>?) {
            // Optional: can be ignored
        }
    }

    private fun initializeQrScanner() {
        scannerView = DecoratedBarcodeView(requireContext())
        scannerView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val viewfinderView = scannerView.viewFinder as ViewfinderView
        viewfinderView.setMaskColor(Color.parseColor("#66000000"))

        binding.barcodeScanner.addView(scannerView)
        scannerView.decodeContinuous(barcodeCallback)
    }

    override fun onResume() {
        super.onResume()
        hasScanned = false
        scannerView.resume()
    }

    override fun onPause() {
        scannerView.pause()
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        scannerView.pause()
    }

    companion object {
        fun newInstance(): QrScannerFragment {
            return QrScannerFragment()
        }
    }

}
