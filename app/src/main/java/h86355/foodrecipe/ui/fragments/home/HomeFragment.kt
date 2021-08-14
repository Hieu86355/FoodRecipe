package h86355.foodrecipe.ui.fragments.home

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.activityViewModels
import com.google.android.material.appbar.AppBarLayout
import h86355.foodrecipe.adapters.HomePagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import h86355.foodrecipe.R
import h86355.foodrecipe.databinding.FragmentHomeBinding
import h86355.foodrecipe.ui.fragments.home.list.RecipeListFragment
import h86355.foodrecipe.utils.Constants.Companion.MEAL_TYPE_ARGUMENT_KEY
import h86355.foodrecipe.viewmodels.MainViewModel

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        initViewPager()

        binding.homeSearchTv.setOnClickListener {
            mainViewModel.navigateToSearch.value = true
        }

        /**
         * Appbar state change listener
         */
        binding.homeAppBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                //  Collapsed
                activity?.window?.statusBarColor =
                    ContextCompat.getColor(requireContext(), R.color.colorStatusBar)

                binding.homeAppBar.elevation = 12f

            } else {
                //Expanded
                activity?.window?.statusBarColor =
                    ContextCompat.getColor(requireContext(), R.color.colorSearchView)
                binding.homeAppBar.elevation = 0f
            }
        })



        return binding.root
    }

    private fun initViewPager() {
        val fragments = MutableList(7) { index ->
            createFragmentWithArgs(index)
        }
        binding.homeViewPager.adapter = HomePagerAdapter(fragments, requireActivity())
        TabLayoutMediator(binding.homeTabLayout, binding.homeViewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.tab_title_all_recipe)
                1 -> tab.text = getString(R.string.tab_title_main)
                2 -> tab.text = getString(R.string.tab_title_dessert)
                3 -> tab.text = getString(R.string.tab_title_drink)
                4 -> tab.text = getString(R.string.tab_title_breakfast)
                5 -> tab.text = getString(R.string.tab_title_dinner)
                6 -> tab.text = getString(R.string.tab_title_salad)
            }
        }.attach()

    }

    private fun createFragmentWithArgs(index: Int): RecipeListFragment {
        val recipeListFragment = RecipeListFragment()
        recipeListFragment.arguments = Bundle().apply {
            when (index) {
                0 -> putString(MEAL_TYPE_ARGUMENT_KEY, "all")
                1 -> putString(MEAL_TYPE_ARGUMENT_KEY, "main course")
                2 -> putString(MEAL_TYPE_ARGUMENT_KEY, "dessert")
                3 -> putString(MEAL_TYPE_ARGUMENT_KEY, "drink")
                4 -> putString(MEAL_TYPE_ARGUMENT_KEY, "breakfast")
                5 -> putString(MEAL_TYPE_ARGUMENT_KEY, "dinner")
                6 -> putString(MEAL_TYPE_ARGUMENT_KEY, "salad")
            }
        }
        return recipeListFragment
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}