package com.maricoolsapps.adminpart.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.maricoolsapps.adminpart.R
import com.maricoolsapps.adminpart.databinding.RegisteredUsersListItemBinding
import com.maricoolsapps.utils.interfaces.OnItemClickListener
import com.maricoolsapps.utils.interfaces.OnItemLongClickListener
import com.maricoolsapps.utils.models.StudentUser
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject

class RegisteredUsersAdapter
@Inject constructor(@ApplicationContext val context: Context):
        RecyclerView.Adapter<RegisteredUsersAdapter.RegisteredUsersViewHolder>(), Filterable {

    var items: MutableList<StudentUser> = mutableListOf()
    var items_b: MutableList<StudentUser> = items

    init {
        SavedQuizAdapter.isActionModeOpened = false
    }

    lateinit var listener_long: OnItemLongClickListener
    lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegisteredUsersViewHolder {
        val binding = RegisteredUsersListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        )
        return RegisteredUsersViewHolder(binding)
    }

    fun setOnLongClickListener(mlistener: OnItemLongClickListener){
        listener_long = mlistener
    }

    override fun getItemCount(): Int {
        return items.size
    }
    fun setOnClickListener(mlistener: OnItemClickListener) {
        listener = mlistener
    }

    fun deleteUser(){
        clickedItems.forEach {
            items.remove(it)
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RegisteredUsersViewHolder, position: Int) {
        val currentPos = items[position]
        Glide.with(context)
                .load(currentPos.photoUri?.toUri())
                .centerCrop()
                .placeholder(R.drawable.profile)
                .into(holder.binding.profileImage)
        holder.binding.name.text = "${currentPos.name}"
        holder.binding.email.text = "${currentPos.email}"
    }

    override fun getFilter(): Filter {

        return object : Filter() {
            override fun performFiltering(sequence: CharSequence?): FilterResults {
                val charSearch = sequence.toString()
                items = if (charSearch.isEmpty()) {
                    items_b
                } else {
                    val resultList = mutableListOf<StudentUser>()
                    for (row in items) {
                        if (row.name.toLowerCase(Locale.ROOT)
                                        .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = items
                return filterResults
            }

            override fun publishResults(sequence: CharSequence?, results: FilterResults?) {
                items = results?.values as MutableList<StudentUser>
                notifyDataSetChanged()
            }
        }
    }

    fun getUsers(users: MutableList<StudentUser>){
        items = users
        notifyDataSetChanged()
    }

    fun deleteUsers(){
        items.clear()
        notifyDataSetChanged()
    }

    inner class RegisteredUsersViewHolder(var binding: RegisteredUsersListItemBinding):
            RecyclerView.ViewHolder(binding.root){

        init{
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
                    SavedQuizAdapter.clickedItems.clear()
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
        var clickedItems = mutableListOf<StudentUser>()
    }
}