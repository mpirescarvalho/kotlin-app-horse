package com.example.myapplication.util

import android.content.Context
import com.example.myapplication.R
import com.example.myapplication.network.dto.SessionResponse
import com.google.gson.Gson

object Memoria {
    var session: SessionResponse? = null

    fun persistSession(context: Context) {
        val sharedPref = context.getSharedPreferences(
            context.getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )
        with (sharedPref.edit()) {
            val sessionJson = Gson().toJson(session)
            putString("SESSION", sessionJson)
            commit()
        }
    }

    fun loadSession(context: Context) {
        val sharedPref = context.getSharedPreferences(
            context.getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )
        val sessionJson = sharedPref.getString("SESSION", "")
        if (!sessionJson.isNullOrEmpty()) {
            session = Gson().fromJson(sessionJson, SessionResponse::class.java)
        }
    }

    fun deleteSession(context: Context) {
        val sharedPref = context.getSharedPreferences(
            context.getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )
        with (sharedPref.edit()) {
            remove("SESSION")
            commit()
            session = null
        }
    }
}