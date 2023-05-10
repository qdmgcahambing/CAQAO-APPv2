package com.example.caqao.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.content.FileProvider.getUriForFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.caqao.R
import com.example.caqao.databinding.FragmentAssessBinding
import com.example.caqao.models.CacaoDetectionViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class AssessFragment : Fragment() {

    private var binding: FragmentAssessBinding? = null
    private val sharedViewModel: CacaoDetectionViewModel by activityViewModels()

        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragmentBinding = FragmentAssessBinding.inflate(inflater, container, false)
        binding = fragmentBinding

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner
            assessBtn.setOnClickListener {
                assessCacaoBeans()
            }
            captureAgainBtn.setOnClickListener {
               //  sharedViewModel.selectImage(null)
//                cameraCheckPermission()
//                Toast.makeText(
//                    requireContext(),
//                    "Invalid Image: Capture Image Again",
//                    Toast.LENGTH_SHORT
//                ).show()

                //return to camera
//                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)

             requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, HomeFragment()).commit()
                (activity as AppCompatActivity).supportActionBar?.title = "Home"
            }
        }
        sharedViewModel.validateImage(requireContext(), requireContext().contentResolver)

        // Back button directs to Home Fragment
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, HomeFragment()).commit()
                // Actionbar title set to Home
                (activity as AppCompatActivity).supportActionBar?.title = "Home"

                val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
                fab.visibility = View.VISIBLE
            }
        })
        // Hidden Bottom navigation
        val botnav = requireActivity().findViewById<MeowBottomNavigation>(R.id.bottomNavigation)
        botnav.visibility = View.GONE

        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        fab.visibility = View.GONE

        // Actionbar title set to Assess Capture
        (activity as AppCompatActivity).supportActionBar?.title = "Assess Capture"
    }


    fun assessCacaoBeans() {
        when {
            TextUtils.isEmpty(binding?.beanCountInputEditText?.text.toString()) -> {
                binding?.beanCountInputEditText?.error = "Required field is empty"
                Toast.makeText(
                    requireContext(),
                    "Please input bean count",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
                val beanSize = binding?.beanCountInputEditText?.text.toString().toInt()
                sharedViewModel.assessCacaoBeans(requireContext(), requireContext().contentResolver,
                    beanSize)
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, ResultsFragment()).commit()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        private const val CAMERA_REQUEST_CODE = 1
        private const val GALLERY_REQUEST_CODE = 2
    }


}
