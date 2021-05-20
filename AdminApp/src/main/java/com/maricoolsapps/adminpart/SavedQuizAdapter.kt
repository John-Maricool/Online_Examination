package com.maricoolsapps.adminpart

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maricoolsapps.adminpart.databinding.QuizListItemBinding
import com.maricoolsapps.adminpart.room.RoomEntity
import javax.inject.Inject

class SavedQuizAdapter
@Inject constructor() :
        RecyclerView.Adapter<SavedQuizAdapter.myViewHolder>() {

    var items: List<RoomEntity> = listOf()
    lateinit var listener: OnItemClickListener

    fun getList(Items: List<RoomEntity>){
        items = Items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val binding = QuizListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        )
        return myViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setOnClickListener(mlistener: OnItemClickListener){
        listener = mlistener
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val currentPos = items[position]
        holder.binding.question.text = "Question:  ${currentPos.question}"
        holder.binding.option1.text = "Option 1: ${currentPos.firstOption}"
        holder.binding.option2.text = "Option 2: ${currentPos.secondOption}"
        holder.binding.option3.text = "Option 3: ${currentPos.thirdOption}"
        holder.binding.option4.text = "Option 4: ${currentPos.forthOption}"
        holder.binding.correctIndex.text = "Correct Option: ${currentPos.correctIndex}"
    }

    inner class myViewHolder(var binding: QuizListItemBinding): RecyclerView.ViewHolder(binding.root){

        init {
            binding.cardView.setOnClickListener {
                val position = bindingAdapterPosition
                Log.d("tah", position.toString())
                if (position != RecyclerView.NO_POSITION) {
                    val currentItem = items[position]
                    listener.onItemClick(currentItem)
                }
            }
        }
    }
}