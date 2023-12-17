package com.example.hrdepartmentclient.repositories

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.util.Log
import com.example.hrdepartmentclient.models.AdressOfDepartment
import com.example.hrdepartmentclient.models.Department
import com.example.hrdepartmentclient.models.DepartmentAndCity
import com.example.hrdepartmentclient.models.Post
import com.example.hrdepartmentclient.models.PostOfDepartment
import com.example.hrdepartmentclient.models.Vacation
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.reflect.Type
import java.net.HttpURLConnection
import java.net.URL

class DepartmentRepository {

    private val url = "10.0.2.2"

    suspend fun getDepartment(): List<DepartmentAndCity> {
        return withContext(Dispatchers.IO) {
            // Выполните сетевой запрос здесь
            // Не обращайтесь к UI-элементам в этом блоке

            // Пример:
            val url = "http://$url:8080/getDepartmentGroupByCity"
            val response = URL(url).readText()

            // Здесь распарсите response в список DepartmentAndCity
            // Пример:
            val departmentsAndCities: List<DepartmentAndCity> = parseJsonToList(response)

            departmentsAndCities
        }
    }



    public fun getDepartmentByCity(city: String): List<Department> {
        val url = URL("http://$url:8080/getDepartmentByCity/" + city)
        val connection = url.openConnection() as HttpURLConnection

        return try {
            connection.connect()

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val response = inputStream.bufferedReader().use { it.readText() }

                // Добавим вывод в лог для отладки
                Log.d("DepartmentRepository", "Server response: $response")

                parseJsonToListDepartment(response)
            } else {
                // Обработка ошибки, например, выбрасывание исключения
                throw RuntimeException("Failed to retrieve data. Response code: ${connection.responseCode}")
            }
        } finally {
            connection.disconnect()
        }
    }

    private fun parseJsonToList(jsonString: String): List<DepartmentAndCity> {
        val gson = Gson()
        val departmentAndCityList = mutableListOf<DepartmentAndCity>()

        // Преобразуем строку в массив JSON
        val jsonArray = JsonParser.parseString(jsonString).asJsonArray

        // Проходим по каждому элементу массива JSON
        for (jsonElement in jsonArray) {
            // Извлекаем JSON-строку из элемента массива
            val departmentAndCityString = jsonElement.asString

            // Преобразуем JSON-строку в объект JsonObject
            val jsonObject = JsonParser.parseString(departmentAndCityString).asJsonObject

            // Извлекаем данные из JsonObject и преобразуем в объект DepartmentAndCity
            val departmentAndCity = gson.fromJson(jsonObject, DepartmentAndCity::class.java)

            // Добавляем в список
            departmentAndCityList.add(departmentAndCity)
        }

        return departmentAndCityList
    }

    private fun parseJsonToListDepartment(jsonString: String): List<Department> {
        val gson = Gson()
        val jsonObject = JsonParser.parseString(jsonString).asJsonObject
        val departments = jsonObject.getAsJsonArray("department")
        return gson.fromJson(departments, object : TypeToken<List<Department>>() {}.type)
    }

    suspend fun getDepartmentDetailsById(id: String): Department? = withContext(Dispatchers.IO) {
        val url = URL("http://$url:8080/getDepartmentById/$id")
        val connection = url.openConnection() as HttpURLConnection

        return@withContext try {
            connection.connect()

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val response = inputStream.bufferedReader().use { it.readText() }
                parseJsonToObject(response)
            } else {
                // Обработка ошибки, например, возврат null или выброс исключения
                null
            }
        } finally {
            connection.disconnect()
        }
    }

    private fun parseJsonToObject(jsonString: String): Department? {
        val gson = Gson()
        return gson.fromJson(jsonString, Department::class.java)
    }

    suspend fun getPostOfDepartment(id: String): List<PostOfDepartment>? = withContext(
        Dispatchers.IO) {
        val url = URL("http://$url:8080/getPostOfDepartmentByDepartmentId/$id")
        val connection = url.openConnection() as HttpURLConnection

        return@withContext try {
            connection.connect()

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val response = inputStream.bufferedReader().use { it.readText() }
                parseJsonToListPostOfDepartment(response)
            } else {
                // Обработка ошибки, например, возврат null или выброс исключения
                null
            }
        } finally {
            connection.disconnect()
        }
    }

    private fun parseJsonToListPostOfDepartment(jsonString: String): List<PostOfDepartment> {
        val gson = Gson()
        val listType: Type = object : TypeToken<List<PostOfDepartment>>() {}.type

        return gson.fromJson(jsonString, listType)
    }

    suspend fun createDepartment(nameDepartment: String, phone: String, idAdress: Int): Boolean {
        Log.d("idAdressOfDepartment", idAdress.toString())

        val url = "http://$url:8080/createDepartments"

        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()

            // Используйте MediaType.parse для создания MediaType из строки
            val mediaType = MediaType.parse("application/json; charset=utf-8")

            // Создание JSON-объекта с данными
            val data = JSONObject()
            data.put("nameDepartment", nameDepartment)
            data.put("phoneNumber", phone)
            data.put("adressOfDepartment", JSONObject().put("id", idAdress))

            // Отправка POST-запроса с использованием OkHttp
            val requestBody = RequestBody.create(mediaType, data.toString())
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()

            response.isSuccessful
        }
    }

    suspend fun deleteDepartment(id: String): Boolean {
        val url = "http://$url:8080/deleteDepartment/$id"

        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url(url)
                .delete()
                .build()

            val response = client.newCall(request).execute()

            response.isSuccessful
        }
    }

    suspend fun updateDepartment(id: String, nameDepartment: String, phone: String, idAdress: Int): Boolean {
        val url = "http://$url:8080/updateDepartment/$id"

        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()

            // Используйте MediaType.parse для создания MediaType из строки
            val mediaType = MediaType.parse("application/json; charset=utf-8")

            // Создание JSON-объекта с данными
            val data = JSONObject()
            data.put("nameDepartment", nameDepartment)
            data.put("phoneNumber", phone)
            data.put("adressOfDepartment", JSONObject().put("id", idAdress))


            // Отправка PUT-запроса с использованием OkHttp
            val requestBody = RequestBody.create(mediaType, data.toString())
            val request = Request.Builder()
                .url(url)
                .put(requestBody)
                .build()

            val response = client.newCall(request).execute()

            response.isSuccessful
        }
    }
}