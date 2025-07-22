package com.example.qrcodescanner.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.qrcodescanner.R
import com.example.qrcodescanner.database.DBHelper
import com.example.qrcodescanner.database.DBHelperI
import com.example.qrcodescanner.database.db.QrResultDatabase
import com.example.qrcodescanner.database.entities.QrResult
import com.example.qrcodescanner.utils.toFormatedDisplay

class QrCodeResultDialog(private var context: Context) {

    private lateinit var dialog: Dialog
    private var qrResult: QrResult? = null
    private lateinit var scannedDate: TextView
    private lateinit var favIcon: ImageView
    private lateinit var share: ImageView
    private lateinit var copyResult: ImageView
    private lateinit var closeDialog: ImageView
    private lateinit var scannedText: TextView

    private var onDismissListener: onDismisListener? = null
    private lateinit var dbHelper: DBHelper

    init {
        init()
        initDialog()
    }

    private fun init() {
        dbHelper = QrResultDatabase.getDatabase(context)?.let { DBHelperI(it) }!!
    }

    fun setOnDismissListener(onDismissListener: onDismisListener) {
        this.onDismissListener = onDismissListener
    }

    private fun initDialog() {
        dialog = Dialog(context)
        dialog.setContentView(R.layout.layout_qr_result_show)
        dialog.setCancelable(false)

        scannedDate = dialog.findViewById(R.id.scannedDate)
        favIcon = dialog.findViewById(R.id.favouriteIcon)
        share = dialog.findViewById(R.id.shareResult)
        copyResult = dialog.findViewById(R.id.copyResult)
        closeDialog = dialog.findViewById(R.id.cancelDialog)
        scannedText = dialog.findViewById(R.id.scannedText)

        onClicks()
    }

    @SuppressLint("SetTextI18n")
    fun show(qrResult: QrResult) {
        this.qrResult = qrResult
        scannedDate.text = qrResult.calendar.toFormatedDisplay()
        val resultText = qrResult.result?.trim()
        scannedText.text = resultText
        favIcon.isSelected = qrResult.favorite == true

        scannedText.paint.isUnderlineText = true
        scannedText.setTextColor(context.getColor(R.color.white))

        scannedText.setOnClickListener {
            resultText?.let {
                launchIntent(resultText)
            }
        }

        dialog.show()
    }

    private fun launchIntent(text: String?) {
        val cleaned = text?.trim()?.replace("\n", "")
        if (cleaned.isNullOrEmpty()) {
            Toast.makeText(context, "Invalid or empty link", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            val uri = Uri.parse(cleaned)
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "No app can handle this link", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }


    private fun onClicks() {
        favIcon.setOnClickListener {
            if (favIcon.isSelected) {
                removeFromFavorite()
            } else {
                addToFavorite()
            }
        }

        share.setOnClickListener {
            shareResult()
        }

        copyResult.setOnClickListener {
            copyResultToClipboard()
        }

        closeDialog.setOnClickListener {
            onDismissListener?.onDismiss()
            dialog.dismiss()
        }
    }

    private fun addToFavorite() {
        favIcon.isSelected = true
        qrResult?.id?.let { dbHelper.addToFavorite(it) }
    }

    private fun removeFromFavorite() {
        favIcon.isSelected = false
        qrResult?.id?.let { dbHelper.removeFromFavorite(it) }
    }

    private fun shareResult() {
        val textIntent = Intent(Intent.ACTION_SEND)
        textIntent.type = "text/plain"
        textIntent.putExtra(Intent.EXTRA_TEXT, scannedText.text.toString())
        context.startActivity(Intent.createChooser(textIntent, "Share scanned result"))
    }

    @SuppressLint("ServiceCast")
    private fun copyResultToClipboard() {
        scannedText.clearFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(scannedText.windowToken, 0)

        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("QrScannerResult", scannedText.text.toString())
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    interface onDismisListener {
        fun onDismiss()
    }
}
