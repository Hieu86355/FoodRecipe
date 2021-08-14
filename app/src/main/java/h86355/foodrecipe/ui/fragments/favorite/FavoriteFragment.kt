package h86355.foodrecipe.ui.fragments.favorite

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import h86355.foodrecipe.R
import h86355.foodrecipe.adapters.FavoriteRecyclerAdapter
import h86355.foodrecipe.databinding.FragmentFavoriteBinding
import h86355.foodrecipe.viewmodels.MainViewModel

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel by activityViewModels<MainViewModel>()
    private lateinit var recyclerAdapter: FavoriteRecyclerAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        recyclerAdapter = FavoriteRecyclerAdapter(binding.favToolBar, mainViewModel)

        // set status bar color
        activity?.window?.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.colorSearchView)

        setupActionBar()

        initRecyclerView()

        return binding.root
    }

    private fun initRecyclerView() {
        binding.favList.layoutManager = LinearLayoutManager(requireContext())
        binding.favList.adapter = recyclerAdapter
        mainViewModel.readFavoriteRecipe().observe(viewLifecycleOwner, Observer {
            recyclerAdapter.submitList(it)
        })
    }

    private fun setupActionBar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.favToolBar)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete_all_favorite) {
            mainViewModel.deleteAllFavorite()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerAdapter.clearContextualActionMode()
        _binding = null
    }
}