package h86355.foodrecipe.adapters

import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import h86355.foodrecipe.R
import h86355.foodrecipe.data.local.FavoriteEntity
import h86355.foodrecipe.databinding.RowFavoriteBinding
import h86355.foodrecipe.ui.fragments.favorite.FavoriteFragmentDirections
import h86355.foodrecipe.viewmodels.MainViewModel

class FavoriteRecyclerAdapter(
    val toolbar: Toolbar,
    val mainViewModel: MainViewModel,
) :
    ListAdapter<FavoriteEntity, FavoriteRecyclerAdapter.FavoriteHolder>(FavoriteDiffCallback()),
    ActionMode.Callback {

    // Holds the layout positions of the selected items.
    val selectedItems = ObservableArrayList<Int>()
    // Holds a Boolean indicating if multi selection is enabled. In a LiveData.
    var isMultiSelectMode: MutableLiveData<Boolean> = MutableLiveData(false)

    private lateinit var actionMode: ActionMode
    private var viewholders = arrayListOf<FavoriteHolder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteHolder {
        return FavoriteHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FavoriteHolder, position: Int) {
        holder.bind(this, getItem(position))
        viewholders.add(holder)
    }

    // Disables multi selection.
    fun disableSelection() {
        selectedItems.clear()
        isMultiSelectMode.postValue(false)
    }

    // Enables multi selection.
    fun enableSelection() {
        isMultiSelectMode.postValue(true)
    }

    // Add an item to the selection.
    fun addItemToSelection(position: Int): Boolean = selectedItems.add(position)

    // Remove an item to the selection.
    fun removeItemFromSelection(position: Int) = selectedItems.remove(position)

    // Indicate if an item is already selected.
    fun isItemSelected(position: Int) = selectedItems.contains(position)

    // Indicate if an item is the last selected.
    fun isLastSelectedItem(position: Int) = isItemSelected(position) && selectedItems.size == 1

    fun navigateToDetail(position: Int) {
        val action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(
            getItem(
                position
            ).result
        )
        toolbar.findNavController().navigate(action)
    }

    // Remove the contextual action mode
    fun clearContextualActionMode() {
        if (this::actionMode.isInitialized) {
            actionMode.finish()
        }
    }

    // Start the contextual action mode
    fun startContextualActionMode() {
        toolbar.startActionMode(this)
    }

    private fun setActionModeTitle() {
        when (selectedItems.size) {
            1 -> actionMode.title = "${selectedItems.size} item selected"
            else -> actionMode.title = "${selectedItems.size} items selected"
        }
    }

    class FavoriteHolder(
        val binding: RowFavoriteBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var adapter: FavoriteRecyclerAdapter

        fun bind(adapter: FavoriteRecyclerAdapter, favoriteEntity: FavoriteEntity) {
            binding.result = favoriteEntity.result
            binding.executePendingBindings()
            this.adapter = adapter

            // Single click listener
            binding.root.setOnClickListener {
                if (adapter.isMultiSelectMode.value!!) {
                    // If the item clicked is the last selected item
                    if (adapter.isLastSelectedItem(layoutPosition)) {
                        adapter.disableSelection()
                        adapter.clearContextualActionMode()
                        setItemSelected(false)
                        return@setOnClickListener
                    }
                    setItemSelected(!adapter.isItemSelected(layoutPosition))
                    adapter.setActionModeTitle()
                } else {
                    adapter.navigateToDetail(layoutPosition)
                }
            }

            // Long click listener
            binding.root.setOnLongClickListener {
                if (!adapter.isMultiSelectMode.value!!) {
                    adapter.enableSelection()
                    adapter.startContextualActionMode()
                    setItemSelected(true)
                    adapter.setActionModeTitle()
                }
                true
            }

        }

        private fun setItemSelected(selected: Boolean) {
            layoutPosition.let {
                if (selected) {
                    binding.root.setBackgroundColor(
                        ContextCompat.getColor(adapter.toolbar.context, R.color.colorMultipleSelectedBg)
                    )
                    adapter.addItemToSelection(it)
                } else {
                    binding.root.setBackgroundColor(
                        ContextCompat.getColor(adapter.toolbar.context, R.color.colorBackground)
                    )
                    adapter.removeItemFromSelection(it)
                }
            }

        }

        companion object {
            fun from(parent: ViewGroup): FavoriteHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowFavoriteBinding.inflate(layoutInflater, parent, false)
                return FavoriteHolder(binding)
            }
        }
    }



    /**
     * Action mode callbacks
     */
    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.favorite_contextual_menu, menu)
        actionMode = mode!!
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_delete_favorite) {
            selectedItems.forEach {
                mainViewModel.deleteFavoriteRecipe(getItem(it))
            }
            disableSelection()
            actionMode.finish()
        }
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        viewholders.forEach { holder ->
            holder.binding.root.setBackgroundColor(
                ContextCompat.getColor(toolbar.context, R.color.colorBackground)
            )
        }
        disableSelection()
    }
}

class FavoriteDiffCallback : DiffUtil.ItemCallback<FavoriteEntity>() {
    override fun areItemsTheSame(oldItem: FavoriteEntity, newItem: FavoriteEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FavoriteEntity, newItem: FavoriteEntity): Boolean {
        return oldItem == newItem
    }

}