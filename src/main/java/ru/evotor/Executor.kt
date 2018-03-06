package ru.evotor

import android.content.Context
import android.net.Uri

/**
 * Created by a.lunkov on 28.02.2018.
 */

abstract class Executor<Q, S : FilterBuilder.SortOrder<S>, T>(private val tableUri: Uri) {

    protected abstract val currentQuery: Q

    internal var selection = StringBuilder()
    private var sortOrderValue: String = ""
    private var limitValue: String = ""

    fun and(intersection: Executor<Q, S, T>): Executor<Q, S, T> {
        selection.append(" AND (").append(intersection.selection).append(")")
        return this
    }

    fun or(union: Executor<Q, S, T>): Executor<Q, S, T> {
        selection.append(" OR (").append(union.selection).append(")")
        return this
    }

    fun and(): Q {
        selection.append(" AND ")
        return currentQuery
    }

    fun or(): Q {
        selection.append(" OR ")
        return currentQuery
    }

    fun limit(value: Int): Executor<Q, S, T> {
        limitValue = " LIMIT $value"
        return this
    }

    fun sortOrder(sortOrder: S): Executor<Q, S, T> {
        sortOrderValue = sortOrder.value.toString()
        return this
    }

    fun execute(context: Context): Cursor<T> {
        val sortOrderLimit = sortOrderValue + limitValue
        return object : Cursor<T>(context.contentResolver.query(
                tableUri,
                null,
                if(selection.isEmpty()) null else selection.toString(),
                null,
                if(sortOrderLimit.isEmpty()) null else sortOrderLimit
        )) {
            override fun toList(): List<T>? {
                var result: List<T>? = null
                if(moveToFirst()) {
                    result = ArrayList()
                    while (moveToNext()) {
                        result.add(getValue())
                    }
                }
                return result
            }

            override fun getValue(): T {
                return this@Executor.getValue(this)
            }
        }
    }

    protected abstract fun getValue(cursor: Cursor<T>): T

}
