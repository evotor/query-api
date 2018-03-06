package ru.evotor

import android.net.Uri

/**
 * Created by a.lunkov on 27.02.2018.
 */

abstract class FilterBuilder<Q, S : FilterBuilder.SortOrder<S>, T>(tableUri: Uri) {

    protected abstract val currentQuery: Q

    protected abstract fun getValue(cursor: Cursor<T>): T

    private val executor: Executor<Q, S, T>

    init {
        executor = object : Executor<Q, S, T>(tableUri) {
            override fun getValue(cursor: Cursor<T>): T {
                return this@FilterBuilder.getValue(cursor)
            }

            override val currentQuery: Q
                get() = this@FilterBuilder.currentQuery
        }
    }

    fun <V> addFieldFilter(fieldName: String): FieldFilter<V, Q, S, T> {
        return object : FieldFilter<V, Q, S, T>(fieldName) {
            override fun appendResult(edition: String): Executor<Q, S, T> {
                executor.selection.append(edition)
                return executor
            }
        }
    }

    fun noFilters(): Executor<Q, S, T> {
        executor.selection = StringBuilder()
        return executor
    }

    abstract class SortOrder<S : SortOrder<S>> {

        internal val value = StringBuilder()

        protected abstract val currentSortOrder: S

        fun addFieldSorter(fieldName: String): FieldSorter<S> {
            return object : FieldSorter<S>(fieldName) {
                override fun appendResult(edition: String): S {
                    value.append(edition)
                    return currentSortOrder
                }
            }
        }

    }

}
