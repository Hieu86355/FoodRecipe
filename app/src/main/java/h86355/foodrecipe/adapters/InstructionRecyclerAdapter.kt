package h86355.foodrecipe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foodrecipes.models.Step
import h86355.foodrecipe.databinding.RowInstructionBinding

class InstructionRecyclerAdapter :
    ListAdapter<Step, InstructionRecyclerAdapter.InstructionHolder>(InstructionDiffCallback()) {

    class InstructionHolder(
        private val binding: RowInstructionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(step: Step) {
            binding.step = step
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): InstructionHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowInstructionBinding.inflate(layoutInflater, parent, false)
                return InstructionHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstructionHolder {
        return InstructionHolder.from(parent)
    }

    override fun onBindViewHolder(holder: InstructionHolder, position: Int) {
        val currenStep = getItem(position)
        holder.bind(currenStep)
    }

}

class InstructionDiffCallback : DiffUtil.ItemCallback<Step>() {
    override fun areItemsTheSame(oldItem: Step, newItem: Step): Boolean {
        return oldItem.number == newItem.number
    }

    override fun areContentsTheSame(oldItem: Step, newItem: Step): Boolean {
        return oldItem == newItem
    }

}