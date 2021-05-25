package com.maricoolsapps.adminpart

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maricoolsapps.adminpart.databinding.QuizListItemBinding
import com.maricoolsapps.adminpart.interfaces.OnItemClickListener
import com.maricoolsapps.adminpart.interfaces.OnItemLongClickListener
import com.maricoolsapps.adminpart.room.RoomEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SavedQuizAdapter
@Inject constructor( @ApplicationContext val context: Context) :
        RecyclerView.Adapter<SavedQuizAdapter.myViewHolder>() {

    var items: MutableList<RoomEntity> = mutableListOf()
    var isSelected = false
    lateinit var listener: OnItemClickListener

    init {
        isActionModeOpened = false
    }

    private val selectedItems = arrayListOf<RoomEntity>()
    lateinit var listener_long: OnItemLongClickListener

    fun getList(Items: MutableList<RoomEntity>) {
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

    fun setOnClickListener(mlistener: OnItemClickListener) {
        listener = mlistener
    }

    fun setOnLongClickListener(mlistener: OnItemLongClickListener) {
        listener_long = mlistener
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

    inner class myViewHolder(var binding: QuizListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.cardView.setOnClickListener {

                val position = bindingAdapterPosition
                Log.d("tah", position.toString())
                    val currentItem = items[position]
                if (isActionModeOpened == false && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(currentItem)
                }else {
                    if (isSelected == false){
                        it.setBackgroundColor(context.resources.getColor(R.color.colorPrimary, null))
                    clickedItems.add(currentItem)
                    isSelected = true
                }else{
                        it.setBackgroundColor(context.resources.getColor(R.color.white, null))
                        clickedItems.remove(currentItem)
                        isSelected = false
                    }
                }
            }

            binding.cardView.setOnLongClickListener {
                val position = bindingAdapterPosition
                val currentItem = items[position]
                if (isActionModeOpened == false && position != RecyclerView.NO_POSITION){
                        if (isSelected == false) {
                            it.setBackgroundColor(context.resources.getColor(R.color.colorPrimary, null))
                            clickedItems.add(currentItem)
                            isSelected = true

                        }else{
                            it.setBackgroundColor(context.resources.getColor(R.color.white, null))
                            clickedItems.remove(currentItem)
                            isSelected = false
                        }
                    listener_long.onItemLongClick(currentItem)
                    isActionModeOpened = true
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