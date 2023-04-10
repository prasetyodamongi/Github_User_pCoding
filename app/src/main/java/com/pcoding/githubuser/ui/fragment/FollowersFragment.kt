package com.pcoding.githubuser.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pcoding.githubuser.R
import com.pcoding.githubuser.adapter.UserRecyclerViewAdapter
import com.pcoding.githubuser.data.User
import com.pcoding.githubuser.databinding.FragmentFollowBinding
import com.pcoding.githubuser.ui.detail.DetailActivity

class FollowersFragment : Fragment(R.layout.fragment_follow) {

    private var _binding : FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FollowersViewModel
    private lateinit var adapter: UserRecyclerViewAdapter

    private lateinit var username: String

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val args = arguments
        username = args?.getString(DetailActivity.EXTRA_USERNAME).toString()

        _binding = FragmentFollowBinding.inflate(inflater,container,false)
        val view = binding.root

        adapter = UserRecyclerViewAdapter()
        adapter.notifyDataSetChanged()

        binding.apply {
            followList.setHasFixedSize(true)
            followList.layoutManager = LinearLayoutManager(activity)
            followList.adapter = adapter
        }

        showLoading(true)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[FollowersViewModel::class.java]
        viewModel.setListFollowers(username)
        viewModel.getListFollowers().observe(viewLifecycleOwner) {
            if (it != null) {
                //set list followers
                adapter.setList(it)
                showLoading(false)
            }
        }

        adapter.setOnItemClickCallback(object : UserRecyclerViewAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                Intent(activity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.EXTRA_USERNAME, data.login)
                    startActivity(it)
                    activity?.finish()
                }
            }
        })

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}