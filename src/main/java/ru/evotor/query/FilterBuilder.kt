package ru.evotor.query

import android.net.Uri

/**
 * Created by a.lunkov on 27.02.2018.
 */

abstract class FilterBuilder<Q, S : FilterBuilder.SortOrder<S>, R>(tableUri: Uri) {

    protected abstract val currentQuery: Q

    protected abstract fun getValue(cursor: Cursor<R>): R

    private val executor: Executor<Q, S, R>

    init {
        executor = object : Executor<Q, S, R>(tableUri) {
            override fun getValue(cursor: Cursor<R>): R {
                return this@FilterBuilder.getValue(cursor)
            }

            override val currentQuery: Q
                get() = this@FilterBuilder.currentQuery
        }
    }

    fun <V> addFieldFilter(fieldName: String): FieldFilter<V, Q, S, R> {
        return initFieldFilter<V, V>(fieldName, null)
    }

    fun <V, T> addFieldFilter(fieldName: String, typeConverter: (V) -> T): FieldFilter<V, Q, S, R> {
        return initFieldFilter(fieldName, typeConverter)
    }

    private fun <V, T> initFieldFilter(fieldName: String, typeConverter: ((V) -> T)?): FieldFilter<V, Q, S, R> {
        return object : FieldFilter<V, Q, S, R>() {

            override fun convertArg(source: V): String {
                var result: Any? = if (typeConverter != null) typeConverter.invoke(source) else source
                if (result is Boolean) {
                    result = if (result) "1" else "0"
                }
                return result.toString()
            }

            override fun appendSelection(selection: String, vararg args: String): Executor<Q, S, R> {
                executor.selection.append(fieldName + selection)
                args.forEach {
                    executor.selectionArgs.add(it)
                }
                return executor
            }

        }
    }

    fun noFilters(): Executor<Q, S, R> {
        executor.selection = StringBuilder()
        return executor
    }

    abstract class SortOrder<S : SortOrder<S>> {

        internal val value = StringBuilder()

        protected abstract val currentSortOrder: S

        fun addFieldSorter(fieldName: String): FieldSorter<S> {
            return object : FieldSorter<S>() {
                override fun appendResult(edition: String): S {
                    value.append(fieldName + edition)
                    return currentSortOrder
                }
            }
        }

    }

}
