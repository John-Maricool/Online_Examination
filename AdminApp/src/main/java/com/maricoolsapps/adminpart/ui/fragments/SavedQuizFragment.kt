package com.maricoolsapps.adminpart.ui.fragments

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isNotEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.maricoolsapps.adminpart.R
import com.maricoolsapps.adminpart.ui.viewModels.SavedQuizViewModel
import com.maricoolsapps.adminpart.adapters.SavedQuizAdapter
import com.maricoolsapps.adminpart.appComponents.MainActivity
import com.maricoolsapps.adminpart.databinding.FragmentSavedQuizBinding
import com.maricoolsapps.utils.interfaces.OnItemClickListener
import com.maricoolsapps.utils.interfaces.OnItemLongClickListener
import com.maricoolsapps.room_library.room.RoomEntity
import com.maricoolsapps.room_library.room.ServerQuizDataModel
import com.maricoolsapps.utils.datastate.MyDataState
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.others.ActionModeImpl
import com.maricoolsapps.utils.others.Status
import com.maricoolsapps.utils.others.showSnack
import com.maricoolsapps.utils.others.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SavedQuizFragment : Fragment(R.layout.fragment_saved_quiz), OnItemClickListener,
    OnItemLongClickListener {

    private val model: SavedQuizViewModel by viewModels()
    private var _binding: FragmentSavedQuizBinding? = null
    private val binding get() = _binding!!
   // private lateinit var clickedItem: RoomEntity
    private lateinit var actionMode: SavedQuizActionMode

    @Inject
    lateinit var adapter: SavedQuizAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSavedQuizBinding.bind(view)
        (activity as MainActivity).toolbar.title = "Saved Quiz"
        actionMode = SavedQuizActionMode(activity as AppCompatActivity)
        binding.recyclerView.setHasFixedSize(false)
        binding.recyclerView.adapter = adapter
        observeLiveData()
        setHasOptionsMenu(true)
        adapter.setOnClickListener(this)
        adapter.setOnLongClickListener(this)
    }

    private fun addOverriteToDb(time: Int) {
        val data: MutableList<RoomEntity> = adapter.items
        model.addOverriteToDb(data, time)
    }

    private fun send(time: Int) {
        val data: MutableList<RoomEntity> = adapter.items
        model.addToFirebase(data, time)
    }

    private fun observeLiveData() {
        model.dataState.observe(viewLifecycleOwner) { dataState ->
            when (dataState.status) {
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    binding.progressBar.showSnack("Error")
                }
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    val list = dataState.data as MutableList<RoomEntity>
                    adapter.setList(list)
                    Log.d("view", dataState.data.toString())
                }

                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }

        model.add.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                requireActivity().showToast(result)
            }
        }
        model.loading.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressUpload.visibility = View.VISIBLE
            } else {
                binding.progressUpload.visibility = View.GONE
            }
        }


    }

    override fun onItemClick(item: Any) {
        val action =
            SavedQuizFragmentDirections.actionSavedQuizFragmentToQuizArrangement(item as RoomEntity)
        findNavController().navigate(action)
    }

    override fun onItemLongClick(item: Any) {
      //  clickedItem = item as RoomEntity
        actionMode.start()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.cloud_upload, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.cloud_upload -> {
                showDialog()
                return true
            }
            R.id.delete_all -> {
                model.deleteQuiz()
                adapter.clearAllData()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Notice")
        alertDialogBuilder.setMessage("You cannot edit any quiz after uploading")
        val input = EditText(requireContext())
        input.hint = "Quiz timing for each question (seconds)"
        input.inputType = InputType.TYPE_CLASS_NUMBER
        alertDialogBuilder.setView(input)
            .setPositiveButton("New Write") { dialog, _ ->
                val text = input.text.toString().trim()
                if (text.isNotEmpty()) {
                   // sendToFirebase()
                    addOverriteToDb(text.toInt())
                    dialog.dismiss()
                } else {
                    requireActivity().showToast("Empty Input")
                    return@setPositiveButton
                }
            }.setNegativeButton("Add to Existing") { dialogInterface, _ ->
                val text = input.text.toString().trim()
                if (text.isNotEmpty()) {
                    //sendToFirebase()
                    send(text.toInt())
                    dialogInterface.dismiss()
                } else {
                    requireActivity().showToast("Empty Input")
                    return@setNegativeButton
                }
            }.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    inner class SavedQuizActionMode
    constructor(activity: AppCompatActivity) : ActionModeImpl(activity) {
        override fun performAction(mode: ActionMode?, item: MenuItem?) {
            when (item?.itemId) {
                R.id.delete -> {
                    model.delete(SavedQuizAdapter.clickedItems)
                    adapter.removeItems()
                    mode?.finish()
                }
            }
        }

        override fun createActionMode(mode: ActionMode?, menu: Menu?) {
            mode?.menuInflater?.inflate(R.menu.action_mode, menu)
            mode?.title = "Choose your options"
        }

        override fun DestroyActionMode(mode: ActionMode?) {
            mode?.finish()
            adapter.clearClickedItems()
        }
    }
}