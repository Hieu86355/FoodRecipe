package h86355.foodrecipe.ui.fragments.detail.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodrecipes.models.Result
import h86355.foodrecipe.databinding.FragmentOverviewBinding

class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)

        val result = arguments?.getParcelable<Result>("recipeBundle")
        result?.let {
            binding.result = it
            binding.lifecycleOwner = this
        }

        return binding.root
    }

}