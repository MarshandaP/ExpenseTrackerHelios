package com.mardev.expensetrackerhelios.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.mardev.expensetrackerhelios.R
import com.mardev.expensetrackerhelios.databinding.ActivityMainBinding
import com.mardev.expensetrackerhelios.model.MyAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragments:ArrayList<Fragment> = ArrayList()
        fragments.add(EkspenseTrackerFragment())
        fragments.add(BudgetingFragment())
        fragments.add(ReportFragment())
        fragments.add(UserProfileFragment())

        binding.viewPager.adapter = MyAdapter(this, fragments)

        binding.viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.bottomNav.selectedItemId = binding.bottomNav.menu.getItem(position).itemId
            }
        })

        binding.bottomNav.setOnItemSelectedListener {
            if(it.itemId == R.id.ItemExpenseTrack){
                binding.viewPager.currentItem = 0
            }
            else if(it.itemId == R.id.ItemBudgeting){
                binding.viewPager.currentItem = 1
            }
            else if(it.itemId == R.id.ItemReport){
                binding.viewPager.currentItem = 2
            }
            else{
                binding.viewPager.currentItem = 3
            }
            true
        }

    }
}