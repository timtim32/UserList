package com.timtim.userlist.data.repository

import com.timtim.userlist.data.model.User
import com.timtim.userlist.data.remote.ApiService

class UserRepository(private val apiService: ApiService) {
    suspend fun getUsers(): List<User> {
        return try {
            val response = apiService.getUsers()
            response.results
        } catch (e: Exception) {
            emptyList()
        }
    }
}
