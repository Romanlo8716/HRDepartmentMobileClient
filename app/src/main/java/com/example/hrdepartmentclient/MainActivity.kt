package com.example.hrdepartmentclient

import android.os.Bundle
import android.view.Menu
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.hrdepartmentclient.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)



        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_worker, R.id.nav_department, R.id.nav_post, R.id.nav_adressOfDepartment, R.id.nav_adressOfDepartmentDetails, R.id.nav_postDetails, R.id.nav_departmentDetails, R.id.nav_workerDetails, R.id.nav_exit, R.id.nav_adressofdepartmentcreate, R.id.nav_adressofdepartmentupdate, R.id.nav_postcreate, R.id.nav_postupdate, R.id.nav_departmentcreate, R.id.nav_departmentupdate, R.id.nav_workercreate, R.id.nav_workerupdate
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.nav_home)
                    true
                }
                R.id.nav_worker -> {
                    findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.nav_worker)
                    true
                }
                R.id.nav_department -> {
                    findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.nav_department)
                    true
                }
                R.id.nav_post -> {
                    findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.nav_post)
                    true
                }
                R.id.nav_adressOfDepartment -> {
                    findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.nav_adressOfDepartment)
                    true
                }
                R.id.nav_exit -> {
                    // Обработка выбора "Exit"
                    finish()
                    true
                }
                else -> false
            }
        }
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}