package com.kuriamind.core

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

abstract class StorageManager<T>(
        context: Context,
        private val key: String,
        private val typeToken: TypeToken<List<T>>
) {
    private val prefs: SharedPreferences =
            context.getSharedPreferences("kuria_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun getItems(): List<T> {
        val json = prefs.getString(key, "[]") ?: "[]"
        // Log.d("DEBUG", json)
        return gson.fromJson(json, typeToken.type)
    }

    fun saveItems(items: List<T>) {
        prefs.edit().putString(key, gson.toJson(items)).apply()
    }

    fun addItem(item: T) {
        val items = getItems().toMutableList()
        items.add(item)
        saveItems(items)
    }

    fun updateItem(updatedItem: T, match: (T) -> Boolean) {
        val items = getItems().map { if (match(it)) updatedItem else it }
        saveItems(items)
    }

    fun deleteItem(match: (T) -> Boolean) {
        val items = getItems().filterNot(match)
        saveItems(items)
    }
}
