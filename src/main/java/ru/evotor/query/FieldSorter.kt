package ru.evotor.query

/**
 * Created by a.lunkov on 01.03.2018.
 */
abstract class FieldSorter<S : FilterBuilder.SortOrder<S>> {

    fun asc(): S {
        return appendResult(" ASC,")
    }

    fun desc(): S {
        return appendResult(" DESC,")
    }

    internal abstract fun appendResult(edition: String): S

}