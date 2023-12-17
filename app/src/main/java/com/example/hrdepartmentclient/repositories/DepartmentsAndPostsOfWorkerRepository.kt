package com.example.hrdepartmentclient.repositories

import com.example.hrdepartmentclient.models.DepartmentsAndPostsOfWorker
import com.example.hrdepartmentclient.models.Post
import com.example.hrdepartmentclient.models.Worker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.reflect.Type
import java.net.HttpURLConnection
import java.net.URL

class DepartmentsAndPostsOfWorkerRepository {

    private val url = "10.0.2.2"

    public suspend fun getDepartmentsAndPostsOfWorker(): List<DepartmentsAndPostsOfWorker> = withContext(Dispatchers.IO) {
        val url = URL("http://$url:8080/getDepartmentsAndPostsOfWorker")
        val connection = url.openConnection() as HttpURLConnection

        return@withContext try {
            connection.connect()


            val inputStream = connection.inputStream
            val response = inputStream.bufferedReader().use { it.readText() }

            parseJsonToList(response)
        } finally {
            connection.disconnect()
        }
    }


    suspend fun getDescriptionWorkerDepartmentsAndPosts(id: Int?): List<DepartmentsAndPostsOfWorker>? = withContext(Dispatchers.IO) {
        val url = URL("http://$url:8080/getDescriptionWorkerDepartmentsAndPosts/$id")
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

    public suspend fun getDepartmentsAndPostsOfWorkerByDepartmentId( id: String): List<DepartmentsAndPostsOfWorker> = withContext(Dispatchers.IO) {
        val url = URL("http://$url:8080/getDepartmentsAndPostsOfWorkerByDepartmentId/$id")
        val connection = url.openConnection() as HttpURLConnection

        return@withContext try {
            connection.connect()


            val inputStream = connection.inputStream
            val response = inputStream.bufferedReader().use { it.readText() }

            parseJsonToList(response)
        } finally {
            connection.disconnect()
        }
    }

    public suspend fun getWorkerOnDepartmentByDepartmentId( id: String): List<DepartmentsAndPostsOfWorker> = withContext(Dispatchers.IO) {
        val url = URL("http://$url:8080/getWorkerOnDepartmentByDepartmentId/$id")
        val connection = url.openConnection() as HttpURLConnection

        return@withContext try {
            connection.connect()


            val inputStream = connection.inputStream
            val response = inputStream.bufferedReader().use { it.readText() }

            parseJsonToList(response)
        } finally {
            connection.disconnect()
        }
    }

    private fun parseJsonToList(jsonString: String): List<DepartmentsAndPostsOfWorker> {
        val gson = Gson()
        val listType: Type = object : TypeToken<List<DepartmentsAndPostsOfWorker>>() {}.type

        return gson.fromJson(jsonString, listType)
    }
}