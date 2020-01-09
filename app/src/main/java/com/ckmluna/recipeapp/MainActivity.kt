package com.ckmluna.recipeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.*

class MainActivity : AppCompatActivity() {
    private var databaseHelper: DatabaseHelper? = null
    private var recipeTypesHelper: RecipeTypesHelper? = null

    private var recipeTypesSpinner: Spinner? = null
    private var recipeListView: ListView? = null
    private var addRecipeFab: View? = null

    private var recipeTypes: ArrayList<String> = ArrayList()
    private var recipeTypesCount: Int = 0
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
            recipeTypesCount = recipeTypes.size
            recipeTypes.add("All")
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
                    if(position == recipeTypesCount) {
                        recipes = databaseHelper!!.allRecipes
                    } else {
                        val recipeTypeId = recipeTypesHelper!!.getRecipeTypeId(position)
                        recipes = databaseHelper!!.getRecipesByType(recipeTypeId)
                    }
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

    //    To display items inside List View
    fun populateList() {
        databaseHelper!!.getWritableDatabase();
        recipes = databaseHelper!!.allRecipes
        val listAdapter = SimpleAdapter(this@MainActivity, recipes, R.layout.recipe_list, arrayOf("name", "type", "ingredients", "steps"), intArrayOf(R.id.recipeName, R.id.recipeType, R.id.recipeIngredients, R.id.recipeSteps))
        recipeListView!!.setAdapter(listAdapter)
        setListViewHeightBasedOnChildren(recipeListView!!)

        recipeListView!!.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, ViewRecipeActivity::class.java)
            intent.putExtra("recipeId", recipes[position].get("id"))
            startActivity(intent)
        }
    }

    //    To make ListView scrollable when list is too long
    fun setListViewHeightBasedOnChildren(listView: ListView) {
        var listAdapter: ListAdapter = listView.adapter
        if (listAdapter == null) return
        var desiredWidth: Int = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        var totalHeight = 0
        var view: View? = null
        for (i in 0 until listAdapter.count) {
            view = listAdapter.getView(i, view, listView)
            if (i == 0)
                view.setLayoutParams(ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.measuredHeight
        }

        var params: ViewGroup.LayoutParams = listView.layoutParams

        params.height = totalHeight + (listView.dividerHeight * (listAdapter.count - 1))

        listView.layoutParams = params
        listView.requestLayout()
    }
}
