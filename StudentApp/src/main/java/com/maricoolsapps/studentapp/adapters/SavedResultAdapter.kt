package com.maricoolsapps.studentapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maricoolsapps.room_library.room.QuizResultEntity
import com.maricoolsapps.studentapp.databinding.SavedResultItemBinding
import javax.inject.Inject

class SavedResultAdapter
@Inject constructor() :
        RecyclerView.Adapter<SavedResultAdapter.myViewHolder>() {

    var items: List<QuizResultEntity> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val binding = SavedResultItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        )
        return myViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val currentPos = items[position]
        holder.binding.name.text = currentPos.name
        holder.binding.score.text = currentPos.percentage.toString() + "%"
    }

    inner class myViewHolder(var binding: SavedResultItemBinding) : RecyclerView.ViewHolder(binding.root)
}