package ru.evotor.query

/**
 * Created by a.lunkov on 27.02.2018.
 */

abstract class FieldFilter<V, Q, S : FilterBuilder.SortOrder<S>, R> {

    fun equal(value: V): Executor<Q, S, R> {
        if (value == null) {
            return appendSelection(" IS NULL")
        }
        return appendSelection("=?", convertArg(value))
    }

    fun notEqual(value: V): Executor<Q, S, R> {
        if (value == null) {
            return appendSelection(" IS NOT NULL")
        }
        return appendSelection("<>?", convertArg(value))
    }

    fun greater(value: V, including: Boolean = false): Executor<Q, S, R> {
        return greaterLower(">", value, including)
    }

    fun lower(value: V, including: Boolean = false): Executor<Q, S, R> {
        return greaterLower("<", value, including)
    }

    private fun greaterLower(operator: String, value: V, including: Boolean): Executor<Q, S, R> {
        return appendSelection(operator + if (including) "=?" else "?", convertArg(value))
    }

    fun like(text: String, useEscape: Boolean = false): Executor<Q, S, R> {
        return appendSelection(
                " LIKE ?${if (useEscape) " ESCAPE '\\'" else ""}",
                if (useEscape)
                    "%${text.replace("\\", "\\" + "\\")
                            .replace("%", "\\" + "%")
                            .replace("_", "\\" + "_")}%"
                else
                    text
        )
    }

    fun between(leftValue: V, rightValue: V): Executor<Q, S, R> {
        return appendSelection("BETWEEN ? AND ?", convertArg(leftValue), convertArg(rightValue))
    }

    fun inside(values: List<V>): Executor<Q, S, R> {
        return insideNotInside(false, values)
    }

    fun inside(values: Array<V>): Executor<Q, S, R> {
        return inside(values.toList())
    }

    fun notInside(values: List<V>): Executor<Q, S, R> {
        return insideNotInside(true, values)
    }

    fun notInside(values: Array<V>): Executor<Q, S, R> {
        return notInside(values.toList())
    }

    private fun insideNotInside(not: Boolean, values: List<V>): Executor<Q, S, R> {
        var resultSelection = ""
        val resultArgs = ArrayList<String>()
        var containsNull = false
        values.forEach {
            if (it != null) {
                resultSelection += ",?"
                resultArgs.add(convertArg(it))
            } else {
                containsNull = true
            }
        }
        if (resultSelection.isNotEmpty()) {
            resultSelection = resultSelection.drop(1)
        }
        if (containsNull) {
            appendSelection(" IS ${if (not) "NOT " else ""}NULL OR ")
        }
        return appendSelection(
                " ${if (not) "NOT " else ""}IN ($resultSelection)",
                *resultArgs.toTypedArray()
        )
    }

    protected abstract fun convertArg(arg: V): String

    protected abstract fun appendSelection(selection: String, vararg args: String): Executor<Q, S, R>

}
