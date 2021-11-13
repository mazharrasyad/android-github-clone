package com.muhazharrasyad.consumerappaplikasigithubuserketiga.controller

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.muhazharrasyad.consumerappaplikasigithubuserketiga.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import com.muhazharrasyad.consumerappaplikasigithubuserketiga.adapter.GithubAdapter
import com.muhazharrasyad.consumerappaplikasigithubuserketiga.databinding.ActivityMainBinding
import com.muhazharrasyad.consumerappaplikasigithubuserketiga.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.muhazharrasyad.consumerappaplikasigithubuserketiga.model.Github
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: GithubAdapter
    private lateinit var binding: ActivityMainBinding
    private var listFavorites: ArrayList<Github> = arrayListOf()

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.setHasFixedSize(true)
        adapter = GithubAdapter(listFavorites)
        binding.rvFavorite.adapter = adapter

        // Content Provider
        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadNotesAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadNotesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<Github>(EXTRA_STATE)
            if (list != null) {
                listFavorites = list
            }
        }

        // Action Bar Back
        val actionbar = supportActionBar
        actionbar?.title = "Favorite User"
    }

    private fun loadNotesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBarFavorite.visibility = View.VISIBLE
            // val favoriteHelper = FavoriteHelper.getInstance(applicationContext)
            // favoriteHelper.open()
            val deferredFavorites = async(Dispatchers.IO) {
                // val cursor = favoriteHelper.queryAll()
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            binding.progressBarFavorite.visibility = View.INVISIBLE
            val favorites = deferredFavorites.await()
            if (favorites.size > 0) {
                adapter.setListFavorite(favorites)
            } else {
                showSnackbarMessage("Tidak ada data saat ini")
            }
            // favoriteHelper.close()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, listFavorites)
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding.rvFavorite, message, Snackbar.LENGTH_SHORT).show()
    }
}