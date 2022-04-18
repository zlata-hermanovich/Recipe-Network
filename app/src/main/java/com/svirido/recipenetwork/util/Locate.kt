package com.svirido.recipenetwork.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AlertDialog
import com.svirido.recipenetwork.ui.mainscreen.MainActivity
import java.util.*

fun MainActivity.showChangeLanguage() {
    val listLang = arrayOf("Русский", "English")
    val uBuilder = AlertDialog.Builder(this)
    uBuilder.setTitle("Choose language")
    uBuilder.setSingleChoiceItems(listLang, -1) { dialog, which ->
        if (which == 0) {
            setLocate("ru")
            recreate()
        } else if (which == 1) {
            setLocate("en")
            recreate()
        }
        dialog.dismiss()
    }
    val mDialog = uBuilder.create().show()
}

@SuppressLint("CommitPrefEdits")
fun MainActivity.setLocate(lang: String) {
    val locale = Locale(lang)
    Locale.setDefault(locale)

    val config = Configuration()
    config.locale = locale
    baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

    val editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
    editor.putString("My_Lang", lang)
    editor.apply()
}

fun MainActivity.loadLocate() {
    val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
    val language = sharedPreferences.getString("My_Lang", "")
    if (language != null) {
        setLocate(language)
    } else {
        setLocate("en")
    }
}
