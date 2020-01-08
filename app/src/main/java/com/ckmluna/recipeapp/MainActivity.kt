package com.ckmluna.recipeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.*
import org.w3c.dom.Element
import org.w3c.dom.Node
import javax.xml.parsers.DocumentBuilderFactory

class MainActivity : AppCompatActivity() {
    private var databaseHelper: DatabaseHelper? = null
    private var recipeTypesHelper: RecipeTypesHelper? = null

    private var recipeTypesSpinner: Spinner? = null
    private var recipeListView: ListView? = null
    private var addRecipeFab: View? = null

    private var recipeTypes: ArrayList<String> = ArrayList()
    private var recipes: ArrayList<HashMap<String, String>> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHelper = DatabaseHelper(this)
        recipeTypesHelper = RecipeTypesHelper(this)

        recipeTypesSpinner = findViewById<Spinner>(R.id.recipeTypesSpinner);
        recipeListView = findViewById<ListView>(R.id.recipeListView)
        addRecipeFab = findViewById(R.id.addRecipeFab);

        try {
            recipeTypes = this.recipeTypesHelper!!.recipeTypeNames
            val adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, this.recipeTypes)
            recipeTypesSpinner!!.setAdapter(adapter)

            populateList()

            addRecipeFab!!.setOnClickListener { view ->
                val intent = Intent(this, AddRecipeActivity::class.java)
                startActivity(intent)
            }

            recipeTypesSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val recipeTypeId = recipeTypesHelper!!.getRecipeTypeId(position)
                    recipes = databaseHelper!!.getRecipesByType(recipeTypeId)
                    val listAdapter = SimpleAdapter(this@MainActivity, recipes, R.layout.recipe_list, arrayOf("name"), intArrayOf(R.id.recipeName, R.id.recipeType))
                    recipeListView!!.setAdapter(listAdapter)
                }
            }
        } catch(e: Exception) {

        }
    }

    override fun onResume() {
        super.onResume()
        populateList()
    }

    fun populateList() {
        databaseHelper!!.getWritableDatabase();
        recipes = databaseHelper!!.allRecipes
        val listAdapter = SimpleAdapter(this@MainActivity, recipes, R.layout.recipe_list, arrayOf("name", "type", "ingredients", "steps"), intArrayOf(R.id.recipeName, R.id.recipeType, R.id.recipeIngredients, R.id.recipeSteps))
        recipeListView!!.setAdapter(listAdapter)

        recipeListView!!.setOnItemClickListener { parent, view, position, id ->
            val element = listAdapter.getItem(position) // The item that was clicked
            val intent = Intent(this, ViewRecipeActivity::class.java)
            intent.putExtra("recipeId", recipes[position].get("id"))
            startActivity(intent)
        }
    }
}
