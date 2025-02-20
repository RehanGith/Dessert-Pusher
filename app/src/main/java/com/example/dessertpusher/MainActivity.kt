 package com.example.dessertpusher

import android.content.ActivityNotFoundException
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleObserver
import com.example.dessertpusher.databinding.ActivityMainBinding
import timber.log.Timber
import timber.log.Timber.DebugTree

const val REVENUE_VALUE = "revenue"
 const val DESSERT_SOLD = "dessertsSold"
 const val TIMER_VALUE = "time"
 class MainActivity : AppCompatActivity(), LifecycleObserver {
     private var revenue = 0
     private var dessertsSold = 0
     private lateinit var dessertTimer: DessertTimer

     // Contains all the views
     private lateinit var binding: ActivityMainBinding


     data class Dessert(val imageId: Int, val price: Int, val startProductionAmount: Int)

     // Create a list of all desserts, in order of when they start being produced
     private val allDesserts = listOf(
         Dessert(R.drawable.cupcake, 5, 0),
         Dessert(R.drawable.donut, 10, 5),
         Dessert(R.drawable.eclair, 15, 20),
         Dessert(R.drawable.froyo, 30, 50),
         Dessert(R.drawable.gingerbread, 50, 100),
         Dessert(R.drawable.honeycomb, 100, 200),
         Dessert(R.drawable.icecreamsandwich, 500, 500),
         Dessert(R.drawable.jellybean, 1000, 1000),
         Dessert(R.drawable.kitkat, 2000, 2000),
         Dessert(R.drawable.lollipop, 3000, 4000),
         Dessert(R.drawable.marshmallow, 4000, 8000),
         Dessert(R.drawable.nougat, 5000, 16000),
         Dessert(R.drawable.oreo, 6000, 20000)
     )
     private var currentDessert = allDesserts[0]
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Use Data Binding to get reference to the views
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.dessertButton.setOnClickListener {
            onDessertClicked()
        }
        Timber.i("onCreate Called")
        dessertTimer = DessertTimer(this.lifecycle)

        binding.dessertButton.setImageResource(currentDessert.imageId)
        if(savedInstanceState != null) {
            revenue = savedInstanceState.getInt(REVENUE_VALUE, 0)
            dessertsSold = savedInstanceState.getInt(DESSERT_SOLD, 0)
            dessertTimer.secondsCount = savedInstanceState.getInt(TIMER_VALUE, 0)
            showCurrentDessert()
        }
        // Set the TextViews to the right values
        binding.revenue = revenue
        binding.amountSold = dessertsSold

    }
     /**
      * Updates the score when the dessert is clicked. Possibly shows a new dessert.
      */
     private fun onDessertClicked() {

         // Update the score
         revenue += currentDessert.price
         dessertsSold++

         binding.revenue = revenue
         binding.amountSold = dessertsSold

         // Show the next dessert
         showCurrentDessert()
     }

     /**
      * Determine which dessert to show.
      */
     private fun showCurrentDessert() {
         var newDessert = allDesserts[0]
         for (dessert in allDesserts) {
             if (dessertsSold >= dessert.startProductionAmount) {
                 newDessert = dessert
             }
             // The list of desserts is sorted by startProductionAmount. As you sell more desserts,
             // you'll start producing more expensive desserts as determined by startProductionAmount
             // We know to break as soon as we see a dessert who's "startProductionAmount" is greater
             // than the amount sold.
             else break
         }

         // If the new dessert is actually different than the current dessert, update the image
         if (newDessert != currentDessert) {
             currentDessert = newDessert
             binding.dessertButton.setImageResource(newDessert.imageId)
         }
     }

     /**
      * Menu methods
      */
     private fun onShare() {
         val shareIntent = ShareCompat.IntentBuilder.from(this)
             .setText(getString(R.string.share_text, dessertsSold, revenue))
             .setType("text/plain")
             .intent
         try {
             startActivity(shareIntent)
         } catch (ex: ActivityNotFoundException) {
             Toast.makeText(this, getString(R.string.sharing_not_available),
                 Toast.LENGTH_LONG).show()
         }
     }

     override fun onCreateOptionsMenu(menu: Menu): Boolean {
         menuInflater.inflate(R.menu.main_menu, menu)
         return super.onCreateOptionsMenu(menu)
     }

     override fun onOptionsItemSelected(item: MenuItem): Boolean {
         when (item.itemId) {
             R.id.shareMenuButton -> onShare()
         }
         return super.onOptionsItemSelected(item)
     }

     override fun onStart() {
         super.onStart()
         Timber.i("onStart Called")
     }

     override fun onResume() {
         super.onResume()
         Timber.i("onResume Called")
     }

     override fun onPause() {
         super.onPause()
         Timber.i("onPause Called")
     }

     override fun onStop() {
         super.onStop()
         Timber.i("onStop Called")
     }

     override fun onRestart() {
         super.onRestart()
         Timber.i("onRestart Called")
     }

     override fun onDestroy() {
         super.onDestroy()
         Timber.i("onDestroy Called")
     }

     override fun onSaveInstanceState(outState: Bundle) {
         super.onSaveInstanceState(outState)
         outState.putInt(REVENUE_VALUE, revenue)
         outState.putInt(DESSERT_SOLD, dessertsSold)
         outState.putInt(TIMER_VALUE, dessertTimer.secondsCount)
         Timber.i("onSaveInstanceState called")
     }

     override fun onRestoreInstanceState(savedInstanceState: Bundle) {
         super.onRestoreInstanceState(savedInstanceState)
         Timber.i("onRestoreInstanceState called")
     }
 }
