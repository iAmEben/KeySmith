package com.iameben.keysmith.util

import android.content.Context

class WordListUtil(private val context: Context) {

    fun loadWordList(): List<String> {
        return try {
            context.assets.open("smart_words.txt").bufferedReader().use() { reader ->
                reader.readLines()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}