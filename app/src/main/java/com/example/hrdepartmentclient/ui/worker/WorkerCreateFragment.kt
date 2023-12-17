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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.Calendar
import java.util.Date

class WorkerCreateFragment: Fragment() {

    //Для даты
    private var selectedDateOfBirthday : Date? = null


    private var selectedDateOfIssue : Date? = null

    //Для фото
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var selectedImageUri: Uri
    private var imageName : String? = ""
    private val READ_EXTERNAL_STORAGE_PERMISSION_CODE = 123

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_workercreate, container, false)
        val workerRepository = WorkerRepository()

        val editTextSurname : EditText = view.findViewById(R.id.editTextSurnameWorker)
        val editTextName : EditText = view.findViewById(R.id.editTextNameWorker)
        val editTextMiddlename : EditText = view.findViewById(R.id.editTextMiddlenameWorker)

        val buttonImageWorker : Button = view.findViewById(R.id.buttonImageWorker)

        val datePickerDateBirthday : DatePicker = view.findViewById(R.id.datePickerDateOfBirth)

        val spinnerGender : Spinner = view.findViewById(R.id.spinnerGender)

        val editTextCity : EditText = view.findViewById(R.id.editTextCityWorker)
        val editTextStreet : EditText = view.findViewById(R.id.editTextStreetWorker)
        val editTextHouse : EditText = view.findViewById(R.id.editTextHomeWorker)

        val spinerFamilyPos : Spinner = view.findViewById(R.id.spinerFamilyPos)

        val editTextCoutChildren : EditText = view.findViewById(R.id.editTextCoutChildren)

        val editTextPhone : EditText = view.findViewById(R.id.editTextPhone)
        val editTextEmail : EditText = view.findViewById(R.id.editTextEmail)

        val editTextSnils : EditText = view.findViewById(R.id.editTextSnils)

        val editTextSeriesPass : EditText = view.findViewById(R.id.editTextSeriesPass)
        val editTextNumberPass : EditText = view.findViewById(R.id.editTextNumberPass)
        val editTextIssuedByWhom : EditText = view.findViewById(R.id.editTextIssuedByWhom)
        val datePickerDateOfIssue : DatePicker = view.findViewById(R.id.datePickerDateOfIssue)
        val editTextCodePodraz : EditText = view.findViewById(R.id.editTextCodePodraz)

        val editTextInn : EditText = view.findViewById(R.id.editTextInn)

        val editTextDescription : EditText = view.findViewById(R.id.editTextDescription)

        val editTextMilitaryTitle : EditText = view.findViewById(R.id.editTextMilitaryTitle)
        val spinnerShelfLife : Spinner = view.findViewById(R.id.spinnerShelfLife)
        val spinnerStockCatagory : Spinner = view.findViewById(R.id.spinnerStockCatagory)
        val editTextProfile : EditText = view.findViewById(R.id.editTextProfile)
        val editTextVUS : EditText = view.findViewById(R.id.editTextVUS)
        val editTextNameKomis : EditText = view.findViewById(R.id.editTextNameKomis)

        val createWorker : Button = view.findViewById(R.id.createWorker)

        //Добавление фото



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


        val today = Calendar.getInstance()

        val calendar = Calendar.getInstance()


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

        var adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, genders)

        // Укажите макет для использования при отображении элементов в раскрывающемся списке
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Примените адаптер к Spinner
        spinnerGender.adapter = adapter


        spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                // Получаем выбранный элемент из Spinner
                val selectedGender = spinnerGender.selectedItem as String
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

        val familyPoses = arrayOf("Не в браке", "В браке")
        var familyPos = ""

        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, familyPoses)

        // Укажите макет для использования при отображении элементов в раскрывающемся списке
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Примените адаптер к Spinner
        spinerFamilyPos.adapter = adapter


        spinerFamilyPos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                // Получаем выбранный элемент из Spinner
                val selectedFamilyPos = spinerFamilyPos.selectedItem as String
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


        val calendarIssue = Calendar.getInstance()


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

        val stockCategoryes = arrayOf("1", "2", "3")
        var stockCategory = ""

        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, stockCategoryes)

        // Укажите макет для использования при отображении элементов в раскрывающемся списке
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Примените адаптер к Spinner
        spinnerStockCatagory.adapter = adapter


        spinnerStockCatagory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                // Получаем выбранный элемент из Spinner
                val selectedStockCatagory = spinnerStockCatagory.selectedItem as String
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


        val shelfLifes = arrayOf("A1", "A2", "A3", "A4", "Б1", "Б2", "Б3", "Б4", "В", "Г", "Д")
        var shelfLife = ""

        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, shelfLifes)

        // Укажите макет для использования при отображении элементов в раскрывающемся списке
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Примените адаптер к Spinner
        spinnerShelfLife.adapter = adapter


        spinnerShelfLife.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                // Получаем выбранный элемент из Spinner
                val selectedShelfLife = spinnerShelfLife.selectedItem as String
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

        createWorker.setOnClickListener {
            val surname = editTextSurname.text.toString()
            val name = editTextName.text.toString()
            val middlename = editTextMiddlename.text.toString()



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


            val dateOfIssue: Date? = selectedDateOfIssue

            val codPodraz = editTextCodePodraz.text.toString()

            val inn = editTextInn.text.toString()

            val descriptionWorker = editTextDescription.text.toString()

            val militaryTitle = editTextMilitaryTitle.text.toString()
            val profile = editTextProfile.text.toString()
            val vus = editTextVUS.text.toString()
            val nameKomis = editTextNameKomis.text.toString()
            val dismiss = false


            if(imageName== null || imageName == ""){
                imageName = "no_photo.jpg"
            }
            GlobalScope.launch(Dispatchers.Main) {
                val success = workerRepository.createWorker(name, surname, middlename, phone, dateBirthday, city, street, house, familyPos, countChildren, email, seriesPass, numberPass, issuedByWhom, dateOfIssue, codPodraz, snils, inn, gender, militaryTitle, shelfLife, stockCategory, profile, vus, nameKomis, imageName, descriptionWorker, dismiss)
                // Обработка результата (например, обновление UI)
                if (success) {
                    findNavController().navigate(R.id.action_nav_workercreate_to_nav_worker)
                } else {

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
          val imageView: ImageView? = view?.findViewById(R.id.imageViewWorkerCreate)
          imageView?.setImageURI(selectedImageUri)

            val contentResolver = requireContext().contentResolver
            val cursor: Cursor? = contentResolver.query(selectedImageUri, null, null, null, null)

            try {
                cursor?.let {
                    if (it.moveToFirst()) {
                        imageName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                        Log.d("ImageName", imageName.toString())
                    }
                }
            } finally {
                cursor?.close()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            READ_EXTERNAL_STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Разрешение получено, открываем галерею
                    openGallery()
                } else {
                    // Разрешение не получено, обрабатываем ситуацию
                    // Можно показать пользователю объяснение, почему разрешение важно, и попробовать запросить еще раз
                }
            }
        }
    }


}