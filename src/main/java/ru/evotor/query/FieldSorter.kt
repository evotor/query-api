package ru.evotor.query

/**
 * Created by a.lunkov on 01.03.2018.
 */
abstract class FieldSorter<S : FilterBuilder.SortOrder<S>> {

    fun asc(): S {
        return append(" ASC,")
    }

    fun desc(): S {
        return append(" DESC,")
    }

    internal abstract fun append(edition: String): S

}