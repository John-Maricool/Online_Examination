package com.maricoolsapps.adminpart.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maricoolsapps.adminpart.R
import com.maricoolsapps.adminpart.databinding.QuizListItemBinding
import com.maricoolsapps.utils.interfaces.OnItemClickListener
import com.maricoolsapps.utils.interfaces.OnItemLongClickListener
import com.maricoolsapps.room_library.room.RoomEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SavedQuizAdapter
@Inject constructor( @ApplicationContext val context: Context) :
        RecyclerView.Adapter<SavedQuizAdapter.myViewHolder>() {

    var items: MutableList<RoomEntity> = mutableListOf()
    lateinit var listener: OnItemClickListener

    init {
        isActionModeOpened = false
    }

    lateinit var listener_long: OnItemLongClickListener

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

    fun setOnClickListener(mlistener: OnItemClickListener) {
        listener = mlistener
    }

    fun setOnLongClickListener(mlistener: OnItemLongClickListener) {
        listener_long = mlistener
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val currentPos = items[position]
        holder.binding.question.append(currentPos.question)
    }

    inner class myViewHolder(var binding: QuizListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.cardView.setOnClickListener {
                val position = bindingAdapterPosition
                    val currentItem = items[position]
                if (!isActionModeOpened && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(currentItem)
                }else {
                        if (!clickedItems.contains(currentItem)){
                        clickedItems.add(currentItem)
                        it.setBackgroundColor(context.resources.getColor(R.color.colorPrimary, null))
                }else{
                        clickedItems.remove(currentItem)
                        it.setBackgroundColor(context.resources.getColor(R.color.white, null))
                    }
                }
            }

            binding.cardView.setOnLongClickListener {
                val position = bindingAdapterPosition
                val currentItem = items[position]
                if (!isActionModeOpened && position != RecyclerView.NO_POSITION){
                    clickedItems.clear()
                    listener_long.onItemLongClick(currentItem)
                    isActionModeOpened = true
                    it.setBackgroundColor(context.resources.getColor(R.color.colorPrimary, null))
                    clickedItems.add(currentItem)
                }
                return@setOnLongClickListener true
            }
        }
    }

    companion object{
        var isActionModeOpened = false
        val clickedItems = mutableListOf<RoomEntity>()
    }
}