package com.example.hrdepartmentclient.ui.department

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.hrdepartmentclient.R
import com.example.hrdepartmentclient.models.Department
import com.example.hrdepartmentclient.models.Post
import com.example.hrdepartmentclient.repositories.DepartmentRepository
import com.example.hrdepartmentclient.repositories.DepartmentsAndPostsOfWorkerRepository
import com.example.hrdepartmentclient.repositories.PostRepository
import com.example.hrdepartmentclient.ui.post.PostDetailsFragment
import com.example.hrdepartmentclient.ui.post.PostDetailsFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DepartmentDetailsFragment : Fragment() {

    private var selectedDepartmentId: String? = null
    var repository = DepartmentRepository()
    val depAndPostRepository = DepartmentsAndPostsOfWorkerRepository()


    companion object {
        private const val ARG_SELECTED_POST = "selected_department"

        fun newInstance(selectedDepartment: Department): DepartmentDetailsFragment {
            val fragment = DepartmentDetailsFragment()
            val args = Bundle()
            args.putParcelable(ARG_SELECTED_POST, selectedDepartment)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedDepartmentId?.let { id ->
            viewLifecycleOwner.lifecycleScope.launch {
                val departmentDetails = repository.getDepartmentDetailsById(id)
                val postOfDepartmentRepository = repository.getPostOfDepartment(id)
                val depAndPost = depAndPostRepository.getDepartmentsAndPostsOfWorkerByDepartmentId(id)
                val workerOnDepartment = depAndPostRepository.getWorkerOnDepartmentByDepartmentId(id)

                val tablePostOfDepartment = view.findViewById<TableLayout>(R.id.tableLayoutPostOfDepartment)
                val postOfDepartment = view.findViewById<TextView>(R.id.PostOfDepartment)
                val createPostOfDepartment = view.findViewById<TextView>(R.id.textViewCreatePostOfDepartment)

                val tableWorkerOfDepartment = view.findViewById<TableLayout>(R.id.tableLayoutWorkerOfDepartment)
                val workerOfDepartment = view.findViewById<TextView>(R.id.WorkerOfDepartment)
                val createWorkerOfDepartment = view.findViewById<TextView>(R.id.textViewCreateWorkerOfDepartment)


                // Теперь у вас есть данные о выбранном отделе
                // Используйте их для отображения на вашем макете
                // Например, если у вас есть TextView с id textViewDepartmentDetails:
                view.findViewById<TextView>(R.id.textViewName)?.text = "Название отдела: ${departmentDetails?.nameDepartment}"
                view.findViewById<TextView>(R.id.textViewPhone)?.text = "Номер телефона: ${departmentDetails?.phoneNumber}"
                view.findViewById<TextView>(R.id.textViewAdress)?.text = "Адрес отдела: ${departmentDetails?.adressOfDepartment?.city}, ${departmentDetails?.adressOfDepartment?.street}, ${departmentDetails?.adressOfDepartment?.house}"

                if (postOfDepartmentRepository != null) {
                    for (item in postOfDepartmentRepository) {
                        var close = 0
                        var open = 0
                        val row = TableRow(requireContext()) // Используйте requireContext() вместо this

                        row.layoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT).apply {
                            bottomMargin = resources.getDimensionPixelSize(R.dimen.your_margin_size) // Замените your_margin_size на нужный вам размер в пикселях или используйте другие методы для задания отступа
                        }

//                        val borderDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.border_row)
//
//// Применение фонового ресурса с рамкой к TableRow
//                        ViewCompat.setBackground(row, borderDrawable)

                        if(depAndPost.isEmpty()){
                            close=0;
                        }
                        else{
                            for( itemPost in depAndPost){
                                if(item.post.id==itemPost.post.id){
                                   close +=1;
                                }
                            }
                        }

                        val postTextView = TextView(requireContext())
                        postTextView.text = item.post.namePost // Предполагается, что у вас есть метод getName() в объекте Department
                        postTextView.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                        postTextView.gravity = Gravity.CENTER
                        row.addView(postTextView)


                        val allCountTextView = TextView(requireContext())
                        allCountTextView.text = item.count.toString() // Предполагается, что у вас есть метод getName() в объекте Post
                        allCountTextView.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                        allCountTextView.gravity = Gravity.CENTER
                        row.addView(allCountTextView)

                         open = item.count-close

                        val openCountTextView = TextView(requireContext())
                        openCountTextView.text = open.toString() // Предполагается, что у вас есть метод getName() в объекте Post
                        openCountTextView.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                        openCountTextView.gravity = Gravity.CENTER
                        row.addView(openCountTextView)


                        val closeCountTextView = TextView(requireContext())
                        closeCountTextView.text = close.toString() // Предполагается, что у вас есть метод getName() в объекте Post
                        closeCountTextView.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                        closeCountTextView.gravity = Gravity.CENTER
                        row.addView(closeCountTextView)


                        // Добавляем строку в таблицу
                        tablePostOfDepartment.addView(row)
                    }
                }

                var text = "Добавить должность"
                var content = SpannableString(text)
                content.setSpan(UnderlineSpan(), 0, text.length, 0)

                // Установите SpannableString в TextView

                // Установите SpannableString в TextView
                createPostOfDepartment.setText(content)

                postOfDepartment.setOnClickListener {
                    if (tablePostOfDepartment.visibility == View.VISIBLE && createPostOfDepartment.visibility == View.VISIBLE) {
                        createPostOfDepartment.visibility = View.GONE
                        tablePostOfDepartment.visibility = View.GONE
                    } else {
                        createPostOfDepartment.visibility = View.VISIBLE
                        tablePostOfDepartment.visibility = View.VISIBLE
                    }
                }

                if (workerOnDepartment != null) {
                    for (item in workerOnDepartment) {
                        val row = TableRow(requireContext())
                        row.layoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT).apply {
                            bottomMargin = resources.getDimensionPixelSize(R.dimen.your_margin_size) // Замените your_margin_size на нужный вам размер в пикселях или используйте другие методы для задания отступа
                        }
//                        val borderDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.border_row)
//
//// Применение фонового ресурса с рамкой к TableRow
//                        ViewCompat.setBackground(row, borderDrawable)

                        val workerTextView = TextView(requireContext())
                        workerTextView.text = "${item.worker.surname} ${item.worker.name} ${item.worker.patronymic}" // Предполагается, что у вас есть метод getName() в объекте Department
                        workerTextView.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                        workerTextView.gravity = Gravity.CENTER
                        row.addView(workerTextView)


                        val postTextView = TextView(requireContext())
                        postTextView.text = item.post.namePost // Предполагается, что у вас есть метод getName() в объекте Post
                        postTextView.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                        postTextView.gravity = Gravity.CENTER
                        row.addView(postTextView)



                        // Добавляем строку в таблицу
                        tableWorkerOfDepartment.addView(row)
                    }
                }

                 text = "Добавить сотрудника"
                 content = SpannableString(text)
                content.setSpan(UnderlineSpan(), 0, text.length, 0)

                // Установите SpannableString в TextView

                // Установите SpannableString в TextView
                createWorkerOfDepartment.setText(content)

                workerOfDepartment.setOnClickListener {
                    if (tableWorkerOfDepartment.visibility == View.VISIBLE && createWorkerOfDepartment.visibility == View.VISIBLE) {
                        createWorkerOfDepartment.visibility = View.GONE
                        tableWorkerOfDepartment.visibility = View.GONE
                    } else {
                        createWorkerOfDepartment.visibility = View.VISIBLE
                        tableWorkerOfDepartment.visibility = View.VISIBLE
                    }
                }

                val buttonDelete = view.findViewById<Button>(R.id.bDeleteDepartment)
                val buttonUpdate = view.findViewById<Button>(R.id.bEditDepartment)

                buttonDelete.setOnClickListener{
                    GlobalScope.launch(Dispatchers.Main) {
                        val successDelete = repository.deleteDepartment(id)
                        // Обработка результата (например, обновление UI)
                        if (successDelete) {
                            findNavController().navigate(R.id.action_nav_departmentDetails_to_nav_department)
                        }
                    }
                }

                buttonUpdate.setOnClickListener {
                    viewLifecycleOwner.lifecycleScope.launch {
                        val departmentDetails = repository.getDepartmentDetailsById(id)


                        openDetailsFragment(departmentDetails)
                    }
                }
            }
        }
    }

    private fun openDetailsFragment(selectedDepartment: Department?) {
        val action = DepartmentDetailsFragmentDirections.actionNavDepartmentDetailsToNavDepartmentupdate(selectedDepartment?.id.toString())
        findNavController().navigate(action)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            selectedDepartmentId = it.getString("selectedDepartmentId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_departmentdetails, container, false)
    }


}