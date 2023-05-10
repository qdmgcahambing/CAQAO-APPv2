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
import com.example.caqao.databinding.FragmentBeanCuttingBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton


class BeanCuttingFragment : Fragment() {
    private lateinit var binding: FragmentBeanCuttingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentBeanCuttingBinding.inflate(layoutInflater)

        binding.step1ImgButton.setOnClickListener {
            val showStep1Dialog = HomeDialogFragment()
            val args = Bundle()
            args.putInt("layoutId", 13)
            showStep1Dialog.arguments = args
            showStep1Dialog.show((activity as AppCompatActivity).supportFragmentManager,"showStep1PopUp")
        }
        binding.step2ImgButton.setOnClickListener {
            val showStep2Dialog = HomeDialogFragment()
            val args = Bundle()
            args.putInt("layoutId", 14)
            showStep2Dialog.arguments = args
            showStep2Dialog.show((activity as AppCompatActivity).supportFragmentManager,"showStep2opUp")
        }
        binding.step3ImgButton.setOnClickListener {
            val showStep3Dialog = HomeDialogFragment()
            val args = Bundle()
            args.putInt("layoutId", 15)
            showStep3Dialog.arguments = args
            showStep3Dialog.show((activity as AppCompatActivity).supportFragmentManager,"showStep3PopUp")
        }
        binding.step4ImgButton.setOnClickListener {
            val showStep4Dialog = HomeDialogFragment()
            val args = Bundle()
            args.putInt("layoutId", 16)
            showStep4Dialog.arguments = args
            showStep4Dialog.show((activity as AppCompatActivity).supportFragmentManager,"showStep4PopUp")
        }
        binding.step5ImgButton.setOnClickListener {
            val showStep5Dialog = HomeDialogFragment()
            val args = Bundle()
            args.putInt("layoutId", 17)
            showStep5Dialog.arguments = args
            showStep5Dialog.show((activity as AppCompatActivity).supportFragmentManager,"showStep5opUp")
        }
        binding.step6ImgButton.setOnClickListener {
            val showStep6Dialog = HomeDialogFragment()
            val args = Bundle()
            args.putInt("layoutId", 18)
            showStep6Dialog.arguments = args
            showStep6Dialog.show((activity as AppCompatActivity).supportFragmentManager,"showStep6PopUp")
        }
        binding.step7ImgButton.setOnClickListener {
            val showStep7Dialog = HomeDialogFragment()
            val args = Bundle()
            args.putInt("layoutId", 19)
            showStep7Dialog.arguments = args
            showStep7Dialog.show((activity as AppCompatActivity).supportFragmentManager,"showStep7PopUp")
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

        (activity as AppCompatActivity).supportActionBar?.title = "Bean Cutting"
    }
}