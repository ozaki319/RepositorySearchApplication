package com.example.repositorysearchapplication.view.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult

class DeleteFavoriteDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog
            .Builder(requireContext())
            .apply {
                setTitle("確認")
                setMessage("お気に入りリポジトリから削除します")
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
