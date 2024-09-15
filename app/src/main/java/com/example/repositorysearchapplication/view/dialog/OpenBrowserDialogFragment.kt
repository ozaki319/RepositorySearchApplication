package com.example.repositorysearchapplication.view.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult

class OpenBrowserDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog
            .Builder(requireContext())
            .apply {
                setTitle("確認")
                setMessage("アプリを離れて外部ブラウザを開きます")
                setPositiveButton("OK") { _, _ ->
                    val bundle = bundleOf("click" to true)
                    setFragmentResult("request_key", bundle)
                }
                setNegativeButton("キャンセル") { _, _ ->
                    val bundle = bundleOf("click" to false)
                    setFragmentResult("request_key", bundle)
                }
            }.create()
}
