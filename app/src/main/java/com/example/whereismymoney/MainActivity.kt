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
import android.app.Activity
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.whereismymoney.helpers.CalendarHelper
import com.example.whereismymoney.helpers.network.APIInterface
import com.example.whereismymoney.helpers.network.APIResponse
import com.example.whereismymoney.helpers.network.NetworkClient
import com.example.whereismymoney.helpers.network.Rates
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_today, R.id.nav_my_debts, R.id.nav_my_debtors
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // API call
        getRates()
    }

    private fun getRates() {
        val retrofit = NetworkClient.retrofitClient
        val r = retrofit?.create<APIInterface>(APIInterface::class.java)

        val call = r?.getRates()

        call?.enqueue(object : Callback<APIResponse> {
            override fun onFailure(call: Call<APIResponse>?, t: Throwable?) {
                alertNetworkErrorDialog()
                currencyText.text = getString(R.string.network_error)
            }

            override fun onResponse(call: Call<APIResponse>?, response: Response<APIResponse>?) {
                val res = response?.body()
                val rates = res?.rates
                val date = CalendarHelper().getCurrentDateAsFormatedString()

                // insert in drawer
                currencyText.text = getString(R.string.currency_text, date)
                EUR_USD.text = getString(R.string.EUR_equal_USD, rates?.USD)
                EUR_RUB.text = getString(R.string.EUR_equal_RUB, rates?.RUB)

                // ну я так подумал, что можно сразу и сумму долгов рассчитать
                setAmountInDrawer(rates)
            }
        })
    }

    // TODO: add enum here
    private fun setAmountInDrawer(rates: Rates?) {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "debts"
        ).allowMainThreadQueries().build()

        val rub = rates?.RUB
        val usd = rates?.USD

        val items = db.debtDao().getAll()
        var myDebtsSum: Long = 0
        var debtsSum: Long = 0

        val iterate = items.listIterator()
        while (iterate.hasNext()) {
            val value = iterate.next()
            // какая-то сложная математика
            if (value.isMyDebt) {
                when (value.currency) {
                    "RUB" -> myDebtsSum += value.amount
                    "USD" -> myDebtsSum += (value.amount * rub!!.toDouble() / usd!!.toDouble()).toLong()
                    "EUR" -> myDebtsSum += (value.amount * rub!!.toDouble()).toLong()
                }
            }
            if (!value.isMyDebt) {
                when (value.currency) {
                    "RUB" -> debtsSum += value.amount
                    "USD" -> debtsSum += (value.amount * rub!!.toDouble() / usd!!.toDouble()).toLong()
                    "EUR" -> debtsSum += (value.amount * rub!!.toDouble()).toLong()
                }
            }
        }

        myDebtsSumText.text = getString(R.string.my_debts_sum, myDebtsSum.toString())
        debtsSumText.text = getString(R.string.my_debtors_sum, debtsSum.toString())
    }

    private fun alertNetworkErrorDialog() {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setMessage(getString(R.string.network_error))

        builder.setPositiveButton(getString(R.string.agree_text)) { _, _ -> }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
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
                val toast = Toast.makeText(
                    applicationContext,
                    getString(R.string.debt_added), Toast.LENGTH_SHORT
                )
                toast.show()
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                val toast = Toast.makeText(
                    applicationContext,
                    getString(R.string.cancel), Toast.LENGTH_SHORT
                )
                toast.show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getRates()

        nav_view.refreshDrawableState()
    }
}
