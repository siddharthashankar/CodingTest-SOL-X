package com.sid.assignment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.sid.assignment.database.CatFact
import com.sid.assignment.database.CatFactDatabase
import com.sid.assignment.utils.Constants.Companion.image_url
import com.sid.assignment.utils.NetworkResult
import com.sid.assignment.utils.Utils
import com.sid.assignment.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import kotlin.random.Random

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel>()
    var image: Bitmap? = null
    private val catFactDatabase by lazy { CatFactDatabase.getDatabase(this).catFactDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // calling the action bar
        var actionBar = getSupportActionBar()
        if (actionBar != null) {
            // Customize the button
            actionBar.setHomeAsUpIndicator(R.drawable.ic_refresh);
            // showing the button in action bar
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (Utils.hasInternetConnection(this)) {
            loadCatImage()
        } else {
            Toast.makeText(this, "Internet is not available", Toast.LENGTH_SHORT).show()
        }

        mainViewModel.fetchCatFactsResponse()
    }

   /*
   * New Cat's img and facts will load on click of refresh button.
   * */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                learn_new_fact.visibility = View.GONE
                card_view.visibility = View.VISIBLE
                if (Utils.hasInternetConnection(this)) {
                    loadCatImage()
                    fetchCatFactData()
                    Toast.makeText(this, "Got new facts from web", Toast.LENGTH_SHORT).show()
                } else {
                    getCatFactsFromDataBase()
                    Toast.makeText(this, "Got facts from Database", Toast.LENGTH_SHORT).show()
                }
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    fun loadCatImage() {
        // Declaring executor to parse the URL
        val executor = Executors.newSingleThreadExecutor()
        // Once the executor parses the URL and receives the image, handler will load it in the ImageView
        val handler = Handler(Looper.getMainLooper())

        // Only for Background process (can take time depending on the Internet speed)
        executor.execute {
            // Tries to get the image and post it in the ImageView with the help of Handler
            try {
                val `in` = java.net.URL(image_url).openStream()
                image = BitmapFactory.decodeStream(`in`)
                // changes in UI
                handler.post {
                    imgCat.setImageBitmap(image)
                }
            }
            // If the URL does not point to image or any other kind of failure
            catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun fetchCatFactData() {
        mainViewModel.catFactResponse.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    val randomIndex = response.data?.let { Random.nextInt(it.size) }
                    val randomElement = randomIndex?.let { response.data?.get(it) }
                    catFacts.text = randomElement?.text
                    // Add the new catfact
                    val newcatFact = CatFact(randomElement?.text, image)
                    lifecycleScope.launch {
                        catFactDatabase.addCatFact(newcatFact)
                    }
                }
                is NetworkResult.Error -> {
                    Toast.makeText(
                        this,
                        response.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                }
            }
        }
    }

    /*
    * Get the cat fact data from database
    * */
    private fun getCatFactsFromDataBase() {
        lifecycleScope.launch {
            catFactDatabase.getCatFact().collect { value ->
                imgCat.setImageBitmap(value.catImg)
                catFacts.text = value.catFactText
            }
        }
    }
}
