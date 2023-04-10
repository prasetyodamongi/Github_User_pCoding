package com.pcoding.githubuser.ui.main

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pcoding.githubuser.api.ApiConfig
import com.pcoding.githubuser.data.User
import com.pcoding.githubuser.data.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {

    private val listUser = MutableLiveData<ArrayList<User>>()
    private val isLoading = MutableLiveData<Boolean>()

    fun setSearchUser(username: String) {
        isLoading.postValue(true)
        val client = ApiConfig.getApiService().getSearchUser(username)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>) {
                isLoading.postValue(false)
                if (response.isSuccessful) {
                    listUser.postValue(response.body()?.items)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                isLoading.postValue(false)
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun getSearchUser(): MutableLiveData<ArrayList<User>> {
        return listUser
    }
}