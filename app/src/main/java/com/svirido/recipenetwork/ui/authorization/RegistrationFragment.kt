package com.svirido.recipenetwork.ui.authorization

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.svirido.recipenetwork.databinding.FragmentRegistrationBinding
import com.svirido.recipenetwork.ui.mainscreen.MainActivity
import java.text.SimpleDateFormat
import java.util.*

private const val ERROR_REGISTRATION = "Authentication failed."
private const val PASSWORDS_DO_NOT_MATCH = "Passwords do not match"
private const val PICK_IMAGE_REQUEST = 71

class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding
    private var selectedPhotoUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registrationButton.setOnClickListener {
            when {
                TextUtils.isEmpty(binding.loginEditText.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(context, "please enter login", Toast.LENGTH_LONG)
                        .show()
                }
                TextUtils.isEmpty(binding.emailEditText.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(context, "please enter email", Toast.LENGTH_LONG)
                        .show()
                }
                TextUtils.isEmpty(binding.passwordEditText.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(context, "please enter password", Toast.LENGTH_LONG)
                        .show()
                }
                (binding.passwordEditText.text.toString() != binding.repeatPasswordEditText.text.toString()) -> {
                    Toast.makeText(context, PASSWORDS_DO_NOT_MATCH, Toast.LENGTH_LONG)
                        .show()
                }
                else -> {
                    val email: String = binding.emailEditText.text.toString().trim { it <= ' ' }
                    val password: String =
                        binding.passwordEditText.text.toString().trim { it <= ' ' }
                    registration(email, password)
                }
            }
        }
        binding.photoUser.setOnClickListener {
            galleryAddPic()
        }
    }

    private fun registration(email: String, password: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    openMainScreen()
                } else {
                    Toast.makeText(context, ERROR_REGISTRATION, Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    private fun openMainScreen() {
        startActivity(Intent(context, MainActivity::class.java))
    }

    private fun galleryAddPic() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    selectedPhotoUri = data?.data
                    binding.photoUser.setImageURI(selectedPhotoUri)

                    val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
                    val now = Date()
                    val filename = formatter.format(now)

                    val ref = FirebaseStorage.getInstance().getReference("images/$filename")
                    ref.putFile(selectedPhotoUri!!)
                }
            }
        }
    }
}

