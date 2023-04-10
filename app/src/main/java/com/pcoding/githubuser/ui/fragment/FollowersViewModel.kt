package com.pcoding.githubuser.ui.fragment

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pcoding.githubuser.api.ApiConfig
import com.pcoding.githubuser.data.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel: ViewModel() {

    private val followers = MutableLiveData<ArrayList<User>>()
    private val isLoading = MutableLiveData<Boolean>()

    fun setListFollowers(username: String) {
        isLoading.postValue(true)
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<ArrayList<User>> {
            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>) {
                isLoading.postValue(false)
                if (response.isSuccessful) {
                    followers.postValue(response.body())
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                isLoading.postValue(false)
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun getListFollowers(): LiveData<ArrayList<User>> {
        return followers
    }
}