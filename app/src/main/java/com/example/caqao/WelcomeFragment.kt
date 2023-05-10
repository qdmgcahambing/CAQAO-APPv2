package com.example.caqao

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import com.example.caqao.databinding.FragmentWelcomeBinding


class WelcomeFragment : Fragment() {
    private var _binding: FragmentWelcomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)

        //Animation
        val topAnim = AnimationUtils.loadAnimation(requireActivity(), R.anim.top_animation)
        val bottomAnim = AnimationUtils.loadAnimation(requireActivity(), R.anim.bottom_animation)
        val botAnim = AnimationUtils.loadAnimation(requireActivity(), R.anim.bot_animation)
        val bottAnim = AnimationUtils.loadAnimation(requireActivity(), R.anim.bott_animation)

        val image = binding.caqaoLogoDark
        val text = binding.caqaoTxt
        val logo = binding.caqaoTextLogoDark
        val slogan = binding.welcomeLayout
//        val background = binding.welcomeBg
        val signup = binding.signUpBtn
        val signin = binding.signInBtn

        image.animation = topAnim
        text.animation = topAnim
        logo.animation = topAnim
        slogan.animation = bottomAnim
//        background.animation = bottomAnim
        signin.animation = botAnim
        signup.animation = bottAnim

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signInBtn.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
        }

        binding.signUpBtn.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_registerFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}