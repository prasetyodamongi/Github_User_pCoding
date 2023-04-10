package com.pcoding.githubuser.ui.main

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pcoding.githubuser.R
import com.pcoding.githubuser.adapter.UserRecyclerViewAdapter
import com.pcoding.githubuser.data.User
import com.pcoding.githubuser.databinding.ActivityMainBinding
import com.pcoding.githubuser.ui.detail.DetailActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserRecyclerViewAdapter
    private lateinit var userViewModel: UserViewModel


    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setBackgroundDrawable(ColorDrawable(getColor(R.color.color_4)))
        showOpenText(true)

        listData()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listData() {
        adapter = UserRecyclerViewAdapter()
        adapter.notifyDataSetChanged()

        userViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[UserViewModel::class.java]
        binding.apply {
            userList.layoutManager = LinearLayoutManager(this@MainActivity)
            userList.setHasFixedSize(true)
            userList.adapter = adapter
        }

        userViewModel.getSearchUser().observe(this) {
            if (it != null) {
                adapter.setList(it)
                showLoading(false)
            }
        }

        adapter.setOnItemClickCallback(object : UserRecyclerViewAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                Intent(this@MainActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.EXTRA_USERNAME, data.login)
                    startActivity(it)
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                showOpenText(false)
                showLoading(true)
                userViewModel.setSearchUser(query)
                searchView.clearFocus()

                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    showLoading(false)
                } else {
                    showOpenText(false)
                    showLoading(true)
                    userViewModel.setSearchUser(newText)
                }
                return true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return false
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showOpenText(isText: Boolean) {
        binding.openingText.visibility = if (isText) View.VISIBLE else View.GONE
    }
}