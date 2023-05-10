package com.example.caqao

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.caqao.fragments.*
import com.google.android.material.navigation.NavigationView
import androidx.navigation.ui.setupWithNavController
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

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var navController: NavController
    private lateinit var bottomNavigation: MeowBottomNavigation
    private lateinit var drawerLayout: DrawerLayout
    private var isSelected = false
    private val sharedViewModel: CacaoDetectionViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        //drawer
        drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val navView = findViewById<NavigationView>(R.id.navView)
        navView.setupWithNavController(navController)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_nav,
            R.string.close_nav,
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // bottom nav
//        addFragment(HomeFragment.newInstance())
        bottomNavigation = findViewById(R.id.bottomNavigation)
        bottomNavigation.show(R.id.homeFragment)
        bottomNavigation.add(MeowBottomNavigation.Model(R.id.homeFragment, R.drawable.baseline_home_24))
        bottomNavigation.add(MeowBottomNavigation.Model(R.id.galleryFragment , R.drawable.ic_photo))


        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            showBottomDialog()
        }


        bottomNavigation.setOnClickMenuListener {
            when (it.id) {
                R.id.homeFragment -> {
                    replaceFragment(HomeFragment.newInstance())
                    supportActionBar!!.title = "Home"
                }
                R.id.galleryFragment -> {
                    replaceFragment(GalleryFragment.newInstance())
                    supportActionBar!!.title = "Gallery"
                }

                else -> {
                    replaceFragment(HomeFragment.newInstance())
                }
            }

        }

        //drawer layout
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, HomeFragment()).commit()
                    supportActionBar!!.title = "Home"
                    bottomNavigation.show(R.id.homeFragment, true)

                }
                R.id.AboutUsFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, AboutUsFragment()).commit()
                    supportActionBar!!.title = "About Us"
                }
                R.id.FAQFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, FAQsFragment()).commit()
                    supportActionBar!!.title = "FAQ"
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

    }

    private fun showBottomDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet_layout)

        val captureImage: LinearLayout = dialog.findViewById(R.id.cameraButton)
        val uploadImage: LinearLayout = dialog.findViewById(R.id.uploadButton)

        captureImage.setOnClickListener {
            cameraCheckPermission()
            dialog.dismiss()
        }
        uploadImage.setOnClickListener {
            galleryCheckPermission()
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setGravity(Gravity.BOTTOM)
    }


    private fun cameraCheckPermission() {
        Dexter.withContext(this)
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

        val filesDir = this.cacheDir
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "IMG_$timeStamp"

        try {
            val imageFile = File.createTempFile(fileName, ".jpg", filesDir)
            val imageUri: Uri = FileProvider.getUriForFile(this,
                "com.example.caqao.fileprovider", imageFile)
            sharedViewModel.selectImage(imageUri)
            Log.d("CaptureImageSuccess", "${imageUri}")
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(intent, com.example.caqao.CAMERA_REQUEST_CODE)

        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun showRotationalDialogForPermission() {
        AlertDialog.Builder(this)
            .setMessage("It looks like you have turned off permissions"
                    + "required for this feature. It can be enable under App settings!!!")
            .setPositiveButton("Go TO SETTINGS") { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", this.packageName, null)
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

    private fun galleryCheckPermission() {
        Dexter.withContext(this).withPermission(
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(object : PermissionListener {
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                gallery()
            }
            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                Toast.makeText(
                    this@MainActivity,
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
        startActivityForResult(intent, com.example.caqao.GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                com.example.caqao.GALLERY_REQUEST_CODE -> {
                    val uri = data?.data
                    if (uri != null) {
                        sharedViewModel.selectImage(uri)
//                        findNavController().navigate(R.id.action_homeFragment_to_assessFragment)
                        this.supportFragmentManager.beginTransaction()
                            .replace(R.id.nav_host_fragment, AssessFragment()).commit()

                    }
                }
                com.example.caqao.CAMERA_REQUEST_CODE -> {
//                    findNavController().navigate(R.id.action_homeFragment_to_assessFragment)
                    this.supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, AssessFragment()).commit()
//                    view?.findNavController()?.navigate(R.id.action_homeFragment_to_assessFragment)
                }

            }
        }
    }




    private fun replaceFragment(fragment: Fragment){
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.nav_host_fragment,fragment).commit()
    }

    private fun addFragment(fragment: Fragment){
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.add(R.id.nav_host_fragment,fragment).addToBackStack(Fragment::class.java.simpleName).commit()
    }


    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
////            val fragmentTransition = supportFragmentManager.beginTransaction()
////            fragmentTransition.add(R.id.nav_host_fragment,HomeFragment()).commit()
//            bottomNavigation.show(R.id.nav_host_fragment, true)
////            supportActionBar?.show()
////            supportActionBar!!.title = "Home"
//            if (isSelected) {
//                isSelected = false
//                bottomNavigation.show(R.id.homeFragment, true)
//            } else {
//                bottomNavigation.show(R.id.galleryFragment, true)
                onBackPressedDispatcher.onBackPressed()

        }
    }


    fun logout (menuItem: MenuItem) {
        startActivity(Intent(applicationContext, MainActivity2::class.java))
        finish()
    }


}