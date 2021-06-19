package com.maricoolsapps.adminpart.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.maricoolsapps.adminpart.R
import com.maricoolsapps.utils.models.RegisteredUsersModel
import com.maricoolsapps.adminpart.ui.viewModels.RegisteredUsersViewModel
import com.maricoolsapps.adminpart.adapters.RegisteredUsersAdapter
import com.maricoolsapps.adminpart.databinding.FragmentRegisteredUsersBinding
import com.maricoolsapps.utils.MyDataState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisteredUsersFragment : Fragment(R.layout.fragment_registered_users) {

    private val model: RegisteredUsersViewModel by viewModels()

    @Inject
    lateinit var adapter: RegisteredUsersAdapter
    private var _binding: FragmentRegisteredUsersBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRegisteredUsersBinding.bind(view)


        binding.recyclerView.setHasFixedSize(false)
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)

    }

    private fun startMonitoring(){
        model.start().observe(viewLifecycleOwner, Observer {dataState ->
            when(dataState){
                is com.maricoolsapps.utils.MyDataState.notLoaded ->{
                    binding.progressBar.visibility = View.GONE
                }
                is com.maricoolsapps.utils.MyDataState.onLoaded ->{
                    binding.progressBar.visibility = View.GONE
                    // adapter.getList(dataState.data)
                    adapter.items = dataState.data as List<RegisteredUsersModel>
                    Log.d("view", dataState.data.toString())

                    binding.recyclerView.adapter = adapter
                }
                is com.maricoolsapps.utils.MyDataState.isLoading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}