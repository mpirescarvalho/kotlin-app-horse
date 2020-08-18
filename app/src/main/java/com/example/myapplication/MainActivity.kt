package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.brouding.simpledialog.SimpleDialog
import com.brouding.simpledialog.builder.Custom
import com.brouding.simpledialog.extra.BtnAction
import com.example.myapplication.models.Marca
import com.example.myapplication.network.Service
import com.example.myapplication.util.Memoria
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_marcas, R.id.navigation_clients, R.id.navigation_products))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.label == "Clientes") {
                supportActionBar?.elevation = 0F
            } else {
                supportActionBar?.elevation = 8F
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                Memoria.deleteSession(this)
                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                ActivityCompat.finishAffinity(this)
                true
            }
            R.id.action_server -> {
                val view = LayoutInflater.from(this).inflate(R.layout.simple_input, null)
                val input = view.findViewById<TextInputEditText>(R.id.input)
                input.hint = "Servidor"

                input.setText(Memoria.API_URL)

                SimpleDialog(Custom(this)
                    .applyGeneral {
                        setTitle("Servidor")
                        setBtnConfirmText(context.getString(R.string.save))
                        setBtnCancelText(context.getString(R.string.cancel))
                    }
                    .onBtnAction {
                        if (it == BtnAction.CONFIRM) {
                            Memoria.API_URL = input.text.toString()
                        }
                    }
                    .setCustomView(view)
                ).show()
                true
            }
            else -> false
        }
    }
}