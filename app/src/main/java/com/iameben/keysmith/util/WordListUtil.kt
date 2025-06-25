package com.iameben.keysmith.util

import android.content.Context

object WordListUtil {
    fun loadWordList(context: Context): List<String> {
        return try {
            context.assets.open("smart_words.txt").bufferedReader().use() { reader ->
                reader.readLines()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}