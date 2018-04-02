package ru.evotor.query

/**
 * Created by a.lunkov on 27.02.2018.
 */

abstract class FieldFilter<V, T, Q, S : FilterBuilder.SortOrder<S>, R>(
        private val typeConverter: ((V) -> T)?) {

    fun equal(value: V): Executor<Q, S, R> {
        if (value == null) {
            return appendResult(" IS NULL")
        }
        return appendResult(appendOperator("=", value))
    }

    fun notEqual(value: V): Executor<Q, S, R> {
        if (value == null) {
            return appendResult(" IS NOT NULL")
        }
        return appendResult(appendOperator("!=", value))
    }

    fun greater(value: V, including: Boolean): Executor<Q, S, R> {
        return greaterLower(">", value, including)
    }

    fun lower(value: V, including: Boolean): Executor<Q, S, R> {
        return greaterLower("<", value, including)
    }

    private fun greaterLower(operator: String, value: V, including: Boolean): Executor<Q, S, R> {
        return appendResult(appendOperator(if (including) "$operator=" else operator, value))
    }

    fun like(text: String): Executor<Q, S, R> {
        return appendResult(" LIKE \"$text\"")
    }

    fun between(leftValue: V, rightValue: V): Executor<Q, S, R> {
        return appendResult(
                " ${appendOperator("BETWEEN ", leftValue)} ${appendOperator("AND ", rightValue)}"
        )
    }

    fun inside(values: List<V>): Executor<Q, S, R> {
        return insideNotInside("IN", values)
    }

    fun inside(values: Array<V>): Executor<Q, S, R> {
        return inside(values.toList())
    }

    fun notInside(values: List<V>): Executor<Q, S, R> {
        return insideNotInside("NOT IN", values)
    }

    fun notInside(values: Array<V>): Executor<Q, S, R> {
        return notInside(values.toList())
    }

    private fun insideNotInside(operator: String, values: List<V>): Executor<Q, S, R> {
        var result = ""
        values.forEach {
            result = appendOperator("$result,", it)
        }
        if (result.isNotEmpty()) {
            result = result.drop(1)
        }
        return appendResult(" $operator ($result)")
    }

    private fun appendOperator(operator: String, sourceValue: V): String {
        val targetValue: Any? = if (typeConverter == null) sourceValue else typeConverter.invoke(sourceValue)
        return operator + if (targetValue is String || targetValue is Enum<*>) "\"$targetValue\"" else "$targetValue"
    }

    protected abstract fun appendResult(edition: String): Executor<Q, S, R>

}
