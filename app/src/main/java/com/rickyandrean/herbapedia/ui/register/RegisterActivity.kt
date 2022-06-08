package com.rickyandrean.herbapedia.ui.register

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.rickyandrean.herbapedia.databinding.ActivityRegisterBinding
import com.rickyandrean.herbapedia.ui.login.LoginActivity
import com.rickyandrean.herbapedia.ui.onboarding.OnboardingActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        binding.fabBack.setOnClickListener {
            val intent = Intent(this, OnboardingActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
            finish()
        }

        binding.btnRegister.setOnClickListener {
            val name = binding.tiFullName.editText!!.text.toString()
            val email = binding.tiEmail.editText!!.text.toString()
            val password = binding.tiPassword.editText!!.text.toString()
            val confirmPassword = binding.tiConfirmPassword.editText!!.text.toString()

            if (password == confirmPassword) {
                registerViewModel.register(name, email, password)
            } else {
                // Confirm password tidak sesuai
            }
        }

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
            finish()
        }
    }

    private fun setupView() {
        supportActionBar?.hide()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    companion object {
        private const val TAG = "RegisterActivity"
    }
}