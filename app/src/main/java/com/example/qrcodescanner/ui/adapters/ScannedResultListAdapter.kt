package com.example.qrcodescanner.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
            onClicks(qrResult)
        }


        private fun onClicks(qrResult: QrResult) {
            binding.layout.setOnClickListener {
                resultDialog.show(qrResult)
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