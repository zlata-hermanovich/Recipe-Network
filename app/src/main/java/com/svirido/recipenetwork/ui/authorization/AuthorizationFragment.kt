package com.svirido.recipenetwork.ui.authorization

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.svirido.recipenetwork.R
import com.svirido.recipenetwork.databinding.FragmentAuthorizationBinding
import com.svirido.recipenetwork.repository.User.userName
import com.svirido.recipenetwork.ui.mainscreen.MainActivity

private const val ERROR_AUTHORIZATION = "Invalid username or password"

class AuthorizationFragment : Fragment() {

    private lateinit var binding: FragmentAuthorizationBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var authGoogle: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthorizationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authGoogle = Firebase.auth

        auth = FirebaseAuth.getInstance()

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    firebaseAuthWithGoogle(account.idToken!!)
                }
            } catch (e: ApiException) {
                Toast.makeText(context, "ERROR API", Toast.LENGTH_LONG).show()
            }
        }

        binding.authGmailButton.setOnClickListener {
            signInWithGoogle()
        }

        binding.registrationButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.authorizationContainer, RegistrationFragment()).commit()
        }

        binding.authorizationButton.setOnClickListener {
            if (fieldCheck()) {
                auth.signInWithEmailAndPassword(
                    binding.loginEditText.text.toString(),
                    binding.passwordEditText.text.toString()
                ).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        userName = auth.currentUser?.email.toString()
                        openMainScreen()
                    } else {
                        Toast.makeText(context, ERROR_AUTHORIZATION, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun getClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun signInWithGoogle() {
        val signInClient = getClient()
        launcher.launch(signInClient.signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                openMainScreen()
            } else {
             Toast.makeText(context,"Error",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun openMainScreen() {
        startActivity(Intent(context, MainActivity::class.java))
    }

    private fun fieldCheck(): Boolean {
        when {
            binding.loginEditText.text.isNullOrBlank() -> {
                binding.loginEditText.setHintTextColor(Color.RED)
                return false
            }
            binding.passwordEditText.text.isNullOrBlank() -> {
                binding.passwordEditText.setHintTextColor(Color.RED)
                return false
            }
        }
        return true
    }
}