package h86355.foodrecipe.ui.fragments.detail

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import h86355.foodrecipe.R
import h86355.foodrecipe.adapters.DetailPagerAdapter
import h86355.foodrecipe.data.local.FavoriteEntity
import h86355.foodrecipe.databinding.FragmentDetailBinding
import h86355.foodrecipe.ui.fragments.detail.ingredient.IngredientFragment
import h86355.foodrecipe.ui.fragments.detail.instruction.InstructionFragment
import h86355.foodrecipe.ui.fragments.detail.overview.OverviewFragment
import h86355.foodrecipe.utils.Constants.Companion.RECIPE_BUNDLE_KEY
import h86355.foodrecipe.viewmodels.MainViewModel

@AndroidEntryPoint
class DetailFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<DetailFragmentArgs>()
    private val mainViewModel by activityViewModels<MainViewModel>()

    private var isRecipeSaved = false
    private var savedRecipeId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        setupActionBar()

        initViewPager()

        return binding.root
    }

    private fun setupActionBar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.detailToolBar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        setHasOptionsMenu(true)
    }

    private fun initViewPager() {
        val fragments = mutableListOf<Fragment>().apply {
            add(OverviewFragment())
            add(IngredientFragment())
            add(InstructionFragment())
        }

        val resultBundle = Bundle().apply {
            putParcelable(RECIPE_BUNDLE_KEY, args.result)
        }

        binding.detailViewPager.adapter =
            DetailPagerAdapter(resultBundle, fragments, requireActivity())
        TabLayoutMediator(binding.detailTabLayout, binding.detailViewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.tab_title_overview)
                1 -> tab.text = getString(R.string.tab_title_ingredient)
                2 -> tab.text = getString(R.string.tab_title_instruction)
            }
        }.attach()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail_menu, menu)
        val menuItem = menu.findItem(R.id.menu_favorite)
        checkSavedRecipeState(menuItem!!)
    }

    private fun checkSavedRecipeState(menuItem: MenuItem) {
        mainViewModel.readFavoriteRecipe().observe(viewLifecycleOwner, Observer { favoriteRecipes ->
            for (recipe in favoriteRecipes) {
                if (recipe.result.id == args.result.id) {
                    savedRecipeId = recipe.id
                    isRecipeSaved = true
                    break
                } else {
                    isRecipeSaved = false
                }
            }
            if (isRecipeSaved) {
                menuItem.icon.setTint(Color.RED)
            } else {
                menuItem.icon.setTint(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorTextSecondary
                    )
                )

            }
        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            android.R.id.home -> dismiss()

            R.id.menu_favorite -> {
                if (isRecipeSaved) {
                    removeFromFavorites(item)
                } else {
                    saveToFavorites(item)
                }
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun removeFromFavorites(item: MenuItem) {
        val favoritesEntity = FavoriteEntity(savedRecipeId, args.result)
        mainViewModel.deleteFavoriteRecipe(favoritesEntity)
        item.icon.setTint(ContextCompat.getColor(requireContext(), R.color.colorTextSecondary))
        isRecipeSaved = false
    }

    private fun saveToFavorites(item: MenuItem) {
        val favoritesEntity = FavoriteEntity(0, args.result)
        mainViewModel.insertFavoriteRecipe(favoritesEntity)
        item.icon.setTint(Color.RED)
        isRecipeSaved = true
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout = bottomSheetDialog.findViewById<View>(
                com.google.android.material.R.id.design_bottom_sheet
            )
            parentLayout?.let { bottomSheet ->
                val behavior = BottomSheetBehavior.from(bottomSheet)
                val layoutParams = bottomSheet.layoutParams
                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
                bottomSheet.layoutParams = layoutParams
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.isDraggable = false
            }
        }
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}