package com.maricoolsapps.adminpart.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maricoolsapps.adminpart.R
import com.maricoolsapps.adminpart.databinding.QuizListItemBinding
import com.maricoolsapps.utils.interfaces.OnItemClickListener
import com.maricoolsapps.utils.interfaces.OnItemLongClickListener
import com.maricoolsapps.room_library.room.RoomEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.android.synthetic.main.quiz_list_item.view.*
import javax.inject.Inject

class SavedQuizAdapter
@Inject constructor(@ApplicationContext val context: Context) :
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

    fun clearAllData() {
        items.clear()
        notifyDataSetChanged()
    }

    fun removeItems() {
        items.removeAll(clickedItems)
        notifyDataSetChanged()
    }

    fun clearClickedItems() {
        isActionModeOpened = false
        clickedItems.clear()
    }

    fun setList(newItem: MutableList<RoomEntity>) {
        items = newItem
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val currentPos = items[position]
        holder.binding.question.append(currentPos.question)
    }

    inner class myViewHolder(var binding: QuizListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.cardView.setOnClickListener {
                val position = bindingAdapterPosition
                Log.d("kjbbka", position.toString())
                val currentItem = items[position]
                if (!isActionModeOpened && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(currentItem)
                } else {
                    if (!clickedItems.contains(currentItem)) {
                        clickedItems.add(currentItem)
                        it.check.visibility = View.VISIBLE
                    } else {
                        clickedItems.remove(currentItem)
                        it.check.visibility = View.GONE
                    }
                }
                Log.d("kjbbka", clickedItems.toString())
            }

            binding.cardView.setOnLongClickListener {
                val position = bindingAdapterPosition
                val currentItem = items[position]
                if (!isActionModeOpened && position != RecyclerView.NO_POSITION) {
                    clickedItems.clear()
                    listener_long.onItemLongClick(currentItem)
                    isActionModeOpened = true
                    it.check.visibility = View.VISIBLE
                    clickedItems.add(currentItem)
                }
                return@setOnLongClickListener true
            }
        }
    }


    companion object {
        var isActionModeOpened = false
        val clickedItems = mutableListOf<RoomEntity>()
    }
}