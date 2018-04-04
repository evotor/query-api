package ru.evotor.query

import android.database.Cursor


/**
 * Обертка над стандартным android.database.Cursor
 * Добавляется метод getValue(), который возвращает java объект, который собирается из записи в базе
 */
abstract class Cursor<out T>(cursor: Cursor) : android.database.Cursor by cursor {

    abstract fun getValue(): T

    fun toList(): List<T> {
        val result: MutableList<T> = ArrayList()
        if (moveToFirst()) {
            result.add(getValue())
            while (moveToNext()) {
                result.add(getValue())
            }
            close()
        }
        return result
    }

}