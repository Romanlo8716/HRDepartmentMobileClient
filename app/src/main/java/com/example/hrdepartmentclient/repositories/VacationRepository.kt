package com.example.hrdepartmentclient.repositories

import com.example.hrdepartmentclient.models.MedicalBook
import com.example.hrdepartmentclient.models.Vacation
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.reflect.Type
import java.net.HttpURLConnection
import java.net.URL

class VacationRepository {

    private val url = "10.0.2.2"

    suspend fun getVacation(id: Int?): List<Vacation>? = withContext(
        Dispatchers.IO) {
        val url = URL("http://$url:8080/getDescriptionWorkerVacations/$id")
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

    private fun parseJsonToList(jsonString: String): List<Vacation> {
        val gson = Gson()
        val listType: Type = object : TypeToken<List<Vacation>>() {}.type

        return gson.fromJson(jsonString, listType)
    }
}