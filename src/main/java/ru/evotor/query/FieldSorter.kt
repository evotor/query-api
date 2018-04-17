package ru.evotor.query

/**
 * Created by a.lunkov on 01.03.2018.
 */
abstract class FieldSorter<S : FilterBuilder.SortOrder<S>> {

    fun asc(): S {
        return appendOrder(" ASC,")
    }

    fun desc(): S {
        return appendOrder(" DESC,")
    }

    internal abstract fun appendOrder(edition: String): S

}