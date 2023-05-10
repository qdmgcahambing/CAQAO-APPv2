package com.example.caqao

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.caqao.database.RegisterDatabase
import com.example.caqao.database.RegisterEntity
import com.example.caqao.databinding.FragmentLoginBinding
import com.example.caqao.models.CacaoDetectionViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val sharedViewModel: CacaoDetectionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate<FragmentLoginBinding>(inflater, R.layout.fragment_login,
            container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.signInBtn.setOnClickListener {
            checkAccount()
        }

        binding.backBtn.setOnClickListener{
            val intent = Intent(requireActivity(), MainActivity2::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    private fun checkAccount() {

        var userLoginStatus: Int? = null
        val username = binding.loginUsername.text.toString()
        val password = binding.loginPassword.text.toString()

        if (checkUserInputs(username, password)) {
            lifecycleScope.launch {
                userLoginStatus = sharedViewModel.loginUser(username, password)
                when (userLoginStatus) {
                    200 -> {
                        Toast.makeText(requireContext(), "User Login Successfully",
                            Toast.LENGTH_SHORT).show()
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                    }
                    401 -> {
                        Toast.makeText(requireContext(), "Invalid Username or Password",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun checkUserInputs(username:String, password:String): Boolean {
        if (username.isBlank()) {
            binding.loginUsername.error = "Please enter your username."
            return false
        }
        if (password.isBlank()) {
            binding.loginPassword.error = "Please enter your password."
            return false
        }
        binding.loginUsername.error = null
        binding.loginPassword.error = null
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
            }
        })
    }
}