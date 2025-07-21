package com.example.qrcodescanner.ui.scanned_history

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qrcodescanner.R
import com.example.qrcodescanner.database.DBHelper
import com.example.qrcodescanner.database.DBHelperI
import com.example.qrcodescanner.database.db.QrResultDatabase
import com.example.qrcodescanner.database.entities.QrResult
import com.example.qrcodescanner.databinding.FragmentScannedHistoryBinding
import com.example.qrcodescanner.ui.adapters.ScannedResultListAdapter
import com.example.qrcodescanner.ui.scanner.QrScannerFragment
import com.example.qrcodescanner.utils.gone
import com.example.qrcodescanner.utils.visible

class ScannedHistoryFragment : Fragment() {

    enum class ResultLisType{
        ALL_RESULT, FAVORITE_RESULT
    }
    private lateinit var resultType : ResultLisType
    private lateinit var dbHelper : DBHelper

    lateinit var binding: FragmentScannedHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleArgument()
    }

    private fun handleArgument() {
        resultType = arguments?.getSerializable(ARGUMENT_RESULT_LIST_TYPE) as ResultLisType
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScannedHistoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        showListOfResult()
        setSwipeRefreshLayout()
        onClicks()
    }

    private fun onClicks() {
        binding.layoutHeader.removeAll.setOnClickListener{
            showRemoveAllScanResultDialog()
        }
    }

    private fun showRemoveAllScanResultDialog() {
        AlertDialog.Builder(requireContext(),R.style.CustomAlertDialog)
            .setTitle("Delete this")
            .setMessage("Are you really want to delete all record?")
            .setPositiveButton("Delete"){dialog, which ->
                clearRecords()
            }
            .setNegativeButton("Cancel"){dialog, which ->
                dialog.cancel()
            }.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearRecords() {
        when(resultType){
            ResultLisType.ALL_RESULT -> dbHelper.deleteAllQrScannedResult()
            ResultLisType.FAVORITE_RESULT -> dbHelper.deleteAllFavQrScanResults()
        }
        binding.scannedHistoryRecyclerView.adapter?.notifyDataSetChanged()
        showAllResults()
    }

    private fun setSwipeRefreshLayout() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            showListOfResult()
        }
    }

    private fun showListOfResult() {
        when(resultType){
            ResultLisType.ALL_RESULT -> {
                showAllResults()
            }
            ResultLisType.FAVORITE_RESULT -> {
                showFavoriteResults()
            }
        }
    }

    private fun showFavoriteResults() {
        val listOfFavoriteResult = dbHelper.getAllQrFavaroiteResult()
        showResult(listOfFavoriteResult)
        binding.layoutHeader.tvHeaderText.text = "Recent Favorite Results"
    }

    private fun showAllResults() {
        val listOfResult = dbHelper.getAllQrScannedResult()
        showResult(listOfResult)
        binding.layoutHeader.tvHeaderText.text = "Recent Scanned"
    }

    private fun showResult(listOfQrResults: List<QrResult>) {
        if (listOfQrResults.isEmpty()){
            showEmptyState()
        }else{
            initRecyclerView(listOfQrResults)
        }
    }

    private fun initRecyclerView(listOfQrResults: List<QrResult>) {
        binding.scannedHistoryRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.scannedHistoryRecyclerView.adapter =
            context?.let { ScannedResultListAdapter(dbHelper, it,listOfQrResults.toMutableList()) }
        showRecyclerView()
    }

    private fun showEmptyState() {
        binding.scannedHistoryRecyclerView.gone()
        binding.noResultFound.visible()
        binding.layoutHeader.removeAll.gone()
    }

    private fun showRecyclerView(){
        binding.scannedHistoryRecyclerView.visible()
        binding.noResultFound.gone()
        binding.layoutHeader.removeAll.visible()
    }

    private fun init() {
        dbHelper = QrResultDatabase.getDatabase(requireContext())?.let { DBHelperI(it) }!!
    }

    companion object {
        private const val ARGUMENT_RESULT_LIST_TYPE ="ArgumentResultListType"
        fun newInstance(screenType : ResultLisType): ScannedHistoryFragment {
            val bundle = Bundle()
            bundle.putSerializable(ARGUMENT_RESULT_LIST_TYPE,screenType)
            val fragment = ScannedHistoryFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}