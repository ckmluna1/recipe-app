package com.ckmluna.recipeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class AddRecipeActivity : AppCompatActivity() {
    private var databaseHelper: DatabaseHelper? = null
    private var recipeTypesHelper: RecipeTypesHelper? = null

    private var editRecipeName: EditText? = null
    private var spinnerType: Spinner? = null
    private var editPictureUrl: EditText? = null
    private var editRecipeIngredients: EditText? = null
    private var editRecipeSteps: EditText? = null
    private var buttonSaveRecipe: Button? = null
    private var buttonCancel: Button? = null

    private var recipeTypes: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        databaseHelper = DatabaseHelper(this)
        recipeTypesHelper = RecipeTypesHelper(this)

        editRecipeName = findViewById(R.id.editRecipeName) as EditText
        spinnerType = findViewById(R.id.spinnerType) as Spinner
        editPictureUrl = findViewById(R.id.editPictureUrl) as EditText
        editRecipeIngredients = findViewById(R.id.editRecipeIngredients) as EditText
        editRecipeSteps = findViewById(R.id.editRecipeSteps) as EditText
        buttonSaveRecipe = findViewById(R.id.buttonSaveRecipe) as Button
        buttonCancel = findViewById(R.id.buttonCancel) as Button

        recipeTypes = this.recipeTypesHelper!!.recipeTypeNames
        val adapter = ArrayAdapter(this@AddRecipeActivity, android.R.layout.simple_spinner_item, this.recipeTypes)
        spinnerType!!.setAdapter(adapter)

        buttonSaveRecipe!!.setOnClickListener {
            val typeId = recipeTypesHelper!!.getRecipeTypeId(spinnerType!!.selectedItemPosition)

            databaseHelper!!.addRecipe(
                editRecipeName!!.text.toString(),
                typeId,
                editPictureUrl!!.text.toString(),
                editRecipeIngredients!!.text.toString(),
                editRecipeSteps!!.text.toString()
            )
            finish()
        }

        buttonCancel!!.setOnClickListener {
            finish()
        }
    }
}
