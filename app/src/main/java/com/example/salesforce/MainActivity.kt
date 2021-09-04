package com.example.salesforce

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.salesforce.databinding.ActivityMainBinding
import com.example.salesforce.ui.FavoritesFragment
import com.example.salesforce.ui.HomeFragment
import com.example.salesforce.util.SalesforceConstants.CURRENT_SEARCH
import com.example.salesforce.util.SalesforceConstants.CURRENT_SELECTION
import com.example.salesforce.viewmodel.SalesForceViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: SalesForceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this)[SalesForceViewModel::class.java]

        binding.bottomNavigation.setOnNavigationItemSelectedListener(this)
        setCurrentFragment(HomeFragment.newInstance())
        currentSelection = R.id.home
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when {
            item.itemId != currentSelection && item.itemId == R.id.home -> {
                currentSelection = R.id.home
                setCurrentFragment(HomeFragment.newInstance())
            }
            item.itemId != currentSelection && item.itemId == R.id.favorites -> {
                currentSelection = R.id.favorites
                setCurrentFragment(FavoritesFragment.newInstance())
            }
        }
        return true
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.homeContainer, fragment)
            commit()
        }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
        outState.putInt(CURRENT_SELECTION, currentSelection)
        outState.putString(CURRENT_SEARCH, viewModel.currentSearch)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentSelection = savedInstanceState.getInt(CURRENT_SELECTION)
        binding.bottomNavigation.selectedItemId = currentSelection

        when (currentSelection) {
            R.id.home -> {
                setCurrentFragment(HomeFragment.newInstance())
            }
            else -> {
                setCurrentFragment(FavoritesFragment.newInstance())
            }
        }
    }

    companion object {
        private var currentSelection = 0
    }
}

