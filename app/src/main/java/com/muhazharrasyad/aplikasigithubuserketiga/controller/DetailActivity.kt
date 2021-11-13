package com.muhazharrasyad.aplikasigithubuserketiga.controller

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.muhazharrasyad.aplikasigithubuserketiga.R
import com.muhazharrasyad.aplikasigithubuserketiga.adapter.SectionsPagerAdapter
import com.muhazharrasyad.aplikasigithubuserketiga.db.DatabaseContract
import com.muhazharrasyad.aplikasigithubuserketiga.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.muhazharrasyad.aplikasigithubuserketiga.db.FavoriteHelper
import com.muhazharrasyad.aplikasigithubuserketiga.helper.MappingHelper
import com.muhazharrasyad.aplikasigithubuserketiga.model.Github
import kotlinx.coroutines.*

class DetailActivity : AppCompatActivity(), View.OnClickListener {
    private var isFavorite = false
    private var favorite: Github? = null
    private lateinit var favoriteHelper: FavoriteHelper

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_follower,
            R.string.tab_following
        )
        const val EXTRA_GITHUB = "extra_github"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val github = intent.getParcelableExtra<Github>(EXTRA_GITHUB) as Github

        val tvDataReceivedName: TextView = findViewById(R.id.tv_data_received_name)
        val tvDataReceivedUsername: TextView = findViewById(R.id.tv_data_received_username)
        val ivDataReceivedAvatar: ImageView = findViewById(R.id.iv_data_received_avatar)

        tvDataReceivedName.text = github.name
        tvDataReceivedUsername.text = github.username
        Glide.with(this)
            .load(github.avatar)
            .into(ivDataReceivedAvatar)

        // Action Bar Back
        val actionbar = supportActionBar
        actionbar?.title = "Github User"
        actionbar?.setDisplayHomeAsUpEnabled(true)

        // Share
        val btnShare: Button = findViewById(R.id.btn_share)
        btnShare.setOnClickListener(this)

        // Follower dan Following
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        // Favorite
        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()

        val fabFavorite: FloatingActionButton = findViewById(R.id.fab_favorite)
        fabFavorite.setOnClickListener(this)

        isFavorite = isFavorite(github.username.toString())

        if (isFavorite) {
            fabFavorite.setImageResource(R.drawable.ic_favorite)
        } else {
            fabFavorite.setImageResource(R.drawable.ic_no_favorite)
        }
    }

    private fun isFavorite(username: String): Boolean {
        return runBlocking {
            val deferredFavorites = async(Dispatchers.IO) {
                val cursor = favoriteHelper.queryByUsername(username)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            deferredFavorites.await().isNotEmpty()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_share -> {
                val github = intent.getParcelableExtra<Github>(EXTRA_GITHUB) as Github

                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "Username Github: ${github.username}")
                    type = "text/plain"
                }

                val shareIntent =
                    Intent.createChooser(sendIntent, intent.getStringExtra(github.username))
                startActivity(shareIntent)
            }
            R.id.fab_favorite -> {
                val github = intent.getParcelableExtra<Github>(EXTRA_GITHUB) as Github
                val fabFavorite: FloatingActionButton = findViewById(R.id.fab_favorite)

                if (isFavorite) {
                    github.favorite = false
                    fabFavorite.setImageResource(R.drawable.ic_no_favorite)

                    favoriteHelper.delete(github.username.toString())
                    Toast.makeText(this, "Hapus dari Favorite", Toast.LENGTH_SHORT).show()
                    val i = Intent(this, FavoriteActivity::class.java)
                    startActivity(i)
                } else {
                    github.favorite = true
                    fabFavorite.setImageResource(R.drawable.ic_favorite)

                    val tvUsername: TextView = findViewById(R.id.tv_data_received_username)
                    val tvName: TextView = findViewById(R.id.tv_data_received_name)

                    val inUsername = tvUsername.text.toString()
                    val inName = tvName.text.toString()
                    val inAvatar = github.avatar.toString()
                    val inFavorite = true

                    favorite?.username = inUsername
                    favorite?.name = inName
                    favorite?.avatar = inAvatar
                    favorite?.favorite = inFavorite

                    val values = ContentValues()
                    values.put(DatabaseContract.FavoriteColumns.USERNAME, inUsername)
                    values.put(DatabaseContract.FavoriteColumns.NAME, inName)
                    values.put(DatabaseContract.FavoriteColumns.AVATAR, inAvatar)
                    values.put(DatabaseContract.FavoriteColumns.FAVORITE, inFavorite)

                    // favoriteHelper.insert(values)
                    contentResolver.insert(CONTENT_URI, values)
                    Toast.makeText(this, "Tambah ke Favorite", Toast.LENGTH_SHORT).show()
                    val i = Intent(this, FavoriteActivity::class.java)
                    startActivity(i)
                }
            }
        }
    }
}