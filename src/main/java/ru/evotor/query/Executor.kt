package ru.evotor.query

import android.content.Context
import android.net.Uri

/**
 * Created by a.lunkov on 28.02.2018.
 */

abstract class Executor<Q, S : FilterBuilder.SortOrder<S>, R>(private val tableUri: Uri) {

    protected abstract val currentQuery: Q

    internal var selection = StringBuilder()
    internal var sortOrderValue = ""
        private set
    internal var limitValue = ""
        private set

    fun and(intersection: Executor<Q, S, R>): Executor<Q, S, R> {
        selection.append(" AND (").append(intersection.selection).append(")")
        return this
    }

    fun or(union: Executor<Q, S, R>): Executor<Q, S, R> {
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

    fun limit(value: Int): Executor<Q, S, R> {
        limitValue = " LIMIT $value"
        return this
    }

    fun sortOrder(sortOrder: S): Executor<Q, S, R> {
        sortOrderValue = " " + sortOrder.value.toString().dropLast(1)
        return this
    }

    fun execute(context: Context): Cursor<R> {
        val sortOrderLimit = sortOrderValue + limitValue
        return object : Cursor<R>(context.contentResolver.query(
                tableUri,
                null,
                if (selection.isEmpty()) null else selection.toString(),
                null,
                if (sortOrderLimit.isEmpty()) null else sortOrderLimit
        )) {
            override fun getValue(): R {
                return this@Executor.getValue(this)
            }
        }
    }

    protected abstract fun getValue(cursor: Cursor<R>): R

}
