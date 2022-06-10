package com.rickyandrean.herbapedia.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.rickyandrean.herbapedia.databinding.ActivityLoginBinding
import com.rickyandrean.herbapedia.helper.ViewModelFactory
import com.rickyandrean.herbapedia.storage.AuthenticationPreference
import com.rickyandrean.herbapedia.ui.main.MainActivity
import com.rickyandrean.herbapedia.ui.onboarding.OnboardingActivity
import com.rickyandrean.herbapedia.ui.register.RegisterActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "authentication")

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel = ViewModelProvider(
            this@LoginActivity,
            ViewModelFactory.getInstance(AuthenticationPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]

        setupView()

        binding.fabBack.setOnClickListener {
            val intent = Intent(this, OnboardingActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
            finish()
        }

        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
            finish()
        }

        binding.btnLogin.setOnClickListener {
            loginViewModel.login(binding.tiLoginEmail.editText?.text.toString(), binding.tiPassword.editText?.text.toString())
        }

        loginViewModel.loginAccess.observe(this) {
            if (it) {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            }
        }

        loginViewModel.error.observe(this) {
            if (it != "") {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupView() {
        supportActionBar?.hide()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }
}