package com.maricoolsapps.adminpart

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.maricoolsapps.adminpart.databinding.FragmentLoginBinding
import com.maricoolsapps.adminpart.databinding.FragmentSavedQuizBinding
import com.maricoolsapps.adminpart.room.RoomEntity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SavedQuizFragment : Fragment(R.layout.fragment_saved_quiz), OnItemClickListener, OnItemLongClickListener, ActionMode.Callback {

    private val model: SavedQuizViewModel by viewModels()

    private var _binding: FragmentSavedQuizBinding? = null
    private val binding get() = _binding!!
     var mode: ActionMode? = null
    lateinit var clickedItem: RoomEntity

    @Inject
    lateinit var adapter: SavedQuizAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSavedQuizBinding.bind(view)
        binding.reecyclerView.setHasFixedSize(false)
        binding.reecyclerView.layoutManager = LinearLayoutManager(activity)
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
                is MyDataState.onLoaded<List<RoomEntity>> ->{
                    binding.progressBar.visibility = View.GONE
                   // adapter.getList(dataState.data)
                    adapter.items = dataState.data.toMutableList()
                    Log.d("view", dataState.data.toString())

                    binding.reecyclerView.adapter = adapter
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
        val action = SavedQuizFragmentDirections.actionSavedQuizFragmentToQuizArrangement(item as RoomEntity)
        findNavController().navigate(action)
    }

    override fun onItemLongClick(item: Any) {
        clickedItem = item as RoomEntity
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
    }
}