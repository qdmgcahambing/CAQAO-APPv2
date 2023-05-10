package com.example.caqao.database

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.caqao.RegisterFragmentArgs
import com.example.caqao.databinding.FragmentRegisterBinding

class RegisterViewModel: Fragment() {
    private lateinit var binding: FragmentRegisterBinding
//        private var user: User? = null
    private val args by navArgs<RegisterFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater)

        binding.registerFirstname.setText(args.currentUser.firstname)
        binding.registerLastname.setText(args.currentUser.lastname)
        binding.registerEmail.setText(args.currentUser.email)
        binding.registerUsername.setText(args.currentUser.username)
        binding.registerPassword.setText(args.currentUser.password)
        binding.registerConfirmpassword.setText(args.currentUser.confirmpass)

        setHasOptionsMenu(true)

        return binding.root
    }

}