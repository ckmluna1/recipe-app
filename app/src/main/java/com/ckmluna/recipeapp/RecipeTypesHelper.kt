package com.ckmluna.recipeapp

import android.content.Context
import org.w3c.dom.Element
import org.w3c.dom.Node
import javax.xml.parsers.DocumentBuilderFactory

class RecipeTypesHelper(context: Context) {
    var context: Context = context
    var recipeTypeIds: ArrayList<Int> = ArrayList();
    var recipeTypeNames: ArrayList<String> = ArrayList();

    init {
        val istream = this.context.assets.open("recipetypes.xml")
        val builderFactory = DocumentBuilderFactory.newInstance()
        val docBuilder = builderFactory.newDocumentBuilder()
        val doc = docBuilder.parse(istream)

        val recipeTypeList = doc.getElementsByTagName("recipetype")
        for (i in 0 until recipeTypeList.getLength()) {
            if (recipeTypeList.item(0).getNodeType().equals(Node.ELEMENT_NODE) ) {
                val element = recipeTypeList.item(i) as Element
                recipeTypeIds.add(getNodeValue("id", element).toInt());
                recipeTypeNames.add(getNodeValue("name", element));
            }
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

    fun getRecipeTypeId(position: Int): Int {
        return recipeTypeIds.get(position)
    }

    fun getRecipeTypeName(position: Int): String {
        return recipeTypeNames.get(position)
    }
}