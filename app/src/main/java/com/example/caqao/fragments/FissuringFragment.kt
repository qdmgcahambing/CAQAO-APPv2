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
import com.example.caqao.databinding.FragmentFissuringBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton


class FissuringFragment : Fragment() {

    private lateinit var binding: FragmentFissuringBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentFissuringBinding.inflate(layoutInflater)

        binding.g1ImgButton.setOnClickListener {
            val showG1Dialog = HomeDialogFragment()
            val args = Bundle()
            args.putInt("layoutId", 9)
            showG1Dialog.arguments = args
            showG1Dialog.show((activity as AppCompatActivity).supportFragmentManager,"showG1PopUp")
        }

        binding.g2ImgButton.setOnClickListener {
            val showG2Dialog = HomeDialogFragment()
            val args = Bundle()
            args.putInt("layoutId", 10)
            showG2Dialog.arguments = args
            showG2Dialog.show((activity as AppCompatActivity).supportFragmentManager,"showG2PopUp")
        }

        binding.g3ImgButton.setOnClickListener {
            val showG3Dialog = HomeDialogFragment()
            val args = Bundle()
            args.putInt("layoutId", 11)
            showG3Dialog.arguments = args
            showG3Dialog.show((activity as AppCompatActivity).supportFragmentManager,"showG3PopUp")
        }

        binding.g4ImgButton.setOnClickListener {
            val showG4Dialog = HomeDialogFragment()
            val args = Bundle()
            args.putInt("layoutId", 12)
            showG4Dialog.arguments = args
            showG4Dialog.show((activity as AppCompatActivity).supportFragmentManager,"showG4PopUp")
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
        (activity as AppCompatActivity).supportActionBar?.title = "Fissuring"

        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        fab.visibility = View.GONE
    }


}