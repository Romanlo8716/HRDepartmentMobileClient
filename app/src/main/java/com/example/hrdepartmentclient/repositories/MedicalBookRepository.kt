package com.example.hrdepartmentclient.repositories

import com.example.hrdepartmentclient.models.LaborBook
import com.example.hrdepartmentclient.models.MedicalBook
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.reflect.Type
import java.net.HttpURLConnection
import java.net.URL

class MedicalBookRepository {

    private val url = "10.0.2.2"

    suspend fun getMedicalBook(id: Int?): List<MedicalBook>? = withContext(
        Dispatchers.IO) {
        val url = URL("http://$url:8080/getDescriptionWorkerMedicalBooks/$id")
        val connection = url.openConnection() as HttpURLConnection

        return@withContext try {
            connection.connect()

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val response = inputStream.bufferedReader().use { it.readText() }
                parseJsonToList(response)
            } else {
                // Обработка ошибки, например, возврат null или выброс исключения
                null
            }
        } finally {
            connection.disconnect()
        }
    }

    private fun parseJsonToList(jsonString: String): List<MedicalBook> {
        val gson = Gson()
        val listType: Type = object : TypeToken<List<MedicalBook>>() {}.type

        return gson.fromJson(jsonString, listType)
    }
}