package com.example.repositorysearchapplication.view.dialog

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.repositorysearchapplication.databinding.NewFolderDialogBinding

class NewFolderDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        val binding = NewFolderDialogBinding.inflate(requireActivity().layoutInflater)
        dialog.setContentView(binding.root)

        // OKをクリックしたときの処理
        binding.txtOK.setOnClickListener {
            if (binding.txtNewFolderName.text.isNullOrBlank()) {
                Toast.makeText(context, "フォルダ名が未入力です", Toast.LENGTH_SHORT).show()
            } else {
                val bundle = bundleOf("folder_name" to binding.txtNewFolderName.text.toString())
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
