package h86355.foodrecipe.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import h86355.foodrecipe.R
import h86355.foodrecipe.databinding.ActivityMainBinding
import h86355.foodrecipe.utils.ConnectionLiveData
import h86355.foodrecipe.viewmodels.MainViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var connectionLiveData: ConnectionLiveData
    private lateinit var lostConnectionSnackbar: Snackbar
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        lostConnectionSnackbar = Snackbar.make(
            binding.root,
            getString(R.string.lost_connection_snackbar),
            Snackbar.LENGTH_INDEFINITE
        )
        lostConnectionSnackbar.anchorView = binding.navigationView
        showLostConnectionSnackBar(mainViewModel.hasInternetConnection())

        initNavigationView()
        observeNetworkState()
        observeNavigateToSearch()

    }

    private fun observeNavigateToSearch() {
        mainViewModel.navigateToSearch.observe(this, Observer { isNavigate ->
            if (isNavigate) {
                binding.navigationView.selectedItemId = R.id.searchFragment
                mainViewModel.navigateToSearch.value = false
            }
        })
    }

    private fun observeNetworkState() {
        connectionLiveData = ConnectionLiveData(this)
        connectionLiveData.observe(this, { isNetworkAvailable ->
            showLostConnectionSnackBar(isNetworkAvailable)
        })
    }

    private fun showLostConnectionSnackBar(networkAvailable: Boolean?) {
        when (networkAvailable) {
            true -> lostConnectionSnackbar.dismiss()
            false -> lostConnectionSnackbar.show()
        }
    }

    private fun initNavigationView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.navigationView.setupWithNavController(navController)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}