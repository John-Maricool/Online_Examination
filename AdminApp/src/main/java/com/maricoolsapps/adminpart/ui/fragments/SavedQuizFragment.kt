package com.maricoolsapps.adminpart.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isNotEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
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
import com.maricoolsapps.utils.others.ActionModeImpl
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SavedQuizFragment : Fragment(R.layout.fragment_saved_quiz), OnItemClickListener, OnItemLongClickListener {

    private val model: SavedQuizViewModel by viewModels()

    private var _binding: FragmentSavedQuizBinding? = null
    private val binding get() = _binding!!
    private lateinit var clickedItem: RoomEntity
    private lateinit var actionMode: SavedQuizActionMode

    @Inject
    lateinit var adapter: SavedQuizAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSavedQuizBinding.bind(view)
        binding.recyclerView.setHasFixedSize(false)
        actionMode = SavedQuizActionMode(activity as AppCompatActivity)
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        model.start()
        startMonitoring()
        setHasOptionsMenu(true)

    }

    override fun onStart() {
        super.onStart()
        adapter.setOnClickListener(this)
        adapter.setOnLongClickListener(this)
    }

    private fun sendToFirebase() {
        if (binding.recyclerView.isNotEmpty()){
            binding.progressUpload.visibility = View.VISIBLE
            binding.progressText.visibility = View.VISIBLE
        }
    }

    private fun clearDocsAndSend() {
        model.clearQuizDocs().observe(viewLifecycleOwner, {
            when(it){
                true -> send()
                false -> {
                    binding.progressUpload.visibility = View.GONE
                    binding.progressText.visibility = View.GONE
                    Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()}
                null -> send()
            }
        })
    }

    private fun send() {
        val data: List<ServerQuizDataModel> = model.map()
        model.clicks = 0
        val size = data.size
        val progressIncrement = 100 / size
        data.forEach {
            model.addToFirebase(it).observe(viewLifecycleOwner, { result ->
                when (result) {
                    is MyServerDataState.onLoaded -> {
                        model.clicks++
                        if (model.clicks == size) {
                            binding.progressBar.visibility = View.GONE
                            binding.progressUpload.visibility = View.GONE
                            binding.progressText.visibility = View.GONE
                            Toast.makeText(activity, "Upload Successful", Toast.LENGTH_LONG).show()
                            model.deleteQuiz()
                            adapter.items.clear()
                            adapter.notifyDataSetChanged()
                        } else {
                            binding.progressUpload.progress += progressIncrement
                            binding.progressText.text = "${model.clicks}/$size"
                        }
                    }

                    is MyServerDataState.notLoaded -> {
                        binding.progressBar.visibility = View.GONE
                        binding.progressUpload.visibility = View.GONE
                        binding.progressText.visibility = View.GONE
                        Toast.makeText(activity, "Upload Failed", Toast.LENGTH_LONG).show()
                    }
                    MyServerDataState.isLoading -> TODO()
                }
            })
        }
    }

    private fun startMonitoring(){
        model.dataState.observe(viewLifecycleOwner, Observer {dataState ->
            when(dataState){
                is MyDataState.notLoaded ->{
                    binding.progressBar.visibility = View.GONE
                }

                is MyDataState.onLoaded ->{
                    binding.progressBar.visibility = View.GONE
                   val list = dataState.data as MutableList<RoomEntity>
                    adapter.items = list
                    Log.d("view", dataState.data.toString())
                    binding.recyclerView.adapter = adapter
                }

                is MyDataState.isLoading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onItemClick(item: Any) {
        val action = SavedQuizFragmentDirections.actionSavedQuizFragmentToQuizArrangement(item as RoomEntity)
        findNavController().navigate(action)
    }

    override fun onItemLongClick(item: Any) {
        clickedItem = item as RoomEntity
        actionMode.start()
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
        alertDialogBuilder.setMessage("You cannot edit any quiz after uploading").setPositiveButton("New Write") { dialog, _ ->
            sendToFirebase()
            clearDocsAndSend()
            dialog?.dismiss()
        }.setNegativeButton("Add to Existing"){ dialogInterface, _ ->
            sendToFirebase()
            send()
            dialogInterface.dismiss()
        }.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    inner class SavedQuizActionMode
     constructor(activity: AppCompatActivity): ActionModeImpl(activity){
        override fun performAction(mode: ActionMode?, item: MenuItem?) {
            when(item?.itemId){
                R.id.delete -> {
                    adapter.items.removeAll(SavedQuizAdapter.clickedItems)
                    model.delete(SavedQuizAdapter.clickedItems)
                    mode?.finish()
                    adapter.notifyDataSetChanged()
                }
            }
        }

        override fun createActionMode(mode: ActionMode?, menu: Menu?) {
            mode?.menuInflater?.inflate(R.menu.action_mode, menu)
            mode?.title = "Choose your options"
        }

        override fun DestroyActionMode(mode: ActionMode?) {
            mode?.finish()
            SavedQuizAdapter.isActionModeOpened = false
            SavedQuizAdapter.clickedItems.clear()
        }
    }
}