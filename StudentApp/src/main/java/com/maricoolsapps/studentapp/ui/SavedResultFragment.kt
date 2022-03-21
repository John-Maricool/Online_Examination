package com.maricoolsapps.studentapp.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.maricoolsapps.studentapp.R
import com.maricoolsapps.studentapp.adapters.SavedResultAdapter
import com.maricoolsapps.studentapp.databinding.FragmentSavedResultBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SavedResultFragment : Fragment(R.layout.fragment_saved_result) {

    private  var _binding: FragmentSavedResultBinding? = null
    private val binding get() = _binding!!

    private val model: SavedResultViewModel by viewModels()

    @Inject
    lateinit var adapter: SavedResultAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSavedResultBinding.bind(view)

        binding.recycler.apply{
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
        }
        model.getAllResults()

        model.state.observe(viewLifecycleOwner) {
            adapter.items = it
            binding.recycler.adapter = adapter
        }
    }

    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.delete_quiz, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.delete -> {
                model.deleteAll()
                adapter.items.toMutableList().clear()
                adapter.notifyDataSetChanged()
            }
        }
        return super.onOptionsItemSelected(item)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}