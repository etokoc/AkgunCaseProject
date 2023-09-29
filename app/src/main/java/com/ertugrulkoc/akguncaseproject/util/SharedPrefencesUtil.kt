package com.ertugrulkoc.akguncaseproject.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SharedPrefencesUtil(context: Context) {
    private var local: SharedPreferences

    init {
        local = context.getSharedPreferences(Constants.SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE)
    }

    fun addCard(uId: String) {
        val newList = arrayListOf<String>()
        newList.addAll(getCardsFromLocal())
        newList.add(uId)
        local.edit().apply {
            this.putStringSet(Constants.SHARED_PREF_VALUE_NAME, newList.toSet())
        }.apply()
    }

    fun isCardSaved(uId: String): Boolean {
        return getCardsFromLocal().contains(uId)
    }

    fun getCardsFromLocal(): Array<String> {
        return local.getStringSet(Constants.SHARED_PREF_VALUE_NAME, emptySet())
            ?.toTypedArray() ?: arrayOf()
    }
}