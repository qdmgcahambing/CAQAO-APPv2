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
import com.google.android.material.floatingactionbutton.FloatingActionButton


class BeanGradeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bean_grade, container, false)
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

        (activity as AppCompatActivity).supportActionBar?.title = "Bean Grade"
    }
}