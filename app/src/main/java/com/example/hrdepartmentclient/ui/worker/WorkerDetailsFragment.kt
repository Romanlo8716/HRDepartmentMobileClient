package com.example.hrdepartmentclient.ui.worker


import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.hrdepartmentclient.R
import com.example.hrdepartmentclient.models.Post
import com.example.hrdepartmentclient.models.Worker
import com.example.hrdepartmentclient.repositories.DepartmentsAndPostsOfWorkerRepository
import com.example.hrdepartmentclient.repositories.EducationRepository
import com.example.hrdepartmentclient.repositories.LaborBookRepository
import com.example.hrdepartmentclient.repositories.MedicalBookRepository
import com.example.hrdepartmentclient.repositories.VacationRepository
import com.example.hrdepartmentclient.repositories.WorkerRepository
import com.example.hrdepartmentclient.ui.post.PostDetailsFragmentDirections
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


class WorkerDetailsFragment : Fragment() {

    private var selectedWorkerId: String? = null
    var repository = WorkerRepository()
    var depAndPostOfWorkerRepository = DepartmentsAndPostsOfWorkerRepository()
    var laborBookRepository = LaborBookRepository()
    var educationRepository = EducationRepository()
    var vacationRepository = VacationRepository()
    var medicalBookRepository = MedicalBookRepository()


    companion object {
        private const val ARG_SELECTED_POST = "selected_worker"

        fun newInstance(selectedWorker: Worker): WorkerDetailsFragment {
            val fragment = WorkerDetailsFragment()
            val args = Bundle()
            args.putParcelable(ARG_SELECTED_POST, selectedWorker)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedWorkerId?.let { id ->
            viewLifecycleOwner.lifecycleScope.launch {
                val workerDetails = repository.getWorkerDetailsById(id)
                val depAndPostWorker = view.findViewById<TextView>(R.id.DepAndPostWorker)
                val tableLayout: TableLayout = view.findViewById(R.id.tableLayoutDescAndPostOfWorker)
                val createDepartment = view.findViewById<TextView>(R.id.textViewCreateDepartment)
                val descWorker = view.findViewById<TextView>(R.id.descWorker)
                val textViewDescription = view.findViewById<TextView>(R.id.textViewDescription)
                val descMilitary = view.findViewById<LinearLayout>(R.id.DescMilitaryTitle)
                val militaryTitle = view.findViewById<TextView>(R.id.MilitaryTitle)
                val laborBook = view.findViewById<TextView>(R.id.LaborBook)
                val createLaborBook = view.findViewById<TextView>(R.id.textViewCreateLaborBook)
                val tableLaborBook = view.findViewById<TableLayout>(R.id.tableLayoutLaborBook)
                val education = view.findViewById<TextView>(R.id.Education)
                val createEducation = view.findViewById<TextView>(R.id.textViewCreateEducation)
                val tableEducation = view.findViewById<TableLayout>(R.id.tableLayoutEducation)
                val vacation = view.findViewById<TextView>(R.id.Vacation)
                val createVacation = view.findViewById<TextView>(R.id.textViewCreateVacation)
                val tableVacation = view.findViewById<TableLayout>(R.id.tableLayoutVacation)
                val medicalBook = view.findViewById<TextView>(R.id.MedicalBook)
                val createMedicalBook = view.findViewById<TextView>(R.id.textViewCreateMedicalBook)
                val tableMedicalBook = view.findViewById<TableLayout>(R.id.tableLayoutMedicalBook)


                // Теперь у вас есть данные о выбранном отделе
                // Используйте их для отображения на вашем макете
                // Например, если у вас есть TextView с id textViewDepartmentDetails:
                view.findViewById<TextView>(com.example.hrdepartmentclient.R.id.textViewSurnameWorker)?.text = "${workerDetails?.surname}"
                view.findViewById<TextView>(R.id.textViewNameWorker)?.text = "${workerDetails?.name}"
                view.findViewById<TextView>(R.id.textViewMiddlenameWorker)?.text = "${workerDetails?.patronymic}"

                val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                var formattedDate = dateFormat.format(workerDetails?.dateOfBirth)

                view.findViewById<TextView>(R.id.textViewDateOfBirth)?.text = "Дата рождения: ${formattedDate}"
                view.findViewById<TextView>(R.id.textViewGender)?.text = "Пол: ${workerDetails?.gender}"
                view.findViewById<TextView>(R.id.textViewPlaceHome)?.text = "Место проживания: \n${workerDetails?.city}, ${workerDetails?.street}, ${workerDetails?.house}"
                view.findViewById<TextView>(R.id.textViewFamilyPos)?.text = "Семейное положение: \n${workerDetails?.familyPosition}"
                view.findViewById<TextView>(com.example.hrdepartmentclient.R.id.textViewCoutChildren)?.text = "Количество детей: ${workerDetails?.countChildren}"
                view.findViewById<TextView>(com.example.hrdepartmentclient.R.id.textViewPhone)?.text = "Телефон: ${workerDetails?.phone}"
                view.findViewById<TextView>(R.id.textViewEmail)?.text = "Электронная почта: \n${workerDetails?.email}"


                view.findViewById<TextView>(R.id.textViewSeriesPass)?.text = "Серия паспорта: ${workerDetails?.seriesPass}"
                view.findViewById<TextView>(R.id.textViewNumberPass)?.text = "Номер паспорта: \n${workerDetails?.numberPass}"
                view.findViewById<TextView>(R.id.textViewIssuedByWhom)?.text = "Кем выдан: \n${workerDetails?.issuedByWhom}"

                formattedDate = dateFormat.format(workerDetails?.dateOfIssue)

                view.findViewById<TextView>(R.id.textViewDateOfIssue)?.text = "Дата выдачи: \n${formattedDate}"
                view.findViewById<TextView>(R.id.textViewCodePodraz)?.text = "Код подразделения: \n${workerDetails?.divisionCode}"
                view.findViewById<TextView>(R.id.textViewSnils)?.text = "${workerDetails?.numberSnils}"
                view.findViewById<TextView>(R.id.textViewInn)?.text = "${workerDetails?.numberInn}"

                if(workerDetails?.descriptionWorker==""){
                    textViewDescription.text = "Нет данных"
                }
                else{
                    textViewDescription.text = "${workerDetails?.descriptionWorker}"
                }

                descWorker.setOnClickListener{
                    if (textViewDescription.visibility == View.VISIBLE) {

                        textViewDescription.visibility = View.GONE
                    } else {
                        textViewDescription.visibility = View.VISIBLE
                    }
                }

                val depAndPostOfWorker = depAndPostOfWorkerRepository.getDescriptionWorkerDepartmentsAndPosts(workerDetails?.id)

                if (depAndPostOfWorker != null) {
                    for (item in depAndPostOfWorker) {
                        val row = TableRow(requireContext()) // Используйте requireContext() вместо this

                        // Создаем TextView для "Отдел"
                        val departmentTextView = TextView(requireContext())
                        departmentTextView.text = item.department.nameDepartment // Предполагается, что у вас есть метод getName() в объекте Department
                        departmentTextView.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                        departmentTextView.gravity = Gravity.CENTER
                        row.addView(departmentTextView)

                        // Создаем TextView для "Должность"
                        val positionTextView = TextView(requireContext())
                        positionTextView.text = item.post.namePost // Предполагается, что у вас есть метод getName() в объекте Post
                        positionTextView.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                        positionTextView.gravity = Gravity.CENTER
                        row.addView(positionTextView)

                        // Создаем TextView для "Должность"
                        val deleteTextView = TextView(requireContext())
                        deleteTextView.text = "Удалить" // Предполагается, что у вас есть метод getName() в объекте Post
                        deleteTextView.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                        deleteTextView.gravity = Gravity.CENTER
                        row.addView(deleteTextView)

                        // Добавляем строку в таблицу
                        tableLayout.addView(row)
                    }
                }

                var text = "Добавить в отдел"
                var content = SpannableString(text)
                content.setSpan(UnderlineSpan(), 0, text.length, 0)

                // Установите SpannableString в TextView

                // Установите SpannableString в TextView
                createDepartment.setText(content)

                depAndPostWorker.setOnClickListener {
                    if (tableLayout.visibility == View.VISIBLE && createDepartment.visibility == View.VISIBLE) {
                        createDepartment.visibility = View.GONE
                        tableLayout.visibility = View.GONE
                    } else {
                        createDepartment.visibility = View.VISIBLE
                        tableLayout.visibility = View.VISIBLE
                    }
                }

                view.findViewById<TextView>(R.id.textViewMilitaryTitle)?.text = "Звание: ${workerDetails?.militaryTitle}"
                view.findViewById<TextView>(R.id.textViewShelfLife)?.text = "Категория годности: ${workerDetails?.shelfLife}"
                view.findViewById<TextView>(R.id.textViewStockCatagory)?.text = "Категория запаса: ${workerDetails?.stockCategory}"
                view.findViewById<TextView>(R.id.textViewProfile)?.text = "Профиль: ${workerDetails?.profile}"
                view.findViewById<TextView>(R.id.textViewVUS)?.text = "ВУС: ${workerDetails?.vus}"
                view.findViewById<TextView>(R.id.textViewNameKomis)?.text = "Название коммисариата: ${workerDetails?.nameKomis}"




                militaryTitle.setOnClickListener {
                    if (descMilitary.visibility == View.VISIBLE) {

                        descMilitary.visibility = View.GONE
                    } else {
                        descMilitary.visibility = View.VISIBLE
                    }
                }

                val laborbookRep = laborBookRepository.getLaborBook(workerDetails?.id)

                if (laborbookRep != null) {
                    for (item in laborbookRep) {
                        val row = TableRow(requireContext()) // Используйте requireContext() вместо this

                        formattedDate = dateFormat.format(item?.dateRecord)


                        val dateTextView = TextView(requireContext())
                        dateTextView.text = formattedDate // Предполагается, что у вас есть метод getName() в объекте Department
                        dateTextView.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                        dateTextView.gravity = Gravity.CENTER
                        row.addView(dateTextView)


                        val nameWorkTextView = TextView(requireContext())
                        nameWorkTextView.text = item.nameWork // Предполагается, что у вас есть метод getName() в объекте Post
                        nameWorkTextView.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                        nameWorkTextView.gravity = Gravity.CENTER
                        row.addView(nameWorkTextView)


                        val informTextView = TextView(requireContext())
                        informTextView.text = item.informationAboutWork // Предполагается, что у вас есть метод getName() в объекте Post
                        informTextView.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                        informTextView.gravity = Gravity.CENTER
                        row.addView(informTextView)

                        // Добавляем строку в таблицу
                        tableLaborBook.addView(row)
                    }
                }

                text = "Добавить данные"
                content = SpannableString(text)
                content.setSpan(UnderlineSpan(), 0, text.length, 0)


                createLaborBook.setText(content)

                laborBook.setOnClickListener {
                    if (tableLaborBook.visibility == View.VISIBLE && createLaborBook.visibility == View.VISIBLE) {
                        createLaborBook.visibility = View.GONE
                        tableLaborBook.visibility = View.GONE
                    } else {
                        createLaborBook.visibility = View.VISIBLE
                        tableLaborBook.visibility = View.VISIBLE
                    }
                }

                val educationRep = educationRepository.getEducation(workerDetails?.id)

                if (educationRep != null) {
                    for (item in educationRep) {
                        val row = TableRow(requireContext()) // Используйте requireContext() вместо this




                        val seriesAndNumberTextView = TextView(requireContext())
                        seriesAndNumberTextView.text = "${item.seriesDiploma} ${item.numberDiploma}" // Предполагается, что у вас есть метод getName() в объекте Department
                        seriesAndNumberTextView.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                        seriesAndNumberTextView.gravity = Gravity.CENTER
                        row.addView(seriesAndNumberTextView)


                        val specialTextView = TextView(requireContext())
                        specialTextView.text = item.special // Предполагается, что у вас есть метод getName() в объекте Post
                        specialTextView.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                        specialTextView.gravity = Gravity.CENTER
                        row.addView(specialTextView)

                        formattedDate = dateFormat.format(item?.dateYearEnd)

                        val dateTextView = TextView(requireContext())
                        dateTextView.text = formattedDate // Предполагается, что у вас есть метод getName() в объекте Post
                        dateTextView.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                        dateTextView.gravity = Gravity.CENTER
                        row.addView(dateTextView)

                        // Добавляем строку в таблицу
                        tableEducation.addView(row)
                    }
                }

                text = "Добавить данные"
                content = SpannableString(text)
                content.setSpan(UnderlineSpan(), 0, text.length, 0)


                createEducation.setText(content)

                education.setOnClickListener {
                    if (tableEducation.visibility == View.VISIBLE && createEducation.visibility == View.VISIBLE) {
                        createEducation.visibility = View.GONE
                        tableEducation.visibility = View.GONE
                    } else {
                        createEducation.visibility = View.VISIBLE
                        tableEducation.visibility = View.VISIBLE
                    }
                }

                val vacationRep = vacationRepository.getVacation(workerDetails?.id)

                if (vacationRep != null) {
                    for (item in vacationRep) {
                        val row = TableRow(requireContext()) // Используйте requireContext() вместо this

                        formattedDate = dateFormat.format(item?.dateStartVacation)

                        val dateStartTextView = TextView(requireContext())
                        dateStartTextView.text = formattedDate // Предполагается, что у вас есть метод getName() в объекте Department
                        dateStartTextView.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                        dateStartTextView.gravity = Gravity.CENTER
                        row.addView(dateStartTextView)

                        formattedDate = dateFormat.format(item?.dateEndVacation)

                        val dateEndTextView = TextView(requireContext())
                        dateEndTextView.text = formattedDate // Предполагается, что у вас есть метод getName() в объекте Post
                        dateEndTextView.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                        dateEndTextView.gravity = Gravity.CENTER
                        row.addView(dateEndTextView)


                        val typeTextView = TextView(requireContext())
                        typeTextView.text = item.typeVacation // Предполагается, что у вас есть метод getName() в объекте Post
                        typeTextView.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                        typeTextView.gravity = Gravity.CENTER
                        row.addView(typeTextView)

                        // Добавляем строку в таблицу
                        tableVacation.addView(row)
                    }
                }

                text = "Добавить данные"
                content = SpannableString(text)
                content.setSpan(UnderlineSpan(), 0, text.length, 0)


                createVacation.setText(content)

                vacation.setOnClickListener {
                    if (tableVacation.visibility == View.VISIBLE && createVacation.visibility == View.VISIBLE) {

                        createVacation.visibility = View.GONE
                        tableVacation.visibility = View.GONE
                    } else {
                        createVacation.visibility = View.VISIBLE
                        tableVacation.visibility = View.VISIBLE
                    }
                }

                val medicalBookRep = medicalBookRepository.getMedicalBook(workerDetails?.id)

                if (medicalBookRep != null) {
                    for (item in medicalBookRep) {
                        val row = TableRow(requireContext()) // Используйте requireContext() вместо this



                        val placeTextView = TextView(requireContext())
                        placeTextView.text = item.placeExam // Предполагается, что у вас есть метод getName() в объекте Department
                        placeTextView.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                        placeTextView.gravity = Gravity.CENTER
                        row.addView(placeTextView)

                        formattedDate = dateFormat.format(item?.dateExam)

                        val dateTextView = TextView(requireContext())
                        dateTextView.text = formattedDate // Предполагается, что у вас есть метод getName() в объекте Post
                        dateTextView.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                        dateTextView.gravity = Gravity.CENTER
                        row.addView(dateTextView)


                        val conclusionTextView = TextView(requireContext())
                        conclusionTextView.text = item.conclusion // Предполагается, что у вас есть метод getName() в объекте Post
                        conclusionTextView.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                        conclusionTextView.gravity = Gravity.CENTER
                        row.addView(conclusionTextView)

                        // Добавляем строку в таблицу
                        tableMedicalBook.addView(row)
                    }
                }

                text = "Добавить данные"
                content = SpannableString(text)
                content.setSpan(UnderlineSpan(), 0, text.length, 0)


                createMedicalBook.setText(content)

                medicalBook.setOnClickListener {
                    if (tableMedicalBook.visibility == View.VISIBLE && createMedicalBook.visibility == View.VISIBLE) {
                        createMedicalBook.visibility = View.GONE
                        tableMedicalBook.visibility = View.GONE
                    } else {
                        createMedicalBook.visibility = View.VISIBLE
                        tableMedicalBook.visibility = View.VISIBLE
                    }
                }


                val buttonUpdate = view.findViewById<Button>(R.id.bEditDepartment)

                if(workerDetails?.dismiss == false){
                    val buttonDismiss = view.findViewById<Button>(R.id.bDismissWorker)
                    val buttonUpdate = view.findViewById<Button>(R.id.bEditWorker)

                    buttonDismiss.visibility = View.VISIBLE
                    buttonUpdate.visibility = View.VISIBLE

                    buttonDismiss.setOnClickListener{
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setTitle("Предупреждение")
                        builder.setMessage("Вы действительно хотите уволить сотрудника? Все данные (кроме основных) будут удалены!")

                        // Кнопка "Да"
                        builder.setPositiveButton("Да") { dialog, which ->
                            GlobalScope.launch(Dispatchers.Main) {
                                val successDelete = repository.dismissWorker(id)
                                // Обработка результата (например, обновление UI)
                                if (successDelete) {
                                    findNavController().navigate(R.id.action_nav_workerDetails_to_nav_worker)
                                }
                            }
                        }
                        // Кнопка "Нет"
                        builder.setNegativeButton("Нет") { dialog, which ->
                            // Ничего не делаем, просто закрываем диалоговое окно
                        }

                        // Отобразить диалоговое окно
                        val dialog = builder.create()
                        dialog.show()
                    }

                    buttonUpdate.setOnClickListener {
                        viewLifecycleOwner.lifecycleScope.launch {
                            val workerDetails = repository.getWorkerDetailsById(id)


                            openDetailsFragment(workerDetails)
                        }
                    }
                }
                else{
                    val buttonDelete = view.findViewById<Button>(R.id.bDeleteWorker)
                    val buttonRecovery = view.findViewById<Button>(R.id.bRecoveryWorker)

                    buttonDelete.visibility = View.VISIBLE
                    buttonRecovery.visibility = View.VISIBLE

                    buttonRecovery.setOnClickListener{
                        GlobalScope.launch(Dispatchers.Main) {
                            val successDelete = repository.recoveryWorker(id)
                            // Обработка результата (например, обновление UI)
                            if (successDelete) {
                                findNavController().navigate(R.id.action_nav_workerDetails_to_nav_worker)
                            }
                        }
                    }

                    buttonDelete.setOnClickListener{
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setTitle("Предупреждение")
                        builder.setMessage("Вы действительно хотите удалить сотрудника? Все данные сотрудника будут удалены!")

                        // Кнопка "Да"
                        builder.setPositiveButton("Да") { dialog, which ->
                            GlobalScope.launch(Dispatchers.Main) {
                                val successDelete = repository.deleteWorker(id)
                                // Обработка результата (например, обновление UI)
                                if (successDelete) {
                                    findNavController().navigate(R.id.action_nav_workerDetails_to_nav_worker)
                                }
                            }
                        }
                        // Кнопка "Нет"
                        builder.setNegativeButton("Нет") { dialog, which ->
                            // Ничего не делаем, просто закрываем диалоговое окно
                        }

                        // Отобразить диалоговое окно
                        val dialog = builder.create()
                        dialog.show()
                    }
                }




                val packageName = context?.packageName
                val imageView: ImageView = view.findViewById(R.id.imageViewWorker)
                val imageName = workerDetails?.photo
                val imageNameWithoutExtension = imageName?.removeSuffix(".jpg")
                val resourceId = resources.getIdentifier(imageNameWithoutExtension, "drawable", packageName)
                Picasso.get().load(resourceId).into(imageView)

                // В вашей активности или фрагменте
//                val inputDialogFragment = InputDialogFragment()
//
//// Установите слушатель для обработки введенных данных
//                inputDialogFragment.setListener(object : InputDialogFragment.InputDialogListener {
//                    override fun onInputDataAdded(inputData: String) {
//                        // Обработайте введенные данные, например, обновите ваш TextView
//                        textViewLaborBook.text = "Данные трудовой книги: $inputData"
//                    }
//                })
//
//// Показать диалоговое окно при нажатии на TextView
//                textViewLaborBook.setOnClickListener {
//                    inputDialogFragment.show(supportFragmentManager, "InputDialogFragment")
//                }
            }
        }
    }

    private fun openDetailsFragment(selectedWorker: Worker?) {
        val action = WorkerDetailsFragmentDirections.actionNavWorkerDetailsToNavWorkerupdate(selectedWorker?.id.toString())
        findNavController().navigate(action)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            selectedWorkerId = it.getString("selectedWorkerId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_workerdetails, container, false)
    }



}