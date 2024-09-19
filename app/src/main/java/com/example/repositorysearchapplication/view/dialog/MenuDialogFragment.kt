package com.example.repositorysearchapplication.view.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.repositorysearchapplication.databinding.MenuDialogBinding

class MenuDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        val binding = MenuDialogBinding.inflate(requireActivity().layoutInflater)
        dialog.setContentView(binding.root)

        // 新規作成をクリックしたときの処理
        binding.txtNewFolder.setOnClickListener {
            val bundle = bundleOf("click" to "new_folder")
            setFragmentResult("request_key", bundle)
            dismiss()
        }

        // フォルダ名変更をクリックしたときの処理
        binding.txtFolderRename.setOnClickListener {
            val bundle = bundleOf("click" to "rename_folder")
            setFragmentResult("request_key", bundle)
            dismiss()
        }

        // フォルダ削除をクリックしたときの処理
        binding.txtFolderDelete.setOnClickListener {
            val bundle = bundleOf("click" to "delete_folder")
            setFragmentResult("request_key", bundle)
            dismiss()
        }

        return dialog
    }
}
