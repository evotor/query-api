package ru.evotor.query

import android.content.Context
import android.net.Uri

/**
 * Created by a.lunkov on 27.02.2018.
 */

abstract class FilterBuilder<Q, S : FilterBuilder.SortOrder<S>, R>(tableUri: Uri) : FilterInitter<Q, S, R>() {

    protected abstract fun getValue(context: Context, cursor: Cursor<R>): R

    private val executor: Executor<Q, S, R>

    init {
        executor = object : Executor<Q, S, R>(tableUri) {
            override fun getValue(context: Context, cursor: Cursor<R>): R {
                return this@FilterBuilder.getValue(context, cursor)
            }

            override val currentQuery: Q
                get() = this@FilterBuilder as Q
        }
    }

    fun noFilters(): Executor<Q, S, R> {
        executor.selection = StringBuilder()
        executor.selectionArgs = ArrayList()
        return executor
    }

    override fun appendFieldSelection(fieldName: String, s: String, vararg args: String?): Executor<Q, S, R> {
        executor.selection.append(fieldName + s)
        args.forEach {
            executor.selectionArgs.add(it)
        }
        return executor
    }

    abstract class Inner<Q, S : FilterBuilder.SortOrder<S>, R> : FilterInitter<Q, S, R>() {

        internal var appendFieldSelection: ((fieldName: String, s: String, args: Array<out String?>) -> Executor<Q, S, R>)? = null

        override fun appendFieldSelection(fieldName: String, s: String, vararg args: String?): Executor<Q, S, R> {
            return appendFieldSelection!!.invoke(fieldName, s, args)
        }

        abstract class SortOrder<S : FilterBuilder.SortOrder<S>> : FilterInitter.SortOrder<S>() {

            internal var appendFieldOrder: ((fieldName: String, edition: String) -> S)? = null

            override fun appendFieldOrder(fieldName: String, edition: String): S {
                return appendFieldOrder!!.invoke(fieldName, edition)
            }

        }

    }

    abstract class SortOrder<S : FilterBuilder.SortOrder<S>> : FilterInitter.SortOrder<S>() {

        internal val value = StringBuilder()

        override fun appendFieldOrder(fieldName: String, edition: String): S {
            value.append(fieldName + edition)
            return this as S
        }

    }


}
