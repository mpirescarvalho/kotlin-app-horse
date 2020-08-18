package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import com.brouding.simpledialog.SimpleDialog
import com.brouding.simpledialog.builder.Custom
import com.brouding.simpledialog.extra.BtnAction
import com.example.myapplication.ui.login.LoginFragment
import com.example.myapplication.util.Memoria
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Memoria.loadSession(this)

        if (!Memoria.session?.token.isNullOrEmpty()) {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            ActivityCompat.finishAffinity(this)
        }

        setContentView(R.layout.activity_login)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, LoginFragment.newInstance())
                .commitNow()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        menu?.findItem(R.id.action_logout)?.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
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