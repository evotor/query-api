package ru.evotor.query

import android.database.Cursor


abstract class Cursor<out T>(cursor: Cursor) : android.database.Cursor by cursor {

    abstract fun getValue(): T

    abstract fun toList(): List<T>?

}