package com.example.repositorysearchapplication.view.dialog

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.repositorysearchapplication.databinding.RenameFolderDialogBinding

class RenameFolderDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        val binding = RenameFolderDialogBinding.inflate(requireActivity().layoutInflater)
        dialog.setContentView(binding.root)

        // 選択中フォルダ名の取得
        val currentFolderName = arguments?.getString("current_folder_name")
        binding.txtCurrentFolderName.text = currentFolderName

        // OKをクリックしたときの処理
        binding.txtOK.setOnClickListener {
            if (binding.editNewFolderName.text.isNullOrBlank()) {
                Toast.makeText(context, "フォルダ名が未入力です", Toast.LENGTH_SHORT).show()
            } else {
                val bundle =
                    bundleOf("new_folder_name" to binding.editNewFolderName.text.toString())
                setFragmentResult("request_key", bundle)
                dismiss()
            }
        }

        // キャンセルをクリックしたときの処理
        binding.txtCancel.setOnClickListener {
            dismiss()
        }

        return dialog
    }
}
