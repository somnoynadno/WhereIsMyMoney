package com.example.whereismymoney

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import androidx.room.*
import com.example.whereismymoney.models.AppDatabase
import kotlinx.android.synthetic.main.app_bar_main.*
import android.app.Activity
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whereismymoney.helpers.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_today, R.id.nav_my_debts, R.id.nav_my_debtors
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()

        addNewDebtButton.setOnClickListener {
            val intent = Intent(this, NewDebtActivity::class.java)
            startActivityForResult(intent, 1)
        }

        getRates()

//        val items = listOf(
//            RecyclerItem("Сергей", "12.12.2012", "1000"),
//            RecyclerItem("Владимир", "11.11.2011", "20000"),
//            RecyclerItem("Слава", "10.10.2010", "500"),
//            RecyclerItem("Игорь", "01.01.2001", "4000")
//        )
//
//        val myAdapter = RecyclerViewAdapter(items, object : RecyclerViewAdapter.Callback {
//            override fun onItemClicked(item: RecyclerItem) {
//                //TODO handle click
//            }
//        })
//
//        val debtsRecyclerView = findViewById<RecyclerView>(R.id.debtsRecyclerView)
//        debtsRecyclerView.adapter = myAdapter
//
//        linearLayoutManager = LinearLayoutManager(this)
//        debtsRecyclerView.setLayoutManager(linearLayoutManager)
    }

    private fun getRates() {
        val retrofit = NetworkClient.retrofitClient
        val r = retrofit!!.create<APIInterface>(APIInterface::class.java)

        val call = r.getRates()

        call.enqueue(object : Callback<APIResponse> {
            override fun onFailure(call: Call<APIResponse>?, t: Throwable?) {
                // TODO: alert error
                t?.printStackTrace()
            }

            override fun onResponse(call: Call<APIResponse>?, response: Response<APIResponse>?) {
                val res = response!!.body()
                var rates = res!!.rates

                // TODO: insert in drawer
                println(rates!!.RUB)
                println(rates!!.USD)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data!!.getStringExtra("result")
                val toast = Toast.makeText(
                    applicationContext,
                    "Долг добавлен!", Toast.LENGTH_SHORT
                )
                toast.show()
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                val toast = Toast.makeText(
                    applicationContext,
                    "Отмена", Toast.LENGTH_SHORT
                )
                toast.show()
            }
        }
    }
}
