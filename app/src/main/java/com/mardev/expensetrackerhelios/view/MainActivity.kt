package com.mardev.expensetrackerhelios.view

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.mardev.expensetrackerhelios.R
import com.mardev.expensetrackerhelios.databinding.ActivityMainBinding
import com.mardev.expensetrackerhelios.model.MyAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController =
            (supportFragmentManager.findFragmentById(R.id.hostFragment)
                    as NavHostFragment).navController
        NavigationUI.setupActionBarWithNavController(this, navController)
        binding.bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.SignInFragment, R.id.SignUpFragment -> {
                    binding.bottomNav.visibility = View.GONE
                }
                else -> {
                    binding.bottomNav.visibility = View.VISIBLE
                }
            }

            val noUpFragments = setOf(
                R.id.ItemExpenseTrack,
                R.id.ItemBudgeting,
                R.id.ItemReport,
                R.id.ItemProfile,
                R.id.SignInFragment

            )
            if (destination.id in noUpFragments) {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
            } else {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }

            supportActionBar?.title = "Helios Expense Tracker"

        }


//        val fragments:ArrayList<Fragment> = ArrayList()
//        fragments.add(EkspenseTrackerFragment())
//        fragments.add(BudgetingFragment())
//        fragments.add(ReportFragment())
//        fragments.add(UserProfileFragment())
//
//        binding.viewPager.adapter = MyAdapter(this, fragments)
//
//        binding.viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                binding.bottomNav.selectedItemId = binding.bottomNav.menu.getItem(position).itemId
//            }
//        })
//
//        binding.bottomNav.setOnItemSelectedListener {
//            if(it.itemId == R.id.ItemExpenseTrack){
//                binding.viewPager.currentItem = 0
//            }
//            else if(it.itemId == R.id.ItemBudgeting){
//                binding.viewPager.currentItem = 1
//            }
//            else if(it.itemId == R.id.ItemReport){
//                binding.viewPager.currentItem = 2
//            }
//            else{
//                binding.viewPager.currentItem = 3
//            }
//            true
//        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

}