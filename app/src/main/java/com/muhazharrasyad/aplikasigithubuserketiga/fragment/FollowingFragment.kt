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
import com.muhazharrasyad.aplikasigithubuserketiga.adapter.FollowingAdapter
import com.muhazharrasyad.aplikasigithubuserketiga.databinding.FragmentFollowingBinding
import com.muhazharrasyad.aplikasigithubuserketiga.model.Following
import com.muhazharrasyad.aplikasigithubuserketiga.model.Github
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.util.ArrayList

class FollowingFragment : Fragment() {
    private lateinit var binding: FragmentFollowingBinding
    private val listFollowing = ArrayList<Following>()
    private lateinit var adapter: FollowingAdapter

    companion object {
        private val TAG = FollowingFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        binding.rvFollowing.layoutManager = LinearLayoutManager(activity)
        binding.rvFollowing.adapter = FollowingAdapter(listFollowing)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = FollowingAdapter(listFollowing)
        listFollowing.clear()
        val following = activity?.intent?.getParcelableExtra<Github>(DetailActivity.EXTRA_GITHUB) as Github
        getListFollowing(following.username.toString())
    }

    private fun getListFollowing(username: String) {
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token INSERT_HERE")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$username/following"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                binding.progressBarFollowing.visibility = View.INVISIBLE

                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONArray(result)

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val username = jsonObject.getString("login")
                        val avatar = jsonObject.getString("avatar_url")
                        val following = Following()
                        following.username = username
                        following.avatar = avatar
                        listFollowing.add(following)
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
                binding.progressBarFollowing.visibility = View.INVISIBLE
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
        binding.rvFollowing.layoutManager = LinearLayoutManager(activity)
        binding.rvFollowing.adapter = adapter
    }
}