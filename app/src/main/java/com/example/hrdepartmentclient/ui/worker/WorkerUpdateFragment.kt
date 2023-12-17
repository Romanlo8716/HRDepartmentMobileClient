package com.example.hrdepartmentclient.ui.worker

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.hrdepartmentclient.R
import com.example.hrdepartmentclient.models.AdressOfDepartment
import com.example.hrdepartmentclient.repositories.PostRepository
import com.example.hrdepartmentclient.repositories.WorkerRepository
import com.example.hrdepartmentclient.ui.department.AddressAdapter
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class WorkerUpdateFragment: Fragment() {
    private var selectedWorkerId: String? = null
    var repository = WorkerRepository()

    //Для даты
    private var selectedDateOfBirthday : Date? = null


    private var selectedDateOfIssue : Date? = null


    //Для фото
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var selectedImageUri: Uri
    private var imageName : String? = ""
    private var resourseImage : String = ""
    private val READ_EXTERNAL_STORAGE_PERMISSION_CODE = 123

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_workerupdate, container, false)


        selectedWorkerId?.let { id ->
            viewLifecycleOwner.lifecycleScope.launch {
                val workerDetails = repository.getWorkerDetailsById(id)

                val editTextSurname : EditText = view.findViewById(R.id.editTextSurnameWorkerUpdate)
                val editTextName : EditText = view.findViewById(R.id.editTextNameWorkerUpdate)
                val editTextMiddlename : EditText = view.findViewById(R.id.editTextMiddlenameWorkerUpdate)

                val buttonImageWorker : Button = view.findViewById(R.id.buttonImageWorker)

                val imageView: ImageView = view.findViewById(R.id.imageViewWorkerUpdate)

                val datePickerDateBirthday : DatePicker = view.findViewById(R.id.datePickerDateOfBirthUpdate)

                val spinnerGender : Spinner = view.findViewById(R.id.spinnerGenderUpdate)

                val editTextCity : EditText = view.findViewById(R.id.editTextCityWorkerUpdate)
                val editTextStreet : EditText = view.findViewById(R.id.editTextStreetWorkerUpdate)
                val editTextHouse : EditText = view.findViewById(R.id.editTextHomeWorkerUpdate)

                val spinerFamilyPos : Spinner = view.findViewById(R.id.spinerFamilyPosUpdate)

                val editTextCoutChildren : EditText = view.findViewById(R.id.editTextCoutChildrenUpdate)

                val editTextPhone : EditText = view.findViewById(R.id.editTextPhoneUpdate)
                val editTextEmail : EditText = view.findViewById(R.id.editTextEmailUpdate)

                val editTextSnils : EditText = view.findViewById(R.id.editTextSnilsUpdate)

                val editTextSeriesPass : EditText = view.findViewById(R.id.editTextSeriesPassUpdate)
                val editTextNumberPass : EditText = view.findViewById(R.id.editTextNumberPassUpdate)
                val editTextIssuedByWhom : EditText = view.findViewById(R.id.editTextIssuedByWhomUpdate)
                val datePickerDateOfIssue : DatePicker = view.findViewById(R.id.datePickerDateOfIssueUpdate)
                val editTextCodePodraz : EditText = view.findViewById(R.id.editTextCodePodrazUpdate)

                val editTextInn : EditText = view.findViewById(R.id.editTextInnUpdate)

                val editTextDescription : EditText = view.findViewById(R.id.editTextDescriptionUpdate)

                val editTextMilitaryTitle : EditText = view.findViewById(R.id.editTextMilitaryTitleUpdate)
                val spinnerShelfLife : Spinner = view.findViewById(R.id.spinnerShelfLifeUpdate)
                val spinnerStockCatagory : Spinner = view.findViewById(R.id.spinnerStockCatagoryUpdate)
                val editTextProfile : EditText = view.findViewById(R.id.editTextProfileUpdate)
                val editTextVUS : EditText = view.findViewById(R.id.editTextVUSUpdate)
                val editTextNameKomis : EditText = view.findViewById(R.id.editTextNameKomisUpdate)

                val updateWorker : Button = view.findViewById(R.id.updateWorker)



                if (workerDetails != null) {

                    buttonImageWorker.setOnClickListener {
                        if (ContextCompat.checkSelfPermission(
                                requireContext(),
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            // Разрешение уже предоставлено, открываем галерею
                            openGallery()
                        } else {
                            // Запрашиваем разрешение, если оно не предоставлено
                            ActivityCompat.requestPermissions(
                                requireActivity(),
                                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                READ_EXTERNAL_STORAGE_PERMISSION_CODE
                            )
                        }
                    }


                    editTextSurname.setText(workerDetails.surname)
                    editTextName.setText(workerDetails.name)
                    editTextMiddlename.setText(workerDetails.patronymic)

                    val packageName = context?.packageName
                    var imageName = workerDetails?.photo
                    val imageNameWithoutExtension = imageName?.removeSuffix(".jpg")
                    val resourceId = resources.getIdentifier(imageNameWithoutExtension, "drawable", packageName)
                    Picasso.get().load(resourceId).into(imageView)




                    val birthDate = workerDetails.dateOfBirth
                    val calendar = Calendar.getInstance()
                    calendar.time = birthDate

                    val year = calendar.get(Calendar.YEAR)
                    val month = calendar.get(Calendar.MONTH)
                    val day = calendar.get(Calendar.DAY_OF_MONTH)


                    datePickerDateBirthday.init(year, month, day, null)

// Добавляем слушатель нажатия на DatePicker
                    datePickerDateBirthday.setOnClickListener {
                        val datePickerDialog = DatePickerDialog(
                            requireContext(),
                            { _, pickedYear, pickedMonth, pickedDay ->
                                // Обновляем значение в базе данных при изменении даты
                                val newDate = Calendar.getInstance().apply {
                                    set(Calendar.YEAR, pickedYear)
                                    set(Calendar.MONTH, pickedMonth)
                                    set(Calendar.DAY_OF_MONTH, pickedDay)
                                }.time

                                // Обновляем значение в базе данных (здесь вам нужно использовать ваш способ обновления данных)
                               selectedDateOfBirthday = newDate

                                // Устанавливаем новую дату в DatePicker после изменения
                                datePickerDateBirthday.updateDate(pickedYear, pickedMonth, pickedDay)
                            },
                            year,
                            month,
                            day
                        )

                        // Отображаем диалоговое окно выбора даты
                        datePickerDialog.show()
                    }


                    val genders = arrayOf("Мужской", "Женский")
                    var gender = ""
                    viewLifecycleOwner.lifecycleScope.launch {

                        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, genders)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerGender.adapter = adapter

                        if (genders.isNotEmpty()) {
                            // Предположим, что у вас есть начальный объект AdressOfDepartment, который вы хотите выбрать
                            val initialGender: String = workerDetails.gender// ваш объект AdressOfDepartment
                            val initialPosition = genders.indexOf(initialGender)
                            spinnerGender.setSelection(initialPosition)
                        }
                    }

                    spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                            // Получаем выбранный элемент из Spinner
                            val selectedGender = spinnerGender.selectedItem as String?
                            // Теперь у вас есть доступ к данным выбранного адреса
                            if (selectedGender != null) {
                                val itemGender = selectedGender
                                gender = itemGender
                            }
                        }

                        override fun onNothingSelected(parentView: AdapterView<*>) {
                            // Обработка события, когда ничего не выбрано
                        }
                    }

                    editTextCity.setText(workerDetails.city)
                    editTextStreet.setText(workerDetails.street)
                    editTextHouse.setText(workerDetails.house)

                    val familyPoses = arrayOf("Не в браке", "В браке")
                    var familyPos = ""
                    viewLifecycleOwner.lifecycleScope.launch {

                        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, familyPoses)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinerFamilyPos.adapter = adapter

                        if (familyPoses.isNotEmpty()) {

                            val initialFamilyPos: String = workerDetails.familyPosition
                            val initialPosition = familyPoses.indexOf(initialFamilyPos)
                            spinerFamilyPos.setSelection(initialPosition)
                        }
                    }

                    spinerFamilyPos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                            // Получаем выбранный элемент из Spinner
                            val selectedFamilyPos = spinerFamilyPos.selectedItem as String?
                            // Теперь у вас есть доступ к данным выбранного адреса
                            if (selectedFamilyPos != null) {
                                val itemFamilyPos = selectedFamilyPos
                                familyPos = itemFamilyPos
                            }
                        }

                        override fun onNothingSelected(parentView: AdapterView<*>) {
                            // Обработка события, когда ничего не выбрано
                        }
                    }

                    editTextCoutChildren.setText(workerDetails.countChildren.toString())
                    editTextPhone.setText(workerDetails.phone)
                    editTextEmail.setText(workerDetails.email)

                    editTextSnils.setText(workerDetails.numberSnils)

                    editTextSeriesPass.setText(workerDetails.seriesPass)
                    editTextNumberPass.setText(workerDetails.numberPass)
                    editTextIssuedByWhom.setText(workerDetails.issuedByWhom)

                    val dateOfIssue = workerDetails.dateOfIssue
                    val calendarIssue = Calendar.getInstance()
                    calendarIssue.time = dateOfIssue

                    val yearIssue = calendarIssue.get(Calendar.YEAR)
                    val monthIssue = calendarIssue.get(Calendar.MONTH)
                    val dayIssue = calendarIssue.get(Calendar.DAY_OF_MONTH)

                    datePickerDateOfIssue.init(yearIssue, monthIssue, dayIssue, null)

// Добавляем слушатель нажатия на DatePicker
                    datePickerDateOfIssue.setOnClickListener {
                        val datePickerDialog = DatePickerDialog(
                            requireContext(),
                            { _, pickedYear, pickedMonth, pickedDay ->
                                // Обновляем значение в базе данных при изменении даты
                                val newDate = Calendar.getInstance().apply {
                                    set(Calendar.YEAR, pickedYear)
                                    set(Calendar.MONTH, pickedMonth)
                                    set(Calendar.DAY_OF_MONTH, pickedDay)
                                }.time

                                // Обновляем значение в базе данных (здесь вам нужно использовать ваш способ обновления данных)
                                selectedDateOfIssue = newDate

                                // Устанавливаем новую дату в DatePicker после изменения
                                datePickerDateOfIssue.updateDate(pickedYear, pickedMonth, pickedDay)
                            },
                            yearIssue,
                            monthIssue,
                            dayIssue
                        )

                        // Отображаем диалоговое окно выбора даты
                        datePickerDialog.show()
                    }


                    editTextCodePodraz.setText(workerDetails.divisionCode)

                    editTextInn.setText(workerDetails.numberInn)

                    editTextDescription.setText(workerDetails.descriptionWorker)

                    editTextMilitaryTitle.setText(workerDetails.militaryTitle)

                    val shelfLifes = arrayOf("A1", "A2", "A3", "A4", "Б1", "Б2", "Б3", "Б4", "В", "Г", "Д")
                    var shelfLife = ""
                    viewLifecycleOwner.lifecycleScope.launch {

                        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, shelfLifes)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerShelfLife.adapter = adapter

                        if (shelfLifes.isNotEmpty()) {

                            val initialShelfLife: String = workerDetails.shelfLife
                            val initialPosition = shelfLifes.indexOf(initialShelfLife)
                            spinnerShelfLife.setSelection(initialPosition)
                        }
                    }

                    spinnerShelfLife.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                            // Получаем выбранный элемент из Spinner
                            val selectedShelfLife = spinnerShelfLife.selectedItem as String?
                            // Теперь у вас есть доступ к данным выбранного адреса
                            if (selectedShelfLife != null) {
                                val itemShelfLife = selectedShelfLife
                                shelfLife = itemShelfLife
                            }
                        }

                        override fun onNothingSelected(parentView: AdapterView<*>) {
                            // Обработка события, когда ничего не выбрано
                        }
                    }

                    val stockCategoryes = arrayOf("1", "2", "3")
                    var stockCategory = ""
                    viewLifecycleOwner.lifecycleScope.launch {

                        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, stockCategoryes)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerStockCatagory.adapter = adapter

                        if (stockCategoryes.isNotEmpty()) {

                            val initialStockCatagory: Int = workerDetails.stockCategory
                            val initialPosition = stockCategoryes.indexOf(initialStockCatagory.toString())
                            spinnerStockCatagory.setSelection(initialPosition)
                        }
                    }

                    spinnerStockCatagory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                            // Получаем выбранный элемент из Spinner
                            val selectedStockCatagory = spinnerStockCatagory.selectedItem as String?
                            // Теперь у вас есть доступ к данным выбранного адреса
                            if (selectedStockCatagory != null) {
                                val itemStockCatagory = selectedStockCatagory
                                stockCategory = itemStockCatagory
                            }
                        }

                        override fun onNothingSelected(parentView: AdapterView<*>) {
                            // Обработка события, когда ничего не выбрано
                        }
                    }

                    editTextProfile.setText(workerDetails.profile)
                    editTextVUS.setText(workerDetails.vus)
                    editTextNameKomis.setText(workerDetails.nameKomis)


                    updateWorker.setOnClickListener {
                        val surname = editTextSurname.text.toString()
                        val name = editTextName.text.toString()
                        val middlename = editTextMiddlename.text.toString()


                        if(selectedDateOfBirthday ==null){
                            selectedDateOfBirthday = workerDetails.dateOfBirth
                        }
                        val dateBirthday: Date? = selectedDateOfBirthday

                        val city = editTextCity.text.toString()
                        val street = editTextStreet.text.toString()
                        val house = editTextHouse.text.toString()

                        val countChildren = editTextCoutChildren.text.toString()

                        val phone = editTextPhone.text.toString()

                        val email = editTextEmail.text.toString()

                        val snils = editTextSnils.text.toString()

                        val seriesPass = editTextSeriesPass.text.toString()
                        val numberPass = editTextNumberPass.text.toString()
                        val issuedByWhom = editTextIssuedByWhom.text.toString()


                        if(selectedDateOfIssue == null){
                            selectedDateOfIssue = workerDetails.dateOfIssue
                        }

                        val dateOfIssue: Date? = selectedDateOfIssue

                        val codPodraz = editTextCodePodraz.text.toString()

                        val inn = editTextInn.text.toString()

                        val descriptionWorker = editTextDescription.text.toString()

                        val militaryTitle = editTextMilitaryTitle.text.toString()
                        val profile = editTextProfile.text.toString()
                        val vus = editTextVUS.text.toString()
                        val nameKomis = editTextNameKomis.text.toString()


                        GlobalScope.launch(Dispatchers.Main) {
                            resourseImage?.let { it1 -> Log.d("Image", it1) }
                            if(resourseImage==""||resourseImage==null){
                                resourseImage = workerDetails.photo
                            }

                            val success = repository.updateWorker(workerDetails?.id.toString(),name, surname, middlename, phone, dateBirthday, city, street, house, familyPos, countChildren, email, seriesPass, numberPass, issuedByWhom, dateOfIssue, codPodraz, snils, inn, gender, militaryTitle, shelfLife, stockCategory, profile, vus, nameKomis, resourseImage, descriptionWorker, workerDetails.dismiss)
                            // Обработка результата (например, обновление UI)
                            if (success) {
                                findNavController().navigate(R.id.action_nav_workerupdate_to_nav_worker)
                            } else {

                            }
                        }
                    }

                }



            }
        }


        return view
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
    }

    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri = data.data!!

            // Теперь у вас есть URI выбранного изображения
            // Вы можете использовать его, например, установив его в ImageView:
            val imageView: ImageView? = view?.findViewById(R.id.imageViewWorkerUpdate)
            imageView?.setImageURI(selectedImageUri)

            val contentResolver = requireContext().contentResolver
            val cursor: Cursor? = contentResolver.query(selectedImageUri, null, null, null, null)

            try {
                cursor?.let {
                    if (it.moveToFirst()) {
                        resourseImage = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                        Log.d("ImageName", resourseImage.toString())
                    }
                }
            } finally {
                cursor?.close()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            selectedWorkerId = it.getString("selectedWorkerId")
        }
    }


}