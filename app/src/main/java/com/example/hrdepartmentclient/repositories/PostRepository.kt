package com.example.hrdepartmentclient.repositories

import com.example.hrdepartmentclient.models.AdressOfDepartment
import com.example.hrdepartmentclient.models.Post
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

class PostRepository {

    private val url = "10.0.2.2"

    public fun getPost(): List<Post> {
        val url = URL("http://$url:8080/getPosts")
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

    private fun parseJsonToList(jsonString: String): List<Post> {
        val gson = Gson()
        val listType: Type = object : TypeToken<List<Post>>() {}.type

        return gson.fromJson(jsonString, listType)
    }

    suspend fun getPostDetailsById(id: String): Post? = withContext(Dispatchers.IO) {
        val url = URL("http://$url:8080/getPostById/$id")
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

    private fun parseJsonToObject(jsonString: String): Post? {
        val gson = Gson()
        return gson.fromJson(jsonString, Post::class.java)
    }

    suspend fun createPost(namePost: String, salary: String): Boolean {
        val url = "http://$url:8080/createPosts"

        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()

            // Используйте MediaType.parse для создания MediaType из строки
            val mediaType = MediaType.parse("application/json; charset=utf-8")

            // Создание JSON-объекта с данными
            val data = JSONObject()
            data.put("namePost", namePost)
            data.put("salary", salary)

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

    suspend fun deletePost(id: String): Boolean {
        val url = "http://$url:8080/deletePost/$id"

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

    suspend fun updatePost(id: String, namePost: String, salary: String): Boolean {
        val url = "http://$url:8080/updatePost/$id"

        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()

            // Используйте MediaType.parse для создания MediaType из строки
            val mediaType = MediaType.parse("application/json; charset=utf-8")

            // Создание JSON-объекта с данными
            val data = JSONObject()
            data.put("namePost", namePost)
            data.put("salary", salary)


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