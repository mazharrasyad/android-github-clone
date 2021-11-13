package com.muhazharrasyad.aplikasigithubuserketiga.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.muhazharrasyad.aplikasigithubuserketiga.controller.DetailActivity
import com.muhazharrasyad.aplikasigithubuserketiga.adapter.FollowerAdapter
import com.muhazharrasyad.aplikasigithubuserketiga.databinding.FragmentFollowerBinding
import com.muhazharrasyad.aplikasigithubuserketiga.model.Follower
import com.muhazharrasyad.aplikasigithubuserketiga.model.Github
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.util.ArrayList

class FollowerFragment : Fragment() {
    private lateinit var binding: FragmentFollowerBinding
    private val listFollower = ArrayList<Follower>()
    private lateinit var adapter: FollowerAdapter

    companion object {
        private val TAG = FollowerFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowerBinding.inflate(inflater, container, false)
        binding.rvFollower.layoutManager = LinearLayoutManager(activity)
        binding.rvFollower.adapter = FollowerAdapter(listFollower)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = FollowerAdapter(listFollower)
        listFollower.clear()
        val follower = activity?.intent?.getParcelableExtra<Github>(DetailActivity.EXTRA_GITHUB) as Github
        getListFollower(follower.username.toString())
    }

    private fun getListFollower(username: String) {
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token INSERT_HERE")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$username/followers"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                binding.progressBarFollower.visibility = View.INVISIBLE

                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONArray(result)

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val username = jsonObject.getString("login")
                        val avatar = jsonObject.getString("avatar_url")
                        val follower = Follower()
                        follower.username = username
                        follower.avatar = avatar
                        listFollower.add(follower)
                    }

                    showRecyclerList()
                } catch (e: Exception) {
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }

            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                binding.progressBarFollower.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showRecyclerList() {
        binding.rvFollower.layoutManager = LinearLayoutManager(activity)
        binding.rvFollower.adapter = adapter
    }
}