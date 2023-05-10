package com.example.caqao.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.caqao.R
import com.example.caqao.databinding.FragmentResultsBinding
import com.example.caqao.models.CacaoDetectionViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ResultsFragment : Fragment() {

    private var binding: FragmentResultsBinding? = null
    private val sharedViewModel: CacaoDetectionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentResultsBinding.inflate(inflater, container, false)
        binding = fragmentBinding


        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner
            saveButton.setOnClickListener {
                viewModel?.saveAssessmentResults()
                Toast.makeText(activity, "Image successfully saved!", Toast.LENGTH_SHORT).show()

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, HomeFragment()).commit()
                (activity as AppCompatActivity).supportActionBar?.title = "Home"

                val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
                fab.visibility = View.VISIBLE
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, HomeFragment()).commit()
                (activity as AppCompatActivity).supportActionBar?.title = "Home"

                val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
                fab.visibility = View.VISIBLE
            }

        })

        (activity as AppCompatActivity).supportActionBar?.title = "Results"

        val view = requireActivity().findViewById<MeowBottomNavigation>(R.id.bottomNavigation)
        view.visibility = View.GONE

        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        fab.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sharedViewModel.resetCacaoDetection()
        binding = null
    }


}