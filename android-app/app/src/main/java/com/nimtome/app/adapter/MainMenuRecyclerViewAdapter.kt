package com.nimtome.app.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nimtome.app.databinding.RecyclerviewMainMenuRowBinding
import com.nimtome.app.model.DndCharacter

class MainMenuRecyclerViewAdapter :
    ListAdapter<DndCharacter, MainMenuRecyclerViewAdapter.MainMenuViewHolder>(ItemCallback) {
    companion object {
        object ItemCallback: DiffUtil.ItemCallback<DndCharacter>() {
            override fun areItemsTheSame(oldItem: DndCharacter, newItem: DndCharacter): Boolean {
                return newItem == oldItem
            }

            override fun areContentsTheSame(oldItem: DndCharacter, newItem: DndCharacter): Boolean {
                return newItem.name == oldItem.name
            }
        }
    }
    var itemClickListener : SpellListItemClickListener? = null

    interface SpellListItemClickListener {
        fun onItemClick(character: DndCharacter, binding: RecyclerviewMainMenuRowBinding)
        fun onItemLongClick(character: DndCharacter, binding: RecyclerviewMainMenuRowBinding)
    }

    inner class MainMenuViewHolder(val binding: RecyclerviewMainMenuRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var character: DndCharacter? = null

        init {
            itemView.setOnClickListener {
                character?.let { character -> itemClickListener?.onItemClick(character, binding) }
            }
            itemView.setOnLongClickListener {
                character?.let { character -> itemClickListener?.onItemLongClick(character, binding)}
                true
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MainMenuViewHolder, position: Int) {
        val character = this.getItem(position)

        holder.binding.tvCharacterName.text = character.name
        holder.binding.tvLevelAndClass.text = "Level ${character.level} ${character.dndClass.legibleName}" //FIXME Ask labvez about i18n
        holder.character = character

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MainMenuViewHolder(
        RecyclerviewMainMenuRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
}
