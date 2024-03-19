package com.example.lab3.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.widget.Toast

class DBManager(private val context: Context) {
    private val dbHelper = DBHelper(context)
    private lateinit var db: SQLiteDatabase

    fun openDbForWrite() {
        db = dbHelper.writableDatabase
    }

    fun insertToDb(language: String) {
        val values = ContentValues().apply {
            put(COLUMN_NAME, language)
        }
        db.insert(TABLE_NAME, null, values)
        Toast.makeText(context, "Data inserted successfully", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("Range")
    fun readDbData(): ArrayList<String> {
        val dataList = ArrayList<String>()
        db = dbHelper.readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val dataLanguage = it.getString(it.getColumnIndex(COLUMN_NAME))
                dataList.add(dataLanguage)
            }
        }

        cursor?.close()
        return dataList
    }

    fun deleteDbData() {
        val db = dbHelper.readableDatabase
        db.delete(TABLE_NAME,  null, null)
    }


    fun closeDb() {
        dbHelper.close()
    }

    companion object {
        const val COLUMN_NAME = "language"
        const val TABLE_NAME = "my_table"
    }
}