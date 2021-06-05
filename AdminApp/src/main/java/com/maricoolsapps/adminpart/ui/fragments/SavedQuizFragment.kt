package com.maricoolsapps.adminpart.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.maricoolsapps.adminpart.R
import com.maricoolsapps.adminpart.ui.viewModels.SavedQuizViewModel
import com.maricoolsapps.adminpart.adapters.SavedQuizAdapter
import com.maricoolsapps.adminpart.databinding.FragmentSavedQuizBinding
import com.maricoolsapps.utilsandrepository.interfaces.OnItemClickListener
import com.maricoolsapps.utilsandrepository.interfaces.OnItemLongClickListener
import com.maricoolsapps.utilsandrepository.models.ServerQuizDataModel
import com.maricoolsapps.utilsandrepository.utils.MyDataState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SavedQuizFragment : Fragment(R.layout.fragment_saved_quiz), OnItemClickListener, OnItemLongClickListener, ActionMode.Callback {

    private val model: SavedQuizViewModel by viewModels()

    private var _binding: FragmentSavedQuizBinding? = null
    private val binding get() = _binding!!
     var mode: ActionMode? = null
    lateinit var clickedItem: ServerQuizDataModel

    @Inject
    lateinit var adapter: SavedQuizAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSavedQuizBinding.bind(view)
        binding.recyclerView.setHasFixedSize(false)
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        model.start()
        startMonitoring()
    }

    override fun onStart() {
        super.onStart()
        adapter.setOnClickListener(this)
        adapter.setOnLongClickListener(this)
    }

    private fun startMonitoring(){
        model.dataState.observe(viewLifecycleOwner, Observer {dataState ->
            when(dataState){
                is MyDataState.notLoaded ->{
                    binding.progressBar.visibility = View.GONE
                }

                is MyDataState.onLoaded<List<ServerQuizDataModel>> ->{
                    binding.progressBar.visibility = View.GONE
                     //adapter.getList(dataState.data as MutableList<ServerQuizDataModel>)
                    adapter.items = dataState.data.toMutableList()
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

    override fun onItemClick(item: Any) {
        val action = SavedQuizFragmentDirections.actionSavedQuizFragmentToQuizArrangement(item as ServerQuizDataModel)
        findNavController().navigate(action)
    }

    override fun onItemLongClick(item: Any) {
        clickedItem = item as ServerQuizDataModel
         mode = activity?.startActionMode(this)
    }

    override fun onActionItemClicked(p0: ActionMode?, item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.delete -> {
                adapter.items.removeAll(SavedQuizAdapter.clickedItems)
                model.delete(SavedQuizAdapter.clickedItems)
                mode?.finish()
                adapter.notifyDataSetChanged()
            }
        }
        return true
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.action_mode, menu)
        mode?.title = "Choose your options"
        return true
    }

    override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
        return false
    }

    override fun onDestroyActionMode(p0: ActionMode?) {
        mode = null
        SavedQuizAdapter.isActionModeOpened = false
        SavedQuizAdapter.clickedItems.clear()
    }
}