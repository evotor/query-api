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

    fun <T : FilterBuilder.Inner<Q, S, R>> addInnerFilterBuilder(target: T): T {
        target.appendFieldSelection = { fieldName: String, s: String, args: Array<out String?> ->
            appendFieldSelection(fieldName, s, *args)
        }
        return target
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

        fun <T : FilterBuilder.Inner.SortOrder<S>> addInnerSortOrder(target: T): T {
            target.appendFieldOrder = { fieldName, edition -> appendFieldOrder(fieldName, edition) }
            return target
        }

        protected abstract fun appendFieldOrder(fieldName: String, edition: String): S

    }

}