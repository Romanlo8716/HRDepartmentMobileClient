package com.example.hrdepartmentclient.ui.worker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.hrdepartmentclient.R
import com.example.hrdepartmentclient.databinding.FragmentPostBinding
import com.example.hrdepartmentclient.databinding.FragmentWorkerBinding
import com.example.hrdepartmentclient.models.Post
import com.example.hrdepartmentclient.models.Worker
import com.example.hrdepartmentclient.repositories.PostRepository
import com.example.hrdepartmentclient.repositories.WorkerRepository
import com.example.hrdepartmentclient.ui.post.PostAdapter
import com.example.hrdepartmentclient.ui.post.PostFragmentDirections
import com.example.hrdepartmentclient.ui.post.PostViewModel
import com.example.hrdepartmentclient.ui.worker.WorkerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WorkerFragment : Fragment(), com.example.hrdepartmentclient.ui.worker.OnItemClickListener {

    private var _binding: FragmentWorkerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val repository = WorkerRepository()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val galleryViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(WorkerViewModel::class.java)

        _binding = FragmentWorkerBinding.inflate(inflater, container, false)
        val root: View = binding.root



        val listView: ListView = binding.listViewWorkers
        val adapter = WorkerAdapter( requireContext(), emptyList(), this) // Передаем пустой список при создании адаптера
        val editTextSearch = root.findViewById<EditText>(R.id.editTextSearch)

        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedWorker = adapter.getItem(position)
            openDetailsFragment(selectedWorker)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            val workerList = withContext(Dispatchers.IO) {
                repository.getWorker()
            }

            adapter.updateData(workerList)
        }

        editTextSearch?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                // Фильтруем список сотрудников по введенному тексту
                Log.d("Filter", "AfterTextChanged: $s")
                adapter.filter.filter(s)
            }
        })

        return root
    }

    private fun openDetailsFragment(selectedWorker: Worker) {
        val action = WorkerFragmentDirections
            .actionNavWorkerToNavWorkerDetails(selectedWorker.id.toString())
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonCreateWorker = view?.findViewById<Button>(R.id.buttonCreateWorker)

        buttonCreateWorker?.setOnClickListener {
            findNavController().navigate(R.id.action_nav_worker_to_nav_workercreate)
        }

    }

    override fun onItemClick(worker: Worker) {
        openDetailsFragment(worker)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}