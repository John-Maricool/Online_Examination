package com.maricoolsapps.adminpart.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maricoolsapps.adminpart.databinding.RegisteredUsersListItemBinding
import com.maricoolsapps.utils.interfaces.OnItemClickListener
import com.maricoolsapps.utils.models.StudentUser
import javax.inject.Inject

class RegisteredUsersAdapter
@Inject constructor():
        RecyclerView.Adapter<RegisteredUsersAdapter.RegisteredUsersViewHolder>() {


    var items: List<StudentUser> = listOf()

    lateinit var listener: OnItemClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegisteredUsersViewHolder {
        val binding = RegisteredUsersListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        )
        return RegisteredUsersViewHolder(binding)
    }

    fun setOnClickListener(mlistener: OnItemClickListener){
        listener = mlistener
    }

    fun setList(newList: List<StudentUser>){
        items = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RegisteredUsersViewHolder, position: Int) {
        val currentPos = items[position]
        holder.binding.name.text = currentPos.name
        holder.binding.email.text = currentPos.email
        holder.binding.number.text = currentPos.number
    }

    inner class RegisteredUsersViewHolder(var binding: RegisteredUsersListItemBinding):
            RecyclerView.ViewHolder(binding.root){

        init{
            binding.cardView.setOnClickListener {
                val position = bindingAdapterPosition
                val currentItem = items[position]
                listener.onItemClick(currentItem)
            }
        }
    }
}