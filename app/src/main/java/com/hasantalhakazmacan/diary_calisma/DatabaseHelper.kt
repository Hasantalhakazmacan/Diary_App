package com.hasantalhakazmacan.diary_calisma

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "DiaryApp.db"
        private const val DATABASE_VERSION = 3

        // Users tablosu
        const val TABLE_USERS = "users"
        const val COLUMN_ID = "id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PASSWORD = "password"

        // Notes tablosu
        const val TABLE_NOTES = "notes"
        const val COL_NOTE_ID = "id"
        const val COL_NOTE_TITLE = "title"
        const val COL_NOTE_CONTENT = "content"
        const val COL_NOTE_DATE = "date"
        const val COL_NOTE_FAV = "isFavorite"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createUsersTableQuery = ("CREATE TABLE " + TABLE_USERS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERNAME + " TEXT, "
                + COLUMN_EMAIL + " TEXT UNIQUE, "
                + COLUMN_PASSWORD + " TEXT)")

        val createNotesTableQuery = ("CREATE TABLE " + TABLE_NOTES + " ("
                + COL_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NOTE_TITLE + " TEXT, "
                + COL_NOTE_CONTENT + " TEXT, "
                + COL_NOTE_DATE + " TEXT, "
                + COL_NOTE_FAV + " INTEGER DEFAULT 0)")

        db?.execSQL(createUsersTableQuery)
        db?.execSQL(createNotesTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES)
        db?.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS)
        onCreate(db)
    }
    fun isEmailRegistered(email: String): Boolean {
        val cursor = readableDatabase.rawQuery(
            "SELECT $COLUMN_ID FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ?",
            arrayOf(email)
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }
    fun insertUser(username: String, email: String, password: String): Long {
        if (isEmailRegistered(email)) return -1L
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_EMAIL, email)
            put(COLUMN_PASSWORD, password)
        }
        return writableDatabase.insert(TABLE_USERS, null, values)
    }
    fun checkLogin(email: String, password: String): Boolean {
        val cursor = readableDatabase.rawQuery(
            "SELECT $COLUMN_ID FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?",
            arrayOf(email, password)
        )
        val ok = cursor.count > 0
        cursor.close()
        return ok
    }
    fun insertNote(title: String, content: String, date: String): Long {
        val values = ContentValues().apply {
            put(COL_NOTE_TITLE, title)
            put(COL_NOTE_CONTENT, content)
            put(COL_NOTE_DATE, date)
            put(COL_NOTE_FAV, 0)
        }
        return writableDatabase.insert(TABLE_NOTES, null, values)
    }
    fun updateNote(id: Int, title: String, content: String, date: String): Int {
        val values = ContentValues().apply {
            put(COL_NOTE_TITLE, title)
            put(COL_NOTE_CONTENT, content)
            put(COL_NOTE_DATE, date)
        }
        return writableDatabase.update(
            TABLE_NOTES, values,
            "$COL_NOTE_ID = ?", arrayOf(id.toString())
        )
    }
    fun deleteNote(id: Int): Int {
        return writableDatabase.delete(
            TABLE_NOTES,
            "$COL_NOTE_ID = ?", arrayOf(id.toString())
        )
    }
    fun setFavorite(id: Int, isFav: Boolean): Int {
        val values = ContentValues().apply {
            put(COL_NOTE_FAV, if (isFav) 1 else 0)
        }
        return writableDatabase.update(
            TABLE_NOTES, values,
            "$COL_NOTE_ID = ?", arrayOf(id.toString())
        )
    }
    fun getNoteById(id: Int): Note? {
        val cursor = readableDatabase.rawQuery(
            "SELECT $COL_NOTE_ID, $COL_NOTE_TITLE, $COL_NOTE_CONTENT, " +
                    "$COL_NOTE_DATE, $COL_NOTE_FAV FROM $TABLE_NOTES " +
                    "WHERE $COL_NOTE_ID = ?",
            arrayOf(id.toString())
        )
        cursor.use {
            if (it.moveToFirst()) {
                return Note(
                    id = it.getInt(0),
                    title = it.getString(1) ?: "",
                    content = it.getString(2) ?: "",
                    date = it.getString(3) ?: "",
                    isFavorite = it.getInt(4) == 1
                )
            }
        }
        return null
    }
    fun getAllNotes(): List<Note> {
        val list = mutableListOf<Note>()
        val cursor = readableDatabase.rawQuery(
            "SELECT $COL_NOTE_ID, $COL_NOTE_TITLE, $COL_NOTE_CONTENT, " +
                    "$COL_NOTE_DATE, $COL_NOTE_FAV FROM $TABLE_NOTES " +
                    "ORDER BY $COL_NOTE_ID DESC",
            null
        )
        cursor.use {
            while (it.moveToNext()) {
                list.add(
                    Note(
                        id = it.getInt(0),
                        title = it.getString(1) ?: "",
                        content = it.getString(2) ?: "",
                        date = it.getString(3) ?: "",
                        isFavorite = it.getInt(4) == 1
                    )
                )
            }
        }
        return list
    }

    // Başlığa veya içeriğe göre arama
    fun searchNotes(query: String): List<Note> {
        val list = mutableListOf<Note>()
        val q = "%$query%"
        val cursor = readableDatabase.rawQuery(
            "SELECT $COL_NOTE_ID, $COL_NOTE_TITLE, $COL_NOTE_CONTENT, " +
                    "$COL_NOTE_DATE, $COL_NOTE_FAV FROM $TABLE_NOTES " +
                    "WHERE $COL_NOTE_TITLE LIKE ? OR $COL_NOTE_CONTENT LIKE ? " +
                    "ORDER BY $COL_NOTE_ID DESC",
            arrayOf(q, q)
        )
        cursor.use {
            while (it.moveToNext()) {
                list.add(
                    Note(
                        id = it.getInt(0),
                        title = it.getString(1) ?: "",
                        content = it.getString(2) ?: "",
                        date = it.getString(3) ?: "",
                        isFavorite = it.getInt(4) == 1
                    )
                )
            }
        }
        return list
    }
}