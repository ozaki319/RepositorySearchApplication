package com.example.repositorysearchapplication.view.dialog

import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.repositorysearchapplication.databinding.InsertFavoriteDialogBinding

class InsertFavoriteDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        val binding = InsertFavoriteDialogBinding.inflate(requireActivity().layoutInflater)
        dialog.setContentView(binding.root)

        // Spinnerの設定
        val favoriteFolderArray = arguments?.getStringArray("favorite_folder_list")
        if (favoriteFolderArray != null) {
            val adapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    favoriteFolderArray,
                )
            binding.spnFolder.adapter = adapter
        }

        // OKをクリックしたときの処理
        binding.txtOK.setOnClickListener {
            if (binding.spnFolder.selectedItem == null) {
                Toast.makeText(context, "フォルダが未選択です", Toast.LENGTH_SHORT).show()
            } else {
                val bundle = bundleOf("click" to true, "save_folder" to binding.spnFolder.selectedItem.toString())
                setFragmentResult("request_key", bundle)
                dismiss()
            }
        }

        // キャンセルをクリックしたときの処理
        binding.txtCancel.setOnClickListener {
            dismiss()
        }

        // 新規作成をクリックしたときの処理
        binding.txtNewFolder.setOnClickListener {
            val bundle = bundleOf("click" to false)
            setFragmentResult("request_key", bundle)
            dismiss()
        }

        return dialog
    }
}
