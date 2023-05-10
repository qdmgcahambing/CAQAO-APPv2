package com.example.caqao.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.caqao.CacaoDetectionListener
import com.example.caqao.CacaoGridAdapter
import com.example.caqao.R
import com.example.caqao.caqaodetail.CacaoDetailFragment
import com.example.caqao.databinding.FragmentGalleryBinding
import com.example.caqao.fragments.GridSpanSizeLookup.Companion.ITEM_VIEW_TYPE_HEADER
import com.example.caqao.fragments.GridSpanSizeLookup.Companion.ITEM_VIEW_TYPE_ITEM
import com.example.caqao.models.CacaoDetectionViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


class GalleryFragment : Fragment() {

    private val sharedViewModel: CacaoDetectionViewModel by activityViewModels()
    private lateinit var binding: FragmentGalleryBinding
    private lateinit var emptyGallery: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "Gallery"

        binding = FragmentGalleryBinding.inflate(inflater)

        binding.viewModel = sharedViewModel
        sharedViewModel.getCacaoDetections()
        binding.lifecycleOwner = this

        binding.photosGrid.adapter = CacaoGridAdapter(CacaoDetectionListener { cacaoDetectionId ->
            val args = Bundle()
            args.putInt("cacaoDetectionId", cacaoDetectionId)

            val fragment = CacaoDetailFragment()
            fragment.arguments = args

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, fragment).commit()

        })


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emptyGallery = view.findViewById(R.id.empty_gallery)

        binding.photosGrid.apply {
            layoutManager = GridLayoutManager(requireContext(), 2).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (adapter?.getItemViewType(position)) {
                            ITEM_VIEW_TYPE_HEADER -> 1
                            ITEM_VIEW_TYPE_ITEM -> 2
                            else -> 1
                        }
                    }
                }
                layoutManager = GridLayoutManager(requireContext(), 2)
                adapter = CacaoGridAdapter(CacaoDetectionListener { cacaoDetectionId ->
                    val args = Bundle()
                    args.putInt("cacaoDetectionId", cacaoDetectionId)

                    val fragment = CacaoDetailFragment()
                    fragment.arguments = args

                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, fragment).commit()
                })
                addItemDecoration(LastTwoItemBottomMarginDecorator(resources.getDimensionPixelSize(R.dimen.fab_margin2)))
            }
        }


        sharedViewModel.savedImagesCount.observe(viewLifecycleOwner) { count ->

            val numberOfSavedImages = sharedViewModel.savedImagesCount.value

            if (numberOfSavedImages != null) {
                emptyGallery.visibility = if (numberOfSavedImages == 0) View.VISIBLE else View.GONE
            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, HomeFragment()).commit()
                    (activity as AppCompatActivity).supportActionBar?.title = "Home"
                    val botnav = requireActivity().findViewById<MeowBottomNavigation>(R.id.bottomNavigation)
                    botnav.show(R.id.homeFragment, true)
                }
            })

        val view = requireActivity().findViewById<MeowBottomNavigation>(R.id.bottomNavigation)
        view.visibility = View.VISIBLE

        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        fab.visibility = View.VISIBLE
    }

    companion object {
        fun newInstance(): GalleryFragment{
            return GalleryFragment()
        }
    }

}