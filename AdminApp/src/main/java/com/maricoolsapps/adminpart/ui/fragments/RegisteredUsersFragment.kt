package com.maricoolsapps.adminpart.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.maricoolsapps.adminpart.R
import com.maricoolsapps.adminpart.ui.viewModels.RegisteredUsersViewModel
import com.maricoolsapps.adminpart.adapters.RegisteredUsersAdapter
import com.maricoolsapps.adminpart.adapters.SavedQuizAdapter
import com.maricoolsapps.adminpart.databinding.FragmentRegisteredUsersBinding
import com.maricoolsapps.utils.datastate.MyDataState
import com.maricoolsapps.utils.interfaces.OnItemLongClickListener
import com.maricoolsapps.utils.models.StudentUser
import com.maricoolsapps.utils.others.ActionModeImpl
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisteredUsersFragment : Fragment(R.layout.fragment_registered_users), OnItemLongClickListener {

    private val model: RegisteredUsersViewModel by viewModels()

    @Inject
    lateinit var adapter: RegisteredUsersAdapter

    lateinit var actionMode: RegisteredUsersActionMode
    private var _binding: FragmentRegisteredUsersBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRegisteredUsersBinding.bind(view)
        actionMode = RegisteredUsersActionMode(activity as AppCompatActivity)

        binding.recyclerView.setHasFixedSize(false)
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)

        startMonitoring()
    }

    override fun onStart() {
        super.onStart()
        adapter.setOnLongClickListener(this)
    }

    private fun startMonitoring(){
        binding.progressBar.visibility = View.VISIBLE
        model.start().observe(viewLifecycleOwner, { dataState ->
            when(dataState){
                is MyDataState.notLoaded ->{
                    binding.progressBar.visibility = View.GONE
                }
                is MyDataState.onLoaded ->{
                    binding.progressBar.visibility = View.GONE
                    // adapter.getList(dataState.data)
                    adapter.items = (dataState.data as MutableList<StudentUser>)
                    Log.d("view", dataState.data.toString())

                    binding.recyclerView.adapter = adapter
                }
                is MyDataState.isLoading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemLongClick(item: Any) {
        actionMode.start()
    }

    private fun deleteUsers(ids: List<String>){
        model.deleteStudents(ids).observe(viewLifecycleOwner, Observer {
            when(it){
                true -> {
                    adapter.items.removeAll(RegisteredUsersAdapter.clickedItems)
                    adapter.notifyDataSetChanged()
                    Toast.makeText(activity, "Successfully Unregistered Students", Toast.LENGTH_SHORT).show()
                }
                false -> Toast.makeText(activity, "Error deleting students", Toast.LENGTH_SHORT).show()
            }
        })
    }

    inner class RegisteredUsersActionMode
    constructor(activity: AppCompatActivity): ActionModeImpl(activity){
        override fun performAction(mode: ActionMode?, item: MenuItem?) {
            when(item?.itemId){
                R.id.unregister -> {
                    val ids = RegisteredUsersAdapter.clickedItems.map {
                        it.id
                    }
                    deleteUsers(ids)
                    mode?.finish()
                }
            }
        }

        override fun createActionMode(mode: ActionMode?, menu: Menu?) {
            mode?.menuInflater?.inflate(R.menu.delete_mode, menu)
            mode?.title = "Choose your options"
        }

        override fun DestroyActionMode(mode: ActionMode?) {
            mode?.finish()
            RegisteredUsersAdapter.isActionModeOpened = false
            RegisteredUsersAdapter.clickedItems.clear()
        }
    }
}