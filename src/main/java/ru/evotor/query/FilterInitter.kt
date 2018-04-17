package ru.evotor.query

abstract class FilterInitter<Q, S : FilterBuilder.SortOrder<S>, R> {

    fun <V> addFieldFilter(fieldName: String): FieldFilter<V, V, Q, S, R> {
        return initFieldFilter(fieldName, null)
    }

    fun <V, T> addFieldFilter(fieldName: String, typeConverter: (V) -> T): FieldFilter<V, T, Q, S, R> {
        return initFieldFilter(fieldName, typeConverter)
    }

    private fun <V, T> initFieldFilter(fieldName: String, typeConverter: ((V) -> T)?): FieldFilter<V, T, Q, S, R> {
        return object : FieldFilter<V, T, Q, S, R>(typeConverter) {
            override fun appendSelection(s: String, vararg args: String?): Executor<Q, S, R> {
                return appendFieldSelection(fieldName, s, *args)
            }
        }
    }

    abstract fun appendFieldSelection(fieldName: String, s: String, vararg args: String?): Executor<Q, S, R>

    abstract class SortOrder<S : FilterBuilder.SortOrder<S>> {

        fun addFieldSorter(fieldName: String): FieldSorter<S> {
            return object : FieldSorter<S>() {
                override fun appendOrder(edition: String): S {
                    return appendFieldOrder(fieldName, edition)
                }
            }
        }

        protected abstract fun appendFieldOrder(fieldName: String, edition: String): S

    }

}