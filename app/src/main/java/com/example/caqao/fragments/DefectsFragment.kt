package com.example.caqao.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.caqao.R
import com.example.caqao.databinding.FragmentDefectsBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DefectsFragment : Fragment() {
    private lateinit var binding: FragmentDefectsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentDefectsBinding.inflate(layoutInflater)

        binding.mouldyImgButton.setOnClickListener {
            val showMouldyDialog = HomeDialogFragment()
            val args = Bundle()
            args.putInt("layoutId", 1)
            showMouldyDialog.arguments = args
            showMouldyDialog.show((activity as AppCompatActivity).supportFragmentManager,"showMouldyPopUp")
        }

        binding.insectImgButton.setOnClickListener {
            val showInsectDialog = HomeDialogFragment()
            val args = Bundle()
            args.putInt("layoutId", 2)
            showInsectDialog.arguments = args
            showInsectDialog.show((activity as AppCompatActivity).supportFragmentManager,"showInsectPopUp")
        }

        binding.slateImgButton.setOnClickListener {
            val showSlateDialog = HomeDialogFragment()
            val args = Bundle()
            args.putInt("layoutId", 3)
            showSlateDialog.arguments = args
            showSlateDialog.show((activity as AppCompatActivity).supportFragmentManager,"showSlatePopUp")
        }

        binding.germinatedImgButton.setOnClickListener {
            val showGerminatedDialog = HomeDialogFragment()
            val args = Bundle()
            args.putInt("layoutId", 4)
            showGerminatedDialog.arguments = args
            showGerminatedDialog.show((activity as AppCompatActivity).supportFragmentManager,"showGerminatedPopUp")
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, HomeFragment()).commit()
                (activity as AppCompatActivity).supportActionBar?.title = "Home"

                val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
                fab.visibility = View.VISIBLE
            }

        })
        val view = requireActivity().findViewById<MeowBottomNavigation>(R.id.bottomNavigation)
        view.visibility = View.GONE

        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        fab.visibility = View.GONE

        (activity as AppCompatActivity).supportActionBar?.title = "Defects"
    }





}