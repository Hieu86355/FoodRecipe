package h86355.foodrecipe.ui.fragments.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieDrawable
import com.example.foodrecipes.adapter.RecipeRecyclerAdapter
import com.example.foodrecipes.models.Result
import com.example.foodrecipes.utils.NetworkResult
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import h86355.foodrecipe.R
import h86355.foodrecipe.adapters.RecipeClickListener
import h86355.foodrecipe.databinding.FragmentSearchBinding
import h86355.foodrecipe.utils.hideKeyboard
import h86355.foodrecipe.utils.showKeyboard
import h86355.foodrecipe.viewmodels.MainViewModel
import h86355.foodrecipe.viewmodels.SearchViewModel

@AndroidEntryPoint
class SearchFragment : Fragment(), RecipeClickListener {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel by viewModels<MainViewModel>()
    private val searchViewModel by viewModels<SearchViewModel>()
    private val recyclerAdapter by lazy { RecipeRecyclerAdapter(this) }
    private lateinit var sheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var mode: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        mode = getString(R.string.mode)

        sheetBehavior = BottomSheetBehavior.from(binding.searchFilter.bottomSheetLayout)

        // set status bar color
        activity?.window?.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.colorStatusBar)

        setSearchListener()

        setSearchFilter()

        initRecyclerView()

        return binding.root
    }

    private fun setSearchFilter() {
        binding.searchOption.setOnClickListener {
            sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            binding.searchEdt.hideKeyboard()
        }

        binding.searchFilter.btnApply.setOnClickListener {
            applyFilter()
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.searchEdt.showKeyboard()
        }

    }

    private fun applyFilter() {
        val mealTypeChipGroup = binding.searchFilter.mealTypeLayout.mealTypeChipGroup
        val mealType = mealTypeChipGroup.findViewById<Chip>(mealTypeChipGroup.checkedChipId).text.toString()
        val dietChipGroup = binding.searchFilter.dietLayout.dietChipGroup
        val diet = dietChipGroup.findViewById<Chip>(dietChipGroup.checkedChipId).text.toString()
        searchViewModel.mealType = mealType.lowercase()
        searchViewModel.diet = diet.lowercase()

    }

    private fun initRecyclerView() {
        binding.searchList.layoutManager = LinearLayoutManager(requireContext())
        binding.searchList.adapter = recyclerAdapter
        mainViewModel.searchResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideAnimLayout()
                    response.data?.let {
                        recyclerAdapter.submitList(it.results)
                    }
                }

                is NetworkResult.Error -> {
                    showNoResultAnim()
                    Log.e("SearchFragment", "observeResponse: error: ${response.message}")
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchEdt.requestFocus()
        binding.searchEdt.showKeyboard()
    }

    private fun setSearchListener() {
        binding.searchEdt.apply {
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    binding.searchEdt.hideKeyboard()
                    binding.searchEdt.clearFocus()
                    searchApiData()
                    true
                }
                false
            }

            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    binding.searchClearText.visibility = View.VISIBLE
                } else {
                    binding.searchClearText.visibility = View.INVISIBLE
                }
            }

        }

        binding.searchClearText.setOnClickListener {
            binding.searchEdt.setText("")
        }
    }

    private fun searchApiData() {
        val searchQuery = binding.searchEdt.text.toString().lowercase()
        if (searchQuery.isNotEmpty()) {
            val clearList = mutableListOf<Result>()
            recyclerAdapter.submitList(clearList)
            searchViewModel.query = searchQuery
            mainViewModel.getSearch(searchViewModel.applyQueries())
            showLoadingAnim()
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun showLoadingAnim() {

        binding.searchList.visibility = View.GONE
        binding.searchAnimLayout.apply {
            visibility = View.VISIBLE
            when(mode) {
                "day" -> setAnimation(R.raw.searching_light)
                "night" -> setAnimation(R.raw.searching_dark)
            }
            repeatCount = LottieDrawable.INFINITE
            playAnimation()
        }
    }

    private fun hideAnimLayout() {
        binding.searchAnimLayout.visibility = View.GONE
        binding.searchList.visibility = View.VISIBLE
    }

    private fun showNoResultAnim() {
        binding.searchList.visibility = View.GONE
        binding.searchAnimLayout.apply {
            visibility = View.VISIBLE
            when(mode) {
                "day" -> setAnimation(R.raw.no_result_light)
                "night" -> setAnimation(R.raw.no_result_dark)
            }
            repeatCount = LottieDrawable.INFINITE
            playAnimation()
        }
    }

    override fun onRecipeClick(result: Result) {
        val action = SearchFragmentDirections.actionSearchFragmentToDetailFragment(result)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}