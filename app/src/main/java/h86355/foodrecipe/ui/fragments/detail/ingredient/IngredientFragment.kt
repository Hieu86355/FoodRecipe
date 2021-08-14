package h86355.foodrecipe.ui.fragments.detail.ingredient

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodrecipes.models.Result
import h86355.foodrecipe.R
import h86355.foodrecipe.adapters.IngredientRecyclerAdapter
import h86355.foodrecipe.databinding.FragmentIngredientBinding

class IngredientFragment : Fragment() {

    private var _binding: FragmentIngredientBinding? = null
    private val binding get() = _binding!!
    private val ingredientAdapter by lazy { IngredientRecyclerAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIngredientBinding.inflate(inflater, container, false)

        initRecyclerView()

        return binding.root
    }

    private fun initRecyclerView() {
        binding.ingredientList.layoutManager = LinearLayoutManager(requireContext())
        binding.ingredientList.adapter = ingredientAdapter

        val divider = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider_layer)!!)
        binding.ingredientList.addItemDecoration(divider)

        val result = arguments?.getParcelable<Result>("recipeBundle")
        result?.extendedIngredients?.let {
            if (it.isNotEmpty()) {
                ingredientAdapter.submitList(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}