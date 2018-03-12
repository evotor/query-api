package ru.evotor.query

import android.database.Cursor


abstract class Cursor<out T>(cursor: Cursor) : android.database.Cursor by cursor {

    abstract fun getValue(): T

    fun toList(): List<T>? {
        var result: List<T>? = null
        if(moveToFirst()) {
            result = ArrayList()
            while (moveToNext()) {
                result.add(getValue())
            }
        }
        return result
    }

}