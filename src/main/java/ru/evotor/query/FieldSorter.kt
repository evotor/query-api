package ru.evotor.query

/**
 * Created by a.lunkov on 01.03.2018.
 */
abstract class FieldSorter<S : FilterBuilder.SortOrder<S>>(private val fieldName: String) {

    fun asc(): S {
        return appendResult(fieldName + " ASC,")
    }

    fun desc(): S {
        return appendResult(fieldName + " DESC,")
    }

    internal abstract fun appendResult(edition: String): S

}