package com.muhazharrasyad.aplikasigithubuserketiga.controller

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.muhazharrasyad.aplikasigithubuserketiga.R
import com.muhazharrasyad.aplikasigithubuserketiga.adapter.GithubAdapter
import com.muhazharrasyad.aplikasigithubuserketiga.databinding.ActivityMainBinding
import com.muhazharrasyad.aplikasigithubuserketiga.model.Github
import java.util.ArrayList
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val listGithub = ArrayList<Github>()

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Github User"
        binding.progressBar.visibility = View.INVISIBLE
        binding.tvEmpty.visibility = View.INVISIBLE
    }

    private fun showRecyclerList() {
        binding.listGithubs.layoutManager = LinearLayoutManager(this)
        val listGithubAdapter = GithubAdapter(listGithub)
        binding.listGithubs.adapter = listGithubAdapter

        listGithubAdapter.setOnItemClickCallback(object : GithubAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Github) {
                getDetailGithubs(data.username.toString())
            }
        })
    }

    private fun getListGithubs(username: String) {
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token INSERT_HERE")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/search/users?q=$username"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                binding.tvHelp.visibility = View.INVISIBLE
                binding.progressBar.visibility = View.INVISIBLE

                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val responseObject = JSONObject(result)
                    val jsonArray = responseObject.getJSONArray("items")

                    if (jsonArray.length() != 0) {
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val username = jsonObject.getString("login")
                            val avatar = jsonObject.getString("avatar_url")
                            val github = Github()
                            github.username = username
                            github.avatar = avatar
                            listGithub.add(github)
                        }
                    } else{
                        binding.tvEmpty.visibility = View.VISIBLE
                    }

                    showRecyclerList()
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }

            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getDetailGithubs(username: String) {
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token INSERT_HERE")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$username"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                binding.tvHelp.visibility = View.INVISIBLE
                binding.progressBar.visibility = View.INVISIBLE

                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonObject = JSONObject(result)
                    val username = jsonObject.getString("login")
                    val name = jsonObject.getString("name")
                    val avatar = jsonObject.getString("avatar_url")
                    val github = Github()
                    github.username = username
                    github.name = name
                    github.avatar = avatar
                    val moveWithDataIntent = Intent(this@MainActivity, DetailActivity::class.java)
                    moveWithDataIntent.putExtra(DetailActivity.EXTRA_GITHUB, github)
                    startActivity(moveWithDataIntent)
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }

            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.menu_search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.menu_search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                listGithub.clear()
                getListGithubs(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_favorite -> {
                val i = Intent(this, FavoriteActivity::class.java)
                startActivity(i)
                return true
            }
            R.id.menu_setting -> {
                val i = Intent(this, SettingActivity::class.java)
                startActivity(i)
                return true
            }
            else -> return true
        }
    }
}