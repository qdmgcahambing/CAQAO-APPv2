package com.example.caqao.caqaodetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.caqao.R
import com.example.caqao.databinding.FragmentCacaoDetailBinding
import com.example.caqao.fragments.*
import com.example.caqao.models.USER_TOKEN
import com.google.android.material.floatingactionbutton.FloatingActionButton


class CacaoDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Actionbar title set to Image Details
        (activity as AppCompatActivity).supportActionBar?.title = "Image Details"
        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentCacaoDetailBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_cacao_detail, container, false)

        val viewModelFactory = arguments?.getInt("cacaoDetectionId")
            ?.let { CacaoDetailViewModelFactory(it) }

        // Get a reference to the ViewModel associated with this fragment.
        val cacaoDetailViewModel =
            viewModelFactory?.let {
                ViewModelProvider(
                    this, it
                ).get(CacaoDetailViewModel::class.java)
            }

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.
        binding.viewModel = cacaoDetailViewModel

        binding.setLifecycleOwner(this)

        // Showing Image Detail Dialog
        binding.cacaoDetectResult.setOnClickListener {
            val showDetectResultDialog = CacaoDetailDialogFragment()
            val args = Bundle()
            cacaoDetailViewModel!!.cacaoDetection.value?.id?.let { it1 ->
                args.putInt("cacaoDetectionId",
                    it1
                )
            }
            showDetectResultDialog.arguments = args
            showDetectResultDialog.show((activity as AppCompatActivity).supportFragmentManager,
                "showDetectResultPopUp")
        }

        // Deleting a record
        binding.deleteButton.setOnClickListener {
            // Showing Delete Dialog Fragment
            val showDeleteDialog = DeleteDialogFragment()
            val args = Bundle()
            cacaoDetailViewModel!!.cacaoDetection.value?.id?.let { it1 ->
                args.putInt("cacaoDetectionId",
                    it1
                )
            }
            showDeleteDialog.arguments = args
            showDeleteDialog.show((activity as AppCompatActivity).supportFragmentManager,"showDeletePopUp")

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Back button directs to Gallery Fragment
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, GalleryFragment()).commit()

                    val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
                    fab.visibility = View.VISIBLE
                }
            })

        // Hides Bottom navigation
        val view = requireActivity().findViewById<MeowBottomNavigation>(R.id.bottomNavigation)
        view.visibility = View.GONE

        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        fab.visibility = View.GONE
    }

}