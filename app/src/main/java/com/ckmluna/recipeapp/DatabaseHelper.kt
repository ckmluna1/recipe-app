package com.ckmluna.recipeapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.util.ArrayList

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    val allRecipes: ArrayList<HashMap<String, String>>
        get() {
            val selectQuery = "SELECT  * FROM $TABLE_RECIPES"
            val db = this.readableDatabase
            val c = db.rawQuery(selectQuery, null)
            return parseQuery(c)
        }

    fun getRecipesByType(recipeTypeId: Int): ArrayList<HashMap<String, String>> {
        val selectQuery = "SELECT  * FROM $TABLE_RECIPES WHERE $KEY_TYPE == $recipeTypeId"
        val db = this.readableDatabase
        val c = db.rawQuery(selectQuery, null)
        return parseQuery(c)
    }

    fun parseQuery(c: Cursor):ArrayList<HashMap<String, String>> {
        var recipesArrayList: ArrayList<HashMap<String, String>> = ArrayList()
        if (c.moveToFirst()) {
            do {
                val newRecipe = HashMap<String, String>()
                newRecipe.put("id", c.getInt(c.getColumnIndex(KEY_ID)).toString())
                newRecipe.put("name", c.getString(c.getColumnIndex(KEY_NAME)))
                newRecipe.put("type", c.getInt(c.getColumnIndex(KEY_TYPE)).toString())
                newRecipe.put("pictureUrl", c.getString(c.getColumnIndex(KEY_PICTURE)))
                newRecipe.put("ingredients", c.getString(c.getColumnIndex(KEY_INGREDIENTS)))
                newRecipe.put("steps", c.getString(c.getColumnIndex(KEY_STEPS)))
                recipesArrayList.add(newRecipe)
            } while (c.moveToNext())
            Log.d("array", recipesArrayList.toString())
        }

        return recipesArrayList
    }

    fun getRecipeById(id: Int): HashMap<String, String> {
        val selectQuery = "SELECT  * FROM $TABLE_RECIPES WHERE $KEY_ID == $id"
        val db = this.readableDatabase
        val c = db.rawQuery(selectQuery, null)
        val newRecipe = HashMap<String, String>()
        if (c.moveToFirst()) {
            newRecipe.put("id", c.getInt(c.getColumnIndex(KEY_ID)).toString())
            newRecipe.put("name", c.getString(c.getColumnIndex(KEY_NAME)))
            newRecipe.put("type", c.getInt(c.getColumnIndex(KEY_TYPE)).toString())
            newRecipe.put("pictureUrl", c.getString(c.getColumnIndex(KEY_PICTURE)))
            newRecipe.put("ingredients", c.getString(c.getColumnIndex(KEY_INGREDIENTS)))
            newRecipe.put("steps", c.getString(c.getColumnIndex(KEY_STEPS)))
            return newRecipe
        }
        return newRecipe
    }

    init {
        Log.d("table", CREATE_TABLE_RECIPES)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_RECIPES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS '$TABLE_RECIPES'")
        onCreate(db)
    }

    fun addRecipe(name: String,
                  type: Int,
                  pictureUrl: String,
                  ingredients: String,
                  steps: String
                  ): Long {
        val db = this.writableDatabase
        // Creating content values
        val values = ContentValues()
        values.put(KEY_NAME, name)
        values.put(KEY_TYPE, type)
        values.put(KEY_PICTURE, pictureUrl)
        values.put(KEY_INGREDIENTS, ingredients)
        values.put(KEY_STEPS, steps)

        return db.insert(TABLE_RECIPES, null, values)
    }

    fun editRecipe(
            id: Int,
            name: String,
            type: Int,
            pictureUrl: String,
            ingredients: String,
            steps: String
            ): Int {
        val db = this.writableDatabase
        // Creating content values
        val values = ContentValues()
        values.put(KEY_NAME, name)
        values.put(KEY_TYPE, type)
        values.put(KEY_PICTURE, pictureUrl)
        values.put(KEY_INGREDIENTS, ingredients)
        values.put(KEY_STEPS, steps)

        return db.update(TABLE_RECIPES, values,KEY_ID + "=?", arrayOf(id.toString()))
    }

    fun deleteRecipe(id: Int): Boolean {
        val db = this.writableDatabase
        return db.delete(TABLE_RECIPES, KEY_ID + "=?", arrayOf(id.toString())) > 0
    }

    companion object {

        var DATABASE_NAME = "app_database"
        private val DATABASE_VERSION = 1
        private val TABLE_RECIPES = "recipes"
        private val KEY_ID = "id"
        private val KEY_NAME = "name"
        private val KEY_TYPE = "type"
        private val KEY_PICTURE = "picture"
        private val KEY_INGREDIENTS = "ingredients"
        private val KEY_STEPS = "steps"

        private val CREATE_TABLE_RECIPES = ("CREATE TABLE "
                + TABLE_RECIPES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_TYPE + " INTEGER,"
                + KEY_PICTURE + " TEXT,"
                + KEY_INGREDIENTS + " TEXT,"
                + KEY_STEPS + " TEXT"
                + ");")
    }
};