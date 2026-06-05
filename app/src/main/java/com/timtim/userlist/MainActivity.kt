package com.timtim.userlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.timtim.userlist.data.remote.ApiService
import com.timtim.userlist.data.repository.UserRepository
import com.timtim.userlist.ui.screens.UserDetailScreen
import com.timtim.userlist.ui.screens.UserListScreen
import com.timtim.userlist.ui.theme.UserListTheme
import com.timtim.userlist.viewmodel.UserListViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val apiService = ApiService.create()
        val repository = UserRepository(apiService)
        
        setContent {
            UserListTheme {
                val navController = rememberNavController()
                val viewModel: UserListViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            @Suppress("UNCHECKED_CAST")
                            return UserListViewModel(repository) as T
                        }
                    }
                )
                
                NavHost(navController = navController, startDestination = "userList") {
                    composable("userList") {
                        UserListScreen(
                            viewModel = viewModel,
                            onUserClick = { user ->
                                viewModel.selectUser(user)
                                navController.navigate("userDetail")
                            }
                        )
                    }
                    composable("userDetail") {
                        val selectedUser by viewModel.selectedUser.collectAsState()
                        selectedUser?.let { user ->
                            UserDetailScreen(
                                user = user,
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
