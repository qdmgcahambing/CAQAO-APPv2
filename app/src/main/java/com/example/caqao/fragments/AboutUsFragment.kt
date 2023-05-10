package com.example.caqao.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.caqao.MainActivity
import com.example.caqao.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AboutUsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Actionbar title set to About Us
        (activity as AppCompatActivity).supportActionBar?.title = "About Us"
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_us, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Back button directs to Home Fragment
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, HomeFragment()).commit()
                val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
                fab.visibility = View.VISIBLE

                val botnav = requireActivity().findViewById<MeowBottomNavigation>(R.id.bottomNavigation)
                botnav.show(R.id.homeFragment, true)
                (activity as AppCompatActivity).supportActionBar?.title = "Home"
            }

        })

        val view = requireActivity().findViewById<MeowBottomNavigation>(R.id.bottomNavigation)
        view.visibility = View.GONE

        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        fab.visibility = View.GONE
    }
}