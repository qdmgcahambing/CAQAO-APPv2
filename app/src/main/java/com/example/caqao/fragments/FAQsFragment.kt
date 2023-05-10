package com.example.caqao.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.caqao.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


class FAQsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private var mList = ArrayList<RecyclerViewData>()
    private lateinit var adapter : RecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_f_a_q, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        addDataToList()
        adapter = RecyclerViewAdapter(mList)
        recyclerView.adapter = adapter

        val margin = resources.getDimensionPixelSize(R.dimen.fab_margin1)
        val decorator = LastItemBottomMarginDecorator(margin)
        recyclerView.addItemDecoration(decorator)



        return view
    }

    private fun addDataToList() {
        mList.add(
            RecyclerViewData(
                "What is CAQAO?",
                context?.getString(R.string.answer1) ?: String()
            )
        )
        mList.add(
            RecyclerViewData(
                "How are cacao beans classified?",
                context?.getString(R.string.answer2) ?: String()
            )
        )
        mList.add(
            RecyclerViewData(
                "How are cacao beans graded?",
                context?.getString(R.string.answer3) ?: String()
            )
        )
        mList.add(
            RecyclerViewData(
                "What is Bean size?",
                context?.getString(R.string.answer10) ?: String()
            )
        )

        mList.add(
            RecyclerViewData(
                "How to sign up?",
                context?.getString(R.string.answer6) ?: String()
            )
        )
        mList.add(
            RecyclerViewData(
                "How to sign in?",
                context?.getString(R.string.answer7) ?: String()
            )
        )
        mList.add(
            RecyclerViewData(
                "How to capture images?",
                context?.getString(R.string.answer4) ?: String()
            )
        )
        mList.add(
            RecyclerViewData(
                "How to upload images?",
                context?.getString(R.string.answer5) ?: String()
            )
        )
        mList.add(
            RecyclerViewData(
                "How to assess images?",
                context?.getString(R.string.answer9) ?: String()
            )
        )
        mList.add(
            RecyclerViewData(
                "How to view captured images?",
                context?.getString(R.string.answer8) ?: String()
            )
        )

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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