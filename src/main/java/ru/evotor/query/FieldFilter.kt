package ru.evotor.query

/**
 * Created by a.lunkov on 27.02.2018.
 */

abstract class FieldFilter<V, T, Q, S : FilterBuilder.SortOrder<S>, R> (private val typeConverter: ((V) -> T)?) {

    fun equal(value: V): Executor<Q, S, R> {
        val result = convertArg(value) ?: return appendSelection(" IS NULL")
        return appendSelection("=?", result)
    }

    fun notEqual(value: V): Executor<Q, S, R> {
        val result = convertArg(value) ?: return appendSelection(" IS NOT NULL")
        return appendSelection("<>?", result)
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

    fun like(text: String, escape: Char? = null): Executor<Q, S, R> {
        return appendSelection(" LIKE ?${if (escape != null) " ESCAPE '$escape'" else ""}", text)
    }

    fun between(leftValue: V, rightValue: V): Executor<Q, S, R> {
        return appendSelection(" BETWEEN ? AND ?", convertArg(leftValue), convertArg(rightValue))
    }

    fun notBetween(leftValue: V, rightValue: V): Executor<Q, S, R> {
        return appendSelection(" NOT BETWEEN ? AND ?", convertArg(leftValue), convertArg(rightValue))
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
        var selection = ""
        val args = ArrayList<String>()
        var argsContainNull = false
        values.forEach {
            val arg = convertArg(it)
            if (arg != null) {
                if (!args.contains(arg)) {
                    selection += ",?"
                    args.add(arg)
                }
            } else {
                argsContainNull = true
            }
        }
        if (selection.isNotEmpty()) {
            selection = selection.drop(1)
        }
        if (argsContainNull) {
            appendSelection(" IS ${if (not) "NOT " else ""}NULL ${if (not) "AND" else "OR"} ")
        }
        return appendSelection(
                " ${if (not) "NOT " else ""}IN ($selection)",
                *args.toTypedArray()
        )
    }

    private fun convertArg(source: V): String? {
        var result: Any? = if (typeConverter != null) typeConverter.invoke(source) else source
        if (result is Boolean) {
            result = if (result) "1" else "0"
        }
        return result?.toString()
    }

    protected abstract fun appendSelection(s: String, vararg args: String?): Executor<Q, S, R>

}
