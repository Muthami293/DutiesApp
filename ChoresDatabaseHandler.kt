package com.example.dutiesapp.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast

class ChoresDatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME,
    null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        var CREATE_CHORE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_CHORE_NAME + " TEXT," + KEY_CHORE_ASSIGNEDBY + " TEXT," + KEY_CHORE_ASSIGNEDTO + " TEXT," +
                KEY_CHORE_ASSINGED_TIME + " LONG" + ")"
        db!!.execSQL(CREATE_CHORE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        //CREATE NEW TABLE
        onCreate(db)
    }

    fun createChore(chore: Chore){
        var db: SQLiteDatabase = writableDatabase
        var values: ContentValues = ContentValues()
        values.put(KEY_CHORE_NAME, chore.choreName)
        values.put(KEY_CHORE_ASSIGNEDBY, chore.assignedBy)
        values.put(KEY_CHORE_ASSIGNEDTO, chore.assignedTo)
//        values.put(KEY_CHORE_ASSINGED_TIME, chore.timeAssigned)
        values.put(KEY_CHORE_ASSINGED_TIME, System.currentTimeMillis())

       db.insert(TABLE_NAME, null, values)

       Log.d("Data inserted", "Data insert success")

    }
    fun readChore(id: Int) : Chore{
        var db: SQLiteDatabase = writableDatabase

        var cursor : Cursor = db.query(TABLE_NAME, arrayOf(KEY_ID, KEY_CHORE_NAME, KEY_CHORE_ASSIGNEDBY,
            KEY_CHORE_ASSIGNEDTO, KEY_CHORE_ASSINGED_TIME), KEY_ID + "+?", arrayOf(id.toString()),
            null, null, null, null)
        if (cursor != null){
            cursor.moveToFirst()
        }
        var chore = Chore()
        chore.id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
        chore.choreName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_CHORE_NAME))
        chore.assignedTo = cursor.getString(cursor.getColumnIndexOrThrow(KEY_CHORE_ASSIGNEDTO))
        chore.assignedBy = cursor.getString(cursor.getColumnIndexOrThrow(KEY_CHORE_ASSIGNEDBY))
        chore.timeAssigned = cursor.getLong(cursor.getColumnIndexOrThrow(KEY_CHORE_ASSINGED_TIME))

        return chore
    }
    fun readReadChores() : ArrayList<Chore>{
        var db: SQLiteDatabase = readableDatabase
        var list:ArrayList<Chore> = ArrayList()

        //select all chores from tables

        var selectAll = "SELECT * FROM " + TABLE_NAME
        var cursor: Cursor = db.rawQuery(selectAll, null)
        //loop through chores
        if (cursor.moveToFirst()){
            do {
                var chore = Chore()
                chore.id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                chore.choreName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_CHORE_NAME))
                chore.assignedTo = cursor.getString(cursor.getColumnIndexOrThrow(KEY_CHORE_ASSIGNEDTO))
                chore.assignedBy = cursor.getString(cursor.getColumnIndexOrThrow(KEY_CHORE_ASSIGNEDBY))
                chore.timeAssigned = cursor.getLong(cursor.getColumnIndexOrThrow(KEY_CHORE_ASSINGED_TIME))
                list.add(chore)
            }while (cursor.moveToNext())
        }
        return list
    }

    fun updateChore(chore: Chore) :  Int{
        var db: SQLiteDatabase = writableDatabase
        var values: ContentValues = ContentValues()
        values.put(KEY_CHORE_NAME, chore.choreName)
        values.put(KEY_CHORE_ASSIGNEDBY, chore.assignedBy)
        values.put(KEY_CHORE_ASSIGNEDTO, chore.assignedTo)
//        values.put(KEY_CHORE_ASSINGED_TIME, chore.timeAssigned)
        values.put(KEY_CHORE_ASSINGED_TIME, System.currentTimeMillis())

        return db.update(TABLE_NAME, values, KEY_ID + "=?", arrayOf(chore.id.toString()))
    }

    fun deleteChore(id : Int){
        var db: SQLiteDatabase = writableDatabase
        db.delete(TABLE_NAME, KEY_ID + "=?", arrayOf(id.toString()))

        db.close()
    }

    fun getChoreCount() : Int{
        var db: SQLiteDatabase = readableDatabase

        var countQuery = "SELECT * FROM " + TABLE_NAME
        var cursor : Cursor = db.rawQuery(countQuery, null)

        return  cursor.count
    }


}
