package com.ckmluna.recipeapp

import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import android.view.View
import com.bumptech.glide.Glide
import java.lang.Exception
import java.net.URL
import java.net.URLConnection

class ViewRecipeActivity : AppCompatActivity() {
    private var databaseHelper: DatabaseHelper? = null
    private var recipeTypesHelper: RecipeTypesHelper? = null

    private var recipeId: Int = 0
    private var recipeHashMap: HashMap<String, String>? = null

    private var recipePicture: ImageView? = null
    private var recipeName: TextView? = null
    private var recipeType: TextView? = null
    private var recipeIngredients: TextView? = null
    private var recipeSteps: TextView? = null
    private var editRecipeFab: View? = null
    private var deleteRecipeFab: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_recipe)

        databaseHelper = DatabaseHelper(this)
        recipeTypesHelper = RecipeTypesHelper(this)

        recipeId = intent.getStringExtra("recipeId").toInt()

        recipePicture = findViewById(R.id.recipePicture) as ImageView
        recipeName = findViewById(R.id.recipeName) as TextView
        recipeType = findViewById(R.id.recipeType) as TextView
        recipeIngredients = findViewById(R.id.recipeIngredients) as TextView
        recipeSteps = findViewById(R.id.recipeSteps) as TextView

        populateView()

        editRecipeFab = findViewById(R.id.editRecipeFab) as View
        deleteRecipeFab = findViewById(R.id.deleteRecipeFab) as View

        editRecipeFab!!.setOnClickListener { view ->
            val intent = Intent(this, EditRecipeActivity::class.java)
            intent.putExtra("recipeId", recipeId.toString())
            startActivity(intent)
        }

        deleteRecipeFab!!.setOnClickListener { view ->
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Warning")
            builder.setMessage("Are you sure you want to delete this recipe?")
            builder.setPositiveButton("YES") {dialog, which ->
                if(databaseHelper!!.deleteRecipe(recipeId)) {
                    this.finish()
                }
            }
            builder.setNegativeButton("NO") {dialog, which ->
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    override fun onResume() {
        super.onResume()

        populateView()
    }

    fun populateView() {
        recipeHashMap = databaseHelper!!.getRecipeById(recipeId)

        Glide.with(this).load(recipeHashMap!!.get("pictureUrl")).into(recipePicture);


        recipeName!!.setText(recipeHashMap!!.get("name"))

        val typeId = recipeHashMap!!.get("type")!!.toInt() - 1
        val type = recipeTypesHelper!!.getRecipeTypeName(typeId!!)
        recipeType!!.setText(type)

        recipeIngredients!!.setText(recipeHashMap!!.get("ingredients"))
        recipeSteps!!.setText(recipeHashMap!!.get("steps"))
    }
}
