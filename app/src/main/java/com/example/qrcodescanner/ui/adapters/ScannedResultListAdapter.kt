package com.example.qrcodescanner.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.qrcodescanner.R
import com.example.qrcodescanner.database.DBHelper
import com.example.qrcodescanner.database.entities.QrResult
import com.example.qrcodescanner.databinding.LayoutHeaderHistoryBinding
import com.example.qrcodescanner.databinding.LayoutSingleItemQrResultBinding
import com.example.qrcodescanner.ui.dialog.QrCodeResultDialog
import com.example.qrcodescanner.ui.scanned_history.ScannedHistoryFragment
import com.example.qrcodescanner.utils.gone
import com.example.qrcodescanner.utils.toFormatedDisplay
import com.example.qrcodescanner.utils.visible

class ScannedResultListAdapter(
    var dbHelper: DBHelper,
    private var context: Context,
    private var listOfScannedResult: MutableList<QrResult>,

) : RecyclerView.Adapter<ScannedResultListAdapter.ScannedResultListViewHolder>() {

    private var resultDialog: QrCodeResultDialog = QrCodeResultDialog(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScannedResultListViewHolder {
        return ScannedResultListViewHolder(
            LayoutSingleItemQrResultBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listOfScannedResult.size
    }

    override fun onBindViewHolder(holder: ScannedResultListViewHolder, position: Int) {
        holder.bind(listOfScannedResult[position],position)
    }


    inner class ScannedResultListViewHolder(var binding: LayoutSingleItemQrResultBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(qrResult: QrResult, position: Int) {
            binding.result.text = qrResult.result
            binding.tvTime.text = qrResult.calendar.toFormatedDisplay()
            setFavorite(qrResult.favorite)
            onClicks(qrResult,position)
        }


        private fun onClicks(qrResult: QrResult, position: Int) {
            binding.layout.setOnClickListener {
                resultDialog.show(qrResult)
            }

             binding.layout.setOnLongClickListener {
                 showDeleteDialog(qrResult,position)
                 return@setOnLongClickListener true
             }
        }

        private fun showDeleteDialog(qrResult: QrResult, position: Int) {
            AlertDialog.Builder(context,R.style.CustomAlertDialog)
                .setTitle("Delete this")
                .setMessage("Are you really want to delete this record?")
                .setPositiveButton("Delete"){dialog, which ->
                    deleteThisRecord(qrResult,position)
                }
                .setNegativeButton("Cancel"){dialog, which ->
                    dialog.cancel()
                }.show()
        }

        private fun deleteThisRecord(qrResult: QrResult, position: Int) {
            if (position in 0 until listOfScannedResult.size) {
                qrResult.id?.let { dbHelper.deleteQrResult(it) }
                listOfScannedResult.removeAt(position)
                notifyItemRemoved(position)
            } else {
                Log.e("ScannedResultAdapter", "Invalid delete: position=$position, list size=${listOfScannedResult.size}")
            }
        }

        private fun setFavorite(favorite: Boolean?) {
            if (favorite == true){
                binding.favouriteIcon.visible()
            }else{
                binding.favouriteIcon.gone()
            }
        }
    }

}