package com.coder.starwars.ui.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.coder.starwars.R
import com.coder.starwars.data.network.model.CharacterItemModel
import com.coder.starwars.databinding.CharacterListItemBinding
import kotlinx.android.synthetic.main.character_list_item.view.*

/**
 * Adapter class for Character List fragment
 */
class CharacterListAdapter(private val interaction: Interaction? = null) :
    ListAdapter<CharacterItemModel, CharacterListAdapter.CharacterViewHolder>(
        CharacterDiffUtil()
    ) {

    // OnCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<CharacterListItemBinding>(
            inflater,
            R.layout.character_list_item,
            parent,
            false
        )
        return CharacterViewHolder(binding, interaction)
    }

    // OnBindViewHolder
    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.binding.character = getItem(position)
        holder.binding.interaction = holder.interaction
    }

    fun swapData(data: List<CharacterItemModel>) {
        submitList(data.toMutableList())
    }

    // View Holder class for showing character list item
    inner class CharacterViewHolder(
        val binding: CharacterListItemBinding,
        val interaction: Interaction?
    ) :
        RecyclerView.ViewHolder(binding.root) {
    }

    // Callback provision for handling click on Character
    interface Interaction {
        fun characterClicked(character: CharacterItemModel)
    }

    // Diff Util Callback for RecyclerView optimization
    private class CharacterDiffUtil : DiffUtil.ItemCallback<CharacterItemModel>() {
        override fun areItemsTheSame(
            oldItem: CharacterItemModel,
            newItem: CharacterItemModel
        ) = oldItem.url == newItem.url

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: CharacterItemModel,
            newItem: CharacterItemModel
        ) = oldItem == newItem
    }
}