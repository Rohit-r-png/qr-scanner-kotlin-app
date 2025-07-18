package com.example.qrcodescanner.ui.dialog

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu.OnDismissListener
import com.example.qrcodescanner.R
import com.example.qrcodescanner.database.DBHelper
import com.example.qrcodescanner.database.DBHelperI
import com.example.qrcodescanner.database.db.QrResultDatabase
import com.example.qrcodescanner.database.entities.QrResult
import com.example.qrcodescanner.utils.toFormatedDisplay

class QrCodeResultDialog(private var context: Context) {



    private lateinit var dialog : Dialog
    private var qrResult : QrResult? = null
    private lateinit var scannedDate: TextView
    private lateinit var favIcon: ImageView
    private lateinit var share: ImageView
    private lateinit var copyResult: ImageView
    private lateinit var closeDialog: ImageView
    private lateinit var scannedText: TextView

    private var onDismissListener: onDismisListener? = null
    private lateinit var dbHelper : DBHelper

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

    fun show(qrResult: QrResult){
        this.qrResult = qrResult
        scannedDate.text = qrResult.calendar.toFormatedDisplay()
        scannedText.text = qrResult.result
        favIcon.isSelected = qrResult.favorite == true
        dialog.show()
    }

    private fun onClicks() {
        favIcon.setOnClickListener {
            if (favIcon.isSelected){
                removeFromFavorite()
            }else{
                addToFavorite()
            }
        }
        share.setOnClickListener {
            shareResult()
        }

        copyResult.setOnClickListener {
            copyResultToClipBoard()
        }

        closeDialog.setOnClickListener {
            onDismissListener?.onDismiss()
            dialog.dismiss()
        }

    }

    private fun addToFavorite() {
        if (favIcon.isSelected){
            qrResult?.id?.let { dbHelper.addToFavorite(it) }
        }
    }

    private fun removeFromFavorite() {
        favIcon.isSelected = false
        qrResult?.id?.let { dbHelper.addToFavorite(it) }    }

    private fun shareResult() {
        val textIntent = Intent(Intent.ACTION_SEND)
        textIntent.type = "text/plain"
        textIntent.putExtra(Intent.EXTRA_TEXT,scannedText.text)
        context.startActivity(textIntent)
    }

    private fun copyResultToClipBoard() {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("QrScannerResult",scannedText.text)
        clipboard.text = clip.getItemAt(0).text.toString()
        Toast.makeText(context,"Copied to clipboard",Toast.LENGTH_LONG).show()
    }

    interface onDismisListener{
        fun onDismiss()
    }

}