package com.example.hrdepartmentclient.repositories

import android.util.Log
import com.example.hrdepartmentclient.models.Post
import com.example.hrdepartmentclient.models.Worker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.lang.reflect.Type
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WorkerRepository {

    private val url = "10.0.2.2"

    public fun getWorker(): List<Worker> {
        val url = URL("http://$url:8080/getWorkers")
        val connection = url.openConnection() as HttpURLConnection

        return try {
            connection.connect()
            val inputStream = connection.inputStream
            val response = inputStream.bufferedReader().use { it.readText() }

            parseJsonToList(response)
        } finally {
            connection.disconnect()
        }
    }

    private fun parseJsonToList(jsonString: String): List<Worker> {
        val gson = Gson()
        val listType: Type = object : TypeToken<List<Worker>>() {}.type

        return gson.fromJson(jsonString, listType)
    }

    suspend fun getWorkerDetailsById(id: String): Worker? = withContext(Dispatchers.IO) {
        val url = URL("http://$url:8080/getWorkerById/$id")
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

    private fun parseJsonToObject(jsonString: String): Worker? {
        val gson = Gson()
        return gson.fromJson(jsonString, Worker::class.java)
    }

    suspend fun createWorker(name: String, surname: String, patronymic : String, phone: String, dateOfBirth: Date?, city: String, street:String, house:String, familyPosition: String, countChildren : String, email:String, seriesPass: String, numberPass:String, issuedByWhom:String, dateOfIssue: Date?, divisionCode: String, numberSnils: String, numberInn: String, gender: String, militaryTitle: String, shelfLife: String, stockCategory : String, profile: String, vus: String, nameKomis: String, photo: String?, descriptionWorker: String, dismiss: Boolean): Boolean {
        Log.d("Photo:", photo.toString())


        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.getDefault())
        val formattedDateDateOfBirth = dateFormat.format(dateOfBirth)
        val formattedDateDateOfIssue = dateFormat.format(dateOfIssue)

        Log.d("Date:", formattedDateDateOfBirth.toString())
        val url = "http://$url:8080/createWorkers"

        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()

            // Используйте MediaType.parse для создания MediaType из строки
            val mediaType = MediaType.parse("application/json; charset=utf-8")

            // Создание JSON-объекта с данными
            val data = JSONObject()
            data.put("name", name)
            data.put("surname", surname)
            data.put("patronymic", patronymic)
            data.put("phone", phone)
            data.put("dateOfBirth", formattedDateDateOfBirth)
            data.put("city", city)
            data.put("street", street)
            data.put("house", house)
            data.put("familyPosition", familyPosition)
            data.put("countChildren", countChildren)
            data.put("email", email)
            data.put("seriesPass", seriesPass)
            data.put("numberPass", numberPass)
            data.put("issuedByWhom", issuedByWhom)
            data.put("dateOfIssue", formattedDateDateOfIssue)
            data.put("divisionCode", divisionCode)
            data.put("numberSnils", numberSnils)
            data.put("numberInn", numberInn)
            data.put("gender", gender)
            data.put("militaryTitle", militaryTitle)
            data.put("shelfLife", shelfLife)
            data.put("stockCategory", stockCategory)
            data.put("profile", profile)
            data.put("vus", vus)
            data.put("nameKomis", nameKomis)
            data.put("photo", photo)
            data.put("descriptionWorker", descriptionWorker)
            data.put("dismiss", dismiss)



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
    suspend fun deleteWorker(id: String): Boolean {
        val url = "http://$url:8080/deleteWorker/$id"

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

    suspend fun dismissWorker(id: String): Boolean {
        val url = "http://$url:8080/dismissWorker/$id"

        val data = JSONObject()

        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()


            data.put("id", id)
            val mediaType = MediaType.parse("application/json; charset=utf-8")
            // Отправка POST-запроса с использованием OkHttp
            val requestBody = RequestBody.create(mediaType, data.toString())
            val request = Request.Builder()
                .url(url)
                .put(requestBody)
                .build()

            val response = client.newCall(request).execute()

            response.isSuccessful
        }
    }

    suspend fun recoveryWorker(id: String): Boolean {
        val url = "http://$url:8080/recoveryWorker/$id"

        val data = JSONObject()

        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()


            data.put("id", id)
            val mediaType = MediaType.parse("application/json; charset=utf-8")
            // Отправка POST-запроса с использованием OkHttp
            val requestBody = RequestBody.create(mediaType, data.toString())
            val request = Request.Builder()
                .url(url)
                .put(requestBody)
                .build()

            val response = client.newCall(request).execute()

            response.isSuccessful
        }
    }

    suspend fun updateWorker(id: String, name: String, surname: String, patronymic : String, phone: String, dateOfBirth: Date?, city: String, street:String, house:String, familyPosition: String, countChildren : String, email:String, seriesPass: String, numberPass:String, issuedByWhom:String, dateOfIssue: Date?, divisionCode: String, numberSnils: String, numberInn: String, gender: String, militaryTitle: String, shelfLife: String, stockCategory : String, profile: String, vus: String, nameKomis: String, photo: String?, descriptionWorker: String, dismiss: Boolean): Boolean {

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.getDefault())
        val formattedDateDateOfBirth = dateFormat.format(dateOfBirth)
        val formattedDateDateOfIssue = dateFormat.format(dateOfIssue)

        val url = "http://$url:8080/updateWorker/$id"

        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()

            // Используйте MediaType.parse для создания MediaType из строки
            val mediaType = MediaType.parse("application/json; charset=utf-8")

            // Создание JSON-объекта с данными
            val data = JSONObject()
            data.put("name", name)
            data.put("surname", surname)
            data.put("patronymic", patronymic)
            data.put("phone", phone)
            data.put("dateOfBirth", formattedDateDateOfBirth)
            data.put("city", city)
            data.put("street", street)
            data.put("house", house)
            data.put("familyPosition", familyPosition)
            data.put("countChildren", countChildren)
            data.put("email", email)
            data.put("seriesPass", seriesPass)
            data.put("numberPass", numberPass)
            data.put("issuedByWhom", issuedByWhom)
            data.put("dateOfIssue", formattedDateDateOfIssue)
            data.put("divisionCode", divisionCode)
            data.put("numberSnils", numberSnils)
            data.put("numberInn", numberInn)
            data.put("gender", gender)
            data.put("militaryTitle", militaryTitle)
            data.put("shelfLife", shelfLife)
            data.put("stockCategory", stockCategory)
            data.put("profile", profile)
            data.put("vus", vus)
            data.put("nameKomis", nameKomis)
            data.put("photo", photo)
            data.put("descriptionWorker", descriptionWorker)
            data.put("dismiss", dismiss)


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