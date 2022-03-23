package com.maricoolsapps.adminpart.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.maricoolsapps.adminpart.R
import com.maricoolsapps.adminpart.ui.viewModels.RegisteredUsersViewModel
import com.maricoolsapps.adminpart.adapters.RegisteredUsersAdapter
import com.maricoolsapps.adminpart.adapters.SavedQuizAdapter
import com.maricoolsapps.adminpart.appComponents.MainActivity
import com.maricoolsapps.adminpart.databinding.FragmentRegisteredUsersBinding
import com.maricoolsapps.utils.datastate.MyDataState
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.interfaces.OnItemClickListener
import com.maricoolsapps.utils.interfaces.OnItemLongClickListener
import com.maricoolsapps.utils.models.StudentUser
import com.maricoolsapps.utils.others.ActionModeImpl
import com.maricoolsapps.utils.others.Status
import com.maricoolsapps.utils.others.showSnack
import com.maricoolsapps.utils.others.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisteredUsersFragment : Fragment(R.layout.fragment_registered_users),
    OnItemLongClickListener, OnItemClickListener, SearchView.OnQueryTextListener {

    private val model: RegisteredUsersViewModel by viewModels()

    @Inject
    lateinit var adapter: RegisteredUsersAdapter

    lateinit var actionMode: RegisteredUsersActionMode
    private var _binding: FragmentRegisteredUsersBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRegisteredUsersBinding.bind(view)
        (activity as MainActivity).toolbar.title = "Registered Students "
        actionMode = RegisteredUsersActionMode(activity as AppCompatActivity)
        binding.recyclerView.setHasFixedSize(false)
        binding.recyclerView.adapter = adapter
        observeLiveData()
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.activate_users, menu)
        val searchItem: MenuItem = menu.findItem(R.id.search_user)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.activate -> model.activateStudents()
            R.id.refresh -> model.getRegisteredUsers()
            R.id.deactivate -> model.deactivateStudents()
            R.id.unregister_all -> unregisterAll()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun unregisterAll() {
        val users = adapter.items
        val id = mutableListOf<String>()
        users.forEach {
            id.add(it.id)
        }
        RegisteredUsersAdapter.clickedItems = users
        model.deleteStudents(id)
    }

    override fun onStart() {
        super.onStart()
        adapter.setOnLongClickListener(this)
        adapter.setOnClickListener(this)
    }



    private fun observeLiveData() {
        model.loading.observe(viewLifecycleOwner){
            if (it){
                binding.progressBar.visibility = View.VISIBLE
            }else{
                binding.progressBar.visibility = View.GONE
            }
        }
        model.students.observe(viewLifecycleOwner){
            when(it.status){
                Status.SUCCESS -> {
                    binding.schimmer.stopShimmer()
                    binding.schimmer.visibility = View.GONE
                    adapter.getUsers(it.data as MutableList<StudentUser>)
                }
                Status.ERROR -> {
                    binding.schimmer.stopShimmer()
                    binding.schimmer.visibility = View.GONE
                    binding.schimmer.showSnack(it.message!!)
                }
                Status.LOADING -> binding.schimmer.startShimmer()
            }
        }
        model.result.observe(viewLifecycleOwner){
            if (it != null){
                requireActivity().showToast(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemLongClick(item: Any) {
        actionMode.start()
    }

    inner class RegisteredUsersActionMode
    constructor(activity: AppCompatActivity) : ActionModeImpl(activity) {
        override fun performAction(mode: ActionMode?, item: MenuItem?) {
            when (item?.itemId) {
                R.id.unregister -> {
                    val ids = RegisteredUsersAdapter.clickedItems.map {
                        it.id
                    }
                    model.deleteStudents(ids)
                    adapter.deleteUser()
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

    override fun onItemClick(item: Any) {
        val Item = item as StudentUser
        val action =
            RegisteredUsersFragmentDirections.actionRegisteredUsersFragmentToRegisteredUsersDetailFragment(
                Item.id
            )
        findNavController().navigate(action)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        adapter.filter.filter(newText)
        return true
    }
}