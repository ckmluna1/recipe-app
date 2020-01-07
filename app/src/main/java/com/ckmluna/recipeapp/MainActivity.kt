package com.ckmluna.recipeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.SimpleAdapter
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.xml.sax.SAXException
import java.io.IOException
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException

class MainActivity : AppCompatActivity() {
    var recipeDataHashMap = HashMap<String, String>()
    var recipes: ArrayList<HashMap<String, String>> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            val listView = findViewById<ListView>(R.id.recipeListView)
            val istream = assets.open("recipetypes.xml")
            val builderFactory = DocumentBuilderFactory.newInstance()
            val docBuilder = builderFactory.newDocumentBuilder()
            val doc = docBuilder.parse(istream)

            val recipeList = doc.getElementsByTagName("recipe")
            for (i in 0 until recipeList.getLength()) {
                if (recipeList.item(0).getNodeType().equals(Node.ELEMENT_NODE) ) {
                    //creating instance of HashMap to put the data of node value
                    recipeDataHashMap = HashMap()
                    val element = recipeList.item(i) as Element
                    recipeDataHashMap.put("name", getNodeValue("name", element))
                    recipeDataHashMap.put("type", getNodeValue("type", element))
                    recipeDataHashMap.put("description", getNodeValue("description", element))
                    //adding the HashMap data to ArrayList
                    recipes.add(recipeDataHashMap)
                }
            }
            val adapter = SimpleAdapter(this@MainActivity, recipes, R.layout.recipe_list, arrayOf("name", "type", "description"), intArrayOf(R.id.recipeName, R.id.recipeType, R.id.recipeDescription))
            listView.setAdapter(adapter)
        } catch(e: Exception) {

        }
    }

    // function to return node value
    protected fun getNodeValue(tag: String, element: Element): String {
        val nodeList = element.getElementsByTagName(tag)
        val node = nodeList.item(0)
        if (node != null) {
            if (node.hasChildNodes()) {
                val child = node.getFirstChild()
                while (child != null) {
                    if (child.getNodeType() === Node.TEXT_NODE) {
                        return child.getNodeValue()
                    }
                }
            }
        }
        return ""
    }
}
