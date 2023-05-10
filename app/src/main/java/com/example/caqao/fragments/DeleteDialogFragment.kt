package com.example.caqao.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.caqao.R
import com.example.caqao.caqaodetail.CacaoDetailViewModel
import com.example.caqao.caqaodetail.CacaoDetailViewModelFactory
import com.example.caqao.databinding.FragmentDeleteDialogBinding


class DeleteDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentDeleteDialogBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_delete_dialog, container, false)

        val viewModelFactory = arguments?.getInt("cacaoDetectionId")
            ?.let { CacaoDetailViewModelFactory(it) }

        // Get a reference to the ViewModel associated with this fragment.
        val cacaoDetailViewModel =
            viewModelFactory?.let {
                ViewModelProvider(
                    this, it
                ).get(CacaoDetailViewModel::class.java)
            }

        // Delete record
        binding.deleteBtn.setOnClickListener {
            arguments?.getInt("cacaoDetectionId")
                ?.let { it1 ->
                    if (cacaoDetailViewModel != null) {
                        cacaoDetailViewModel.deleteCacaoDetectionWithId(it1)
                    }
                }
            Toast.makeText(requireContext(), "Record Deleted",
                Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, GalleryFragment()).commit()

            dismiss()
        }


        //Cancel
        binding.cancelBtn.setOnClickListener {
            dismiss()
        }

        binding.viewModel = cacaoDetailViewModel

        binding.setLifecycleOwner(this)

        return binding.root
    }


}