package h86355.foodrecipe.ui.fragments.home.list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodrecipes.adapter.RecipeRecyclerAdapter
import com.example.foodrecipes.models.Result
import com.example.foodrecipes.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import h86355.foodrecipe.adapters.RecipeClickListener
import h86355.foodrecipe.databinding.FragmentRecipeListBinding
import h86355.foodrecipe.ui.fragments.home.HomeFragmentDirections
import h86355.foodrecipe.utils.Constants.Companion.MEAL_TYPE_ARGUMENT_KEY
import h86355.foodrecipe.utils.observeOnce
import h86355.foodrecipe.viewmodels.HomeViewModel
import h86355.foodrecipe.viewmodels.MainViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipeListFragment : Fragment(), RecipeClickListener {

    private var _binding: FragmentRecipeListBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel by viewModels<MainViewModel>()
    private val homeViewModel by viewModels<HomeViewModel>()
    private val recyclerAdapter by lazy { RecipeRecyclerAdapter(this) }
    private var mealType = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeListBinding.inflate(inflater, container, false)

        binding.homeViewModel = homeViewModel
        binding.lifecycleOwner = this

        initRecyclerView()

        mealType = arguments?.getString(MEAL_TYPE_ARGUMENT_KEY).toString()

        homeViewModel.mealType = mealType

        readDatabase()

        return binding.root
    }

    private fun initRecyclerView() {
        binding.recipeList.adapter = recyclerAdapter
        binding.recipeList.layoutManager = LinearLayoutManager(requireContext())
        homeViewModel.showLoadingEffect()
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readRecipe(mealType)
                .observeOnce(viewLifecycleOwner, Observer { database ->
                    if (database.isNullOrEmpty()) {
                        requestApiData()
                    } else {
                        val list = mutableListOf<Result>()
                        database.forEach { recipeEntity ->
                            recipeEntity.foodRecipe.results?.let {
                                list.addAll(it)
                            }
                        }
                        recyclerAdapter.submitList(list)
                        homeViewModel.hideLoadingEffect()
                    }
                })
        }
    }

    private fun requestApiData() {
        mainViewModel.getRecipes(homeViewModel.applyQueries())
        homeViewModel.showLoadingEffect()
        mainViewModel.recipeResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is NetworkResult.Success -> {
                    homeViewModel.hideLoadingEffect()
                    response.data?.let {
                        recyclerAdapter.submitList(it.results)
                        mainViewModel.cacheRecipe(response.data, mealType)
                    }
                }

                is NetworkResult.Error -> {
                    homeViewModel.hideLoadingEffect()
                    Log.e("RecipeListFragment", "requestApiData: error: ${response.message}")
                }
            }

        })

    }

    override fun onRecipeClick(result: Result) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(result)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}