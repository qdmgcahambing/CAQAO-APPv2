package com.example.caqao.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.caqao.R
import com.example.caqao.databinding.FragmentHomeBinding
import com.example.caqao.models.CacaoDetectionViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


private const val CAMERA_REQUEST_CODE = 1
private const val GALLERY_REQUEST_CODE = 2

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private val sharedViewModel: CacaoDetectionViewModel by activityViewModels()

    private var clicked = false

    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(activity, R.anim.rotate_open_anim)
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(activity, R.anim.rotate_close_anim)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentHomeBinding.inflate(inflater, container, false)
        binding = fragmentBinding

        fragmentBinding.defectsCard.setOnClickListener { view : View ->
            //view.findNavController().navigate(R.id.action_homeFragment_to_defectsFragment)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, DefectsFragment()).commit()
        }
        fragmentBinding.colorCard.setOnClickListener { view : View ->
            //view.findNavController().navigate(R.id.action_homeFragment_to_colorFragment)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, ColorFragment()).commit()
        }
        fragmentBinding.fissuringCard.setOnClickListener { view : View ->
            //view.findNavController().navigate(R.id.action_homeFragment_to_fissuringFragment)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, FissuringFragment()).commit()
        }

        fragmentBinding.beangradingCard.setOnClickListener { view : View ->
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, BeanGradeFragment()).commit()
        }

        fragmentBinding.beancuttingCard.setOnClickListener { view : View ->
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, BeanCuttingFragment()).commit()
        }

        val view = requireActivity().findViewById<MeowBottomNavigation>(R.id.bottomNavigation)
        view.visibility = View.VISIBLE

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner
//            moreButton.setOnClickListener { onMoreButtonClicked() }
//            uploadButton.setOnClickListener { galleryCheckPermission() }
//            cameraButton.setOnClickListener { cameraCheckPermission() }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
//                    val botnav = requireActivity().findViewById<MeowBottomNavigation>(R.id.bottomNavigation)
//                    botnav.show(R.id.homeFragment, true)
                }
            })

        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        fab.visibility = View.VISIBLE
    }

    private fun galleryCheckPermission() {
        Dexter.withContext(requireContext()).withPermission(
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(object : PermissionListener {
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                gallery()
            }
            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                Toast.makeText(
                    requireContext(),
                    "You have denied the storage permission to select image",
                    Toast.LENGTH_SHORT
                ).show()
                showRotationalDialogForPermission()
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: PermissionRequest?, p1: PermissionToken?) {
                showRotationalDialogForPermission()
            }
        }).onSameThread().check()
    }

    private fun gallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/jpeg"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    private fun cameraCheckPermission() {
        Dexter.withContext(requireContext())
            .withPermissions(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA).withListener(
                object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if (report.areAllPermissionsGranted()) {
                                camera()
                            }
                        }
                    }
                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        p1: PermissionToken?) {
                        showRotationalDialogForPermission()
                    }
                }
            ).onSameThread().check()
    }

    @SuppressLint("LongLogTag")
    private fun camera() {

        val filesDir = requireContext().cacheDir
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "IMG_$timeStamp"

        try {
            val imageFile = File.createTempFile(fileName, ".jpg", filesDir)
            val imageUri: Uri = FileProvider.getUriForFile(requireContext(),
                "com.example.caqao.fileprovider", imageFile)
            sharedViewModel.selectImage(imageUri)
            Log.d("CaptureImageSuccess", "${imageUri}")
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(intent, CAMERA_REQUEST_CODE)

        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {
                    val uri = data?.data
                    if (uri != null) {
                        sharedViewModel.selectImage(uri)
//                        findNavController().navigate(R.id.action_homeFragment_to_assessFragment)
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.nav_host_fragment, AssessFragment()).commit()

                    }
                }
                CAMERA_REQUEST_CODE -> {
//                    findNavController().navigate(R.id.action_homeFragment_to_assessFragment)
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, AssessFragment()).commit()
//                    view?.findNavController()?.navigate(R.id.action_homeFragment_to_assessFragment)
                }

            }
        }
    }

    private fun showRotationalDialogForPermission() {
        AlertDialog.Builder(requireContext())
            .setMessage("It looks like you have turned off permissions"
                    + "required for this feature. It can be enable under App settings!!!")
            .setPositiveButton("Go TO SETTINGS") { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", requireContext().packageName, null)
                    intent.data = uri
                    startActivity(intent)

                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("CANCEL") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

//    private fun onMoreButtonClicked() {
//        setVisibility(clicked)
//        clicked = !clicked
//    }

//    private fun setVisibility(clicked: Boolean) {
//        if(!clicked){
//            binding?.cameraButton?.visibility = View.VISIBLE
//            binding?.uploadButton?.visibility = View.VISIBLE
//            binding?.moreButton?.startAnimation(rotateOpen)
//        }else{
//            binding?.cameraButton?.visibility = View.GONE
//            binding?.uploadButton?.visibility = View.GONE
//            binding?.moreButton?.startAnimation(rotateClose)
//        }
//    }

    companion object {
        @JvmStatic
        fun newInstance()=
            HomeFragment().apply { arguments=Bundle().apply {  } }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}