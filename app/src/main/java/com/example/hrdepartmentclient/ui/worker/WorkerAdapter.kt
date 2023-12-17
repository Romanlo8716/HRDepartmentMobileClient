package com.example.hrdepartmentclient.ui.worker

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import com.example.hrdepartmentclient.R
import com.example.hrdepartmentclient.models.DepartmentsAndPostsOfWorker

import com.example.hrdepartmentclient.models.Worker
import com.example.hrdepartmentclient.repositories.DepartmentsAndPostsOfWorkerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale


class WorkerAdapter(
    private val context: Context,
    private var workerList: List<Worker>,
    private val listener: OnItemClickListener
) : BaseAdapter(), Filterable {

    private var originalWorkerList: List<Worker> = workerList
    private var filteredWorkerList: List<Worker> = workerList

    private val filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = mutableListOf<Worker>()

            if (constraint.isNullOrBlank()) {
                filteredList.addAll(originalWorkerList)
            } else {
                val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim()

                for (worker in originalWorkerList) {
                    if (worker.surname.toLowerCase(Locale.ROOT).contains(filterPattern)
                        || worker.name.toLowerCase(Locale.ROOT).contains(filterPattern)
                        || worker.patronymic.toLowerCase(Locale.ROOT).contains(filterPattern)
                    ) {
                        filteredList.add(worker)
                    }
                }
            }

            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredWorkerList = results?.values as? List<Worker> ?: emptyList()
            notifyDataSetChanged()
        }
    }

    override fun getFilter(): Filter {
        return filter
    }

    override fun getCount(): Int {
        return filteredWorkerList.size
    }

    override fun getItem(position: Int): Worker {
        return filteredWorkerList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun updateData(newData: List<Worker>) {
        originalWorkerList = newData
        filteredWorkerList = newData
        notifyDataSetChanged()
    }

    @SuppressLint("SuspiciousIndentation")
    fun setStatus(worker: Worker, depAndPostofWorkers:List<DepartmentsAndPostsOfWorker>) : String{
        var status : String
        status = ""

                if(worker.dismiss==true){
                    status="Уволен"
                }
                else{
                    if(depAndPostofWorkers.isEmpty()){
                        status="Кандидат"
                    }
                    else{
                        if(depAndPostofWorkers.size>0){
                            status = depAndPostofWorkers.find { it.worker.id == worker.id }?.let { "Сотрудник" } ?: "Кандидат"
                        }
                        else{
                            status="Кандидат"
                        }
                    }
                }

        return status

    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: WorkerViewHolder

        val view: View

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_worker, parent, false)
            holder = WorkerViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as WorkerViewHolder
        }



        val worker = getItem(position)

        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(worker.dateOfBirth)


        val depRepos  = DepartmentsAndPostsOfWorkerRepository()


        runBlocking {
            val depAndPostofWorkers: List<DepartmentsAndPostsOfWorker> = try {
                withContext(Dispatchers.IO) {
                    depRepos.getDepartmentsAndPostsOfWorker()
                }
            } catch (e: Exception) {
                emptyList()
            }

            holder.textViewWorker.text = "${worker.surname} ${worker.name} ${worker.patronymic} \n ${formattedDate} Статус: ${setStatus(worker, depAndPostofWorkers)}"
        }


        // Обработка нажатия на элемент списка
        view.setOnClickListener {
            listener?.onItemClick(worker)
        }

        return view
    }
}