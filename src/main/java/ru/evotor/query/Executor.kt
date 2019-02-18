package ru.evotor.query

import android.content.Context
import android.net.Uri
import android.util.Log
import java.util.*

/**
 * Created by a.lunkov on 28.02.2018.
 */

abstract class Executor<Q, S : FilterBuilder.SortOrder<S>, R>(private val tableUri: Uri) {

    protected abstract val currentQuery: Q

    internal var selection = StringBuilder()
    internal var selectionArgs = ArrayList<String?>()
    internal var sortOrderValue = ""
        private set
    internal var limitValue = ""
        private set

    fun and(intersection: Executor<Q, S, R>): Executor<Q, S, R> {
        return intersectionUnion("AND", intersection)
    }

    fun or(union: Executor<Q, S, R>): Executor<Q, S, R> {
        return intersectionUnion("OR", union)
    }

    private fun intersectionUnion(operator: String, target: Executor<Q, S, R>): Executor<Q, S, R> {
        if (target.selection.isNotBlank()) {
            val wasNotBlank = selection.isNotBlank()
            if (wasNotBlank) {
                selection.append(" $operator (")
            }
            selection.append(target.selection)
            if (wasNotBlank) {
                selection.append(")")
            }
            selectionArgs.addAll(target.selectionArgs)
        }
        return this
    }

    fun and(): Q {
        if (selection.isNotBlank()) {
            selection.append(" AND ")
        }
        return currentQuery
    }

    fun or(): Q {
        if (selection.isNotBlank()) {
            selection.append(" OR ")
        }
        return currentQuery
    }

    fun limit(value: Int): Executor<Q, S, R> {
        limitValue = " LIMIT $value"
        return this
    }

    fun sortOrder(sortOrder: S): Executor<Q, S, R> {
        sortOrderValue = sortOrder.value.toString().dropLast(1)
        return this
    }

    fun execute(context: Context): Cursor<R> {
        val sortOrderLimit = (if (sortOrderValue.isEmpty()) "ROWID" else sortOrderValue) + limitValue
        Log.v("Executor", "Executing query: tableUri=$tableUri selection=${if (selection.isEmpty()) null else selection.toString()} selectionArgs=${Arrays.toString(selectionArgs.toTypedArray())} sortOrderLimit=${if (sortOrderLimit.isEmpty()) null else sortOrderLimit}")
        return object : Cursor<R>(context.contentResolver.query(
                tableUri,
                null,
                if (selection.isEmpty()) null else selection.toString(),
                if (selectionArgs.isEmpty()) null else selectionArgs.toTypedArray(),
                if (sortOrderLimit.isEmpty()) null else sortOrderLimit
        )) {
            override fun getValue(): R {
                return this@Executor.getValue(context, this)
            }
        }
    }

    protected abstract fun getValue(context: Context, cursor: Cursor<R>): R

}
