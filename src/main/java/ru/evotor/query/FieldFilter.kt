package ru.evotor.query

/**
 * Created by a.lunkov on 27.02.2018.
 */

abstract class FieldFilter<V, Q, S : FilterBuilder.SortOrder<S>, T> {

    fun equal(value: V?): Executor<Q, S, T> {
        if (value == null) {
            return appendResult(" IS NULL")
        }
        return equalNotEqual("=", value)
    }

    fun notEqual(value: V?): Executor<Q, S, T> {
        if (value == null) {
            return appendResult(" IS NOT NULL")
        }
        return equalNotEqual("!=", value)
    }

    private fun equalNotEqual(operator: String, value: V): Executor<Q, S, T> {
        return appendResult(operator + if(value is String || value is Enum<*>) "\"$value\"" else "$value")
    }

    fun greater(value: V, including: Boolean): Executor<Q, S, T> {
        return greaterLower('>', value, including)
    }

    fun lower(value: V, including: Boolean): Executor<Q, S, T> {
        return greaterLower('<', value, including)
    }

    private fun greaterLower(operator: Char, value: V, including: Boolean): Executor<Q, S, T> {
        var result = operator.toString()
        if (including) {
            result += "="
        }
        result += value
        return appendResult(result)
    }

    fun like(text: String): Executor<Q, S, T> {
        return appendResult(" LIKE \"$text\"")
    }

    fun between(leftValue: V, rightValue: V): Executor<Q, S, T> {
        return appendResult(" BETWEEN $leftValue AND $rightValue")
    }

    fun inside(values: List<V>): Executor<Q, S, T> {
        return insideNotInside("IN", values)
    }

    fun inside(values: Array<V>): Executor<Q, S, T> {
        return inside(values.toList())
    }

    fun notInside(values: List<V>): Executor<Q, S, T> {
        return insideNotInside("NOT IN", values)
    }

    fun notInside(values: Array<V>): Executor<Q, S, T> {
        return notInside(values.toList())
    }

    private fun insideNotInside(operator: String, values: List<V>): Executor<Q, S, T> {
        val result = StringBuilder()
        values.forEach {
            result.append(if(it is String || it is Enum<*>) "\"$it\"," else "$it,")
        }
        result.setCharAt(result.length - 1, ')')
        return appendResult(" $operator ($result")
    }

    protected abstract fun appendResult(edition: String): Executor<Q, S, T>

}
