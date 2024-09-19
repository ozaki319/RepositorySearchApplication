package com.example.repositorysearchapplication.view.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.repositorysearchapplication.databinding.DeleteFolderDialogBinding

class DeleteFolderDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        val binding = DeleteFolderDialogBinding.inflate(requireActivity().layoutInflater)
        dialog.setContentView(binding.root)

        // 選択中フォルダ名の取得
        val currentFolderName = arguments?.getString("current_folder_name")
        binding.txtCurrentFolderName.text = currentFolderName

        // OKをクリックしたときの処理
        binding.txtOK.setOnClickListener {
            val bundle = bundleOf("click" to true)
            setFragmentResult("request_key", bundle)
            dismiss()
        }

        // キャンセルをクリックしたときの処理
        binding.txtCancel.setOnClickListener {
            dismiss()
        }

        return dialog
    }
}
