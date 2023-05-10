package com.example.caqao

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.caqao.database.RegisterDatabase
import com.example.caqao.database.RegisterEntity
import com.example.caqao.databinding.FragmentRegisterBinding
import com.example.caqao.models.CacaoDetectionViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private var checkExistingUser = MutableLiveData<Int>()
    private val sharedViewModel: CacaoDetectionViewModel by activityViewModels()
    private var any : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentRegisterBinding>(
            inflater,
            R.layout.fragment_register,
            container,
            false
        )

        binding.signUpBtn.setOnClickListener {view: View-> view
            addUser()
        }

        binding.backBtn.setOnClickListener{
            val intent = Intent(requireActivity(), MainActivity2::class.java)
            startActivity(intent)
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }

    private fun addUser() {

        val firstname = binding.registerFirstname.text.toString()
        val lastname = binding.registerLastname.text.toString()
        val email = binding.registerEmail.text.toString()
        val username = binding.registerUsername.text.toString()
        val password = binding.registerPassword.text.toString()
        val confirmpass = binding.registerConfirmpassword.text.toString()

        if (checkUserInputs(firstname, lastname, email, username, password, confirmpass)){
            lifecycleScope.launch {
                val userAccountCreationStatus = sharedViewModel.registerUser(
                    firstname, lastname, email, username, password)
                when (userAccountCreationStatus) {
                    200 -> {
                        binding.registerUsername.error = null
                        binding.registerPassword.error = null
                        Toast.makeText(requireContext(), "Account successfully created!!",
                            Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment2)
                    }
                    401 -> {
                        binding.registerUsername.error = "Username already exists."
                        Toast.makeText(requireContext(), "Username already exists.",
                            Toast.LENGTH_SHORT).show()
                    }
                    402 -> {
                        binding.registerEmail.error = "User email already exists."
                        Toast.makeText(requireContext(), "User email already exists.",
                            Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(requireContext(), "Error: Something Went Wrong.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun checkUserInputs(
        firstname: String,
        lastname: String,
        email: String,
        username: String,
        password: String,
        confirmpass: String
    ): Boolean {

        // is password valid?
        if (!isPasswordValid(password)) {
            Toast.makeText(requireContext(), "Password must contain 8 characters and above.",
                Toast.LENGTH_SHORT).show()
            binding.registerPassword.error = "Password must contain 8 characters and above."
            return false
        }

        // does password match?
        if (confirmpass != password){
            Toast.makeText(requireContext(), "Passwords do not match.",
                Toast.LENGTH_SHORT).show()
            binding.registerConfirmpassword.error = "Passwords do not match."
            return false
        }

        // is email valid?
        if (!isEmailValid(email)) {
            Toast.makeText(requireContext(), "Email must be in correct format.",
                Toast.LENGTH_SHORT).show()
            binding.registerEmail.error = "Email must be in correct format."
            return false
        }

        // all fields must not be empty
        if (firstname.isBlank() || lastname.isBlank() || email.isBlank() || username.isBlank() ||
                password.isBlank() || confirmpass.isBlank()) {
            Toast.makeText(requireContext(), "Please fill in all the fields.",
                Toast.LENGTH_SHORT).show()
            return false
        }

        binding.registerFirstname.error = null
        binding.registerLastname.error = null
        binding.registerUsername.error = null
        binding.registerPassword.error = null
        binding.registerConfirmpassword.error = null

        return true
    }

    private fun isPasswordValid(text: String): Boolean {
        return text != null && text.length >= 8
    }

    private fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex("^([a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.[a-zA-Z]{2,})$")
        return emailRegex.matches(email)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
            }
        })
    }
}