package com.maricoolsapps.adminpart.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isNotEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.maricoolsapps.adminpart.R
import com.maricoolsapps.adminpart.ui.viewModels.SavedQuizViewModel
import com.maricoolsapps.adminpart.adapters.SavedQuizAdapter
import com.maricoolsapps.adminpart.databinding.FragmentSavedQuizBinding
import com.maricoolsapps.utils.interfaces.OnItemClickListener
import com.maricoolsapps.utils.interfaces.OnItemLongClickListener
import com.maricoolsapps.room_library.room.RoomEntity
import com.maricoolsapps.room_library.room.ServerQuizDataModel
import com.maricoolsapps.utils.datastate.MyDataState
import com.maricoolsapps.utils.datastate.MyServerDataState
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
        binding.recyclerView.setHasFixedSize(false)
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        model.start()
        startMonitoring()
        setHasOptionsMenu(true)
    }

    private fun sendToFirebase() {
        if (binding.recyclerView.isNotEmpty()){
            binding.progressBar.visibility = View.VISIBLE
            binding.progressText.visibility = View.VISIBLE
            clearDocsAndSend()
        }
    }

    private fun clearDocsAndSend() {
        model.clearQuizDocs().observe(viewLifecycleOwner, Observer {
            when(it){
                true -> send()
                false -> {
                    binding.progressBar.visibility = View.GONE
                    binding.progressText.visibility = View.GONE
                    Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()}
                null -> send()
            }
        })
    }

    private fun send() {
        val data = model.map()
        model.clicks = 0

        val size = data.size
        val progressIncrement = 100 / size
        data.forEach {
            model.addToFirebase(it).observe(viewLifecycleOwner, Observer { result ->
                when (result) {
                    is MyServerDataState.onLoaded -> {
                        model.clicks++
                        if (model.clicks == size) {
                            binding.progressBar.visibility = View.GONE
                            binding.progressText.visibility = View.GONE
                            Toast.makeText(activity, "Upload Successful", Toast.LENGTH_LONG).show()
                            model.deleteQuiz()
                            adapter.items.clear()
                            adapter.notifyDataSetChanged()
                        } else {
                            binding.progressBar.progress += progressIncrement
                            binding.progressText.text = "${model.clicks}/$size"
                        }
                    }

                    is MyServerDataState.notLoaded -> {
                        binding.progressBar.visibility = View.GONE
                        binding.progressText.visibility = View.GONE
                        Toast.makeText(activity, "Upload Failed", Toast.LENGTH_LONG).show()
                    }
                    MyServerDataState.isLoading -> TODO()
                }

            })
        }
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

                is MyDataState.onLoaded ->{
                    binding.progressBar.visibility = View.GONE
                    // adapter.getList(dataState.data)
                    adapter.items = dataState.data as MutableList<RoomEntity>
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
        val action = SavedQuizFragmentDirections.actionSavedQuizFragmentToQuizArrangement(item as RoomEntity)
        findNavController().navigate(action)
    }

    override fun onItemLongClick(item: Any) {
        clickedItem = item as RoomEntity
         mode = activity?.startActionMode(this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
       super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.cloud_upload, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.cloud_upload -> {
                showDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDialog() {
        val alertDialogBuilder =  AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Notice")
        alertDialogBuilder.setMessage("You can only upload the quiz once, any new upload overrides the existing one").setPositiveButton("Yes") { dialog, _ ->
            sendToFirebase()
            dialog?.dismiss()
        }.setNegativeButton("No"){ dialogInterface, _ ->
            dialogInterface.cancel()
        }.show()
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