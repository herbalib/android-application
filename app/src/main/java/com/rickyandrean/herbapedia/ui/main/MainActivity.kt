package com.rickyandrean.herbapedia.ui.main

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.navigation.NavigationBarView
import com.rickyandrean.herbapedia.R
import com.rickyandrean.herbapedia.databinding.ActivityMainBinding
import com.rickyandrean.herbapedia.ui.main.ui.home.HomeFragment
import com.rickyandrean.herbapedia.ui.main.ui.setting.SettingFragment
import com.rickyandrean.herbapedia.ui.main.ui.plants.PlantsFragment
import com.rickyandrean.herbapedia.ui.maps.MapsActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "authentication")

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        stack.add(0)

        DATA_STORE = dataStore

        binding.tvGreetingHeader.text = "Hi, $name"
        binding.navView.setOnItemSelectedListener(this)
        binding.cvMapsMaps.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_home -> {
                when (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)!!.childFragmentManager.fragments[0]) {
                    is PlantsFragment -> {
                        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
                        val navController = navHostFragment.navController
                        val extras = FragmentNavigatorExtras(findViewById<View>(R.id.cv_plants) to "home", findViewById<View>(R.id.cv_plants_search) to "home_search", findViewById<View>(R.id.cv_plants_scan) to "home_scan")

                        navController.navigate(
                            R.id.action_navigation_plants_to_navigation_home,
                            null,
                            null,
                            extras
                        )
                        updateStack(0)
                    }
                    is SettingFragment -> {
                        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
                        val navController = navHostFragment.navController

                        navController.navigate(
                            R.id.action_navigation_setting_to_navigation_home,
                            null,
                            null
                        )
                        updateStack(0)
                    }
                }
            }
            R.id.navigation_plants -> {
                when (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)!!.childFragmentManager.fragments[0]) {
                    is HomeFragment -> {
                        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
                        val navController = navHostFragment.navController
                        val extras = FragmentNavigatorExtras(findViewById<View>(R.id.cv_home) to "plants", findViewById<View>(R.id.cv_home_search) to "plants_search", findViewById<View>(R.id.cv_home_scan) to "plants_scan")

                        navController.navigate(
                            R.id.action_navigation_home_to_navigation_plants,
                            null,
                            null,
                            extras
                        )
                        updateStack(1)
                    }
                    is SettingFragment -> {
                        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
                        val navController = navHostFragment.navController

                        navController.navigate(
                            R.id.action_navigation_setting_to_navigation_plants,
                            null,
                            null
                        )
                        updateStack(1)
                    }
                }
            }
            R.id.navigation_setting -> {
                when (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)!!.childFragmentManager.fragments[0]) {
                    is HomeFragment -> {
                        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
                        val navController = navHostFragment.navController
                        val extras = FragmentNavigatorExtras(findViewById<View>(R.id.cv_home) to "setting")

                        navController.navigate(
                            R.id.action_navigation_home_to_navigation_setting,
                            null,
                            null,
                            extras
                        )
                        updateStack(2)
                    }
                    is PlantsFragment -> {
                        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
                        val navController = navHostFragment.navController

                        navController.navigate(
                            R.id.action_navigation_plants_to_navigation_setting,
                            null,
                            null
                        )
                        updateStack(2)
                    }
                }
            }
        }

        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()

        stack.removeLast()
        updateActiveBottomNavigation()
    }

    private fun updateStack(id: Int) {
        when(id) {
            0 -> {
                stack.clear()
                stack.add(0)
            }
            else -> {
                if (id in stack) {
                    stack.removeLast()
                } else {
                    stack.add(id)
                }
            }
        }

        updateActiveBottomNavigation()
    }

    private fun updateActiveBottomNavigation() {
        if (stack.isNotEmpty()) {
            binding.navView.menu.getItem(stack.last()).isChecked = true
        }
    }

    companion object {
        val stack = mutableListOf<Int>()
        var searchAnimation = false
        var token = "token"
        var name = "name"
        var lat = "lat"
        var lon = "lon"
        lateinit var DATA_STORE: DataStore<Preferences>
    }
}