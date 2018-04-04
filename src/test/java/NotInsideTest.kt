import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.math.BigDecimal

/**
 * Created by a.lunkov on 06.03.2018.
 */
@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class NotInsideTest {

    @Test
    fun testUsualUsage() {
        MyEntityFilterBuilder()
                .uuid.notInside(arrayListOf("1", "2"))
                .let {
                    Assert.assertEquals("UUID NOT IN (?,?)", it.selection.toString())
                    Assert.assertEquals(arrayListOf("1", "2"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testSpecialSymbols() {
        MyEntityFilterBuilder()
                .uuid.notInside(arrayListOf("(", "%", "\\", "LIKE", "_", "\"", "'", "*", "NULL"))
                .let {
                    Assert.assertEquals("UUID NOT IN (?,?,?,?,?,?,?,?,?)", it.selection.toString())
                    Assert.assertEquals(arrayListOf("(", "%", "\\", "LIKE", "_", "\"", "'", "*", "NULL"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testEmptyList() {
        MyEntityFilterBuilder()
                .uuid.notInside(arrayListOf())
                .let {
                    Assert.assertEquals("UUID NOT IN ()", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String>(), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun test1NullValue() {
        MyEntityFilterBuilder()
                .uuid.notInside(arrayListOf(null))
                .let {
                    Assert.assertEquals("UUID IS NOT NULL AND UUID NOT IN ()", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String>(), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun test2NullValues() {
        MyEntityFilterBuilder()
                .uuid.notInside(arrayListOf(null, null))
                .let {
                    Assert.assertEquals("UUID IS NOT NULL AND UUID NOT IN ()", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String>(), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun test2NullValuesAndString() {
        MyEntityFilterBuilder()
                .uuid.notInside(arrayListOf(null, "some_value", null))
                .let {
                    Assert.assertEquals("UUID IS NOT NULL AND UUID NOT IN (?)", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String>("some_value"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterUsualUsage() {
        MyEntityFilterBuilder()
                .price.notInside(arrayListOf(BigDecimal(1), BigDecimal(2)))
                .let {
                    Assert.assertEquals("PRICE_OUT NOT IN (?,?)", it.selection.toString())
                    Assert.assertEquals(arrayListOf("100", "200"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterEmptyList() {
        MyEntityFilterBuilder()
                .price.notInside(arrayListOf())
                .let {
                    Assert.assertEquals("PRICE_OUT NOT IN ()", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String>(), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverter1NullValue() {
        MyEntityFilterBuilder()
                .price.notInside(arrayListOf(null))
                .let {
                    Assert.assertEquals("PRICE_OUT IS NOT NULL AND PRICE_OUT NOT IN ()", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String>(), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverter2NullValues() {
        MyEntityFilterBuilder()
                .price.notInside(arrayListOf(null, null))
                .let {
                    Assert.assertEquals("PRICE_OUT IS NOT NULL AND PRICE_OUT NOT IN ()", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String>(), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverter2NullValuesAndString() {
        MyEntityFilterBuilder()
                .price.notInside(arrayListOf(null, BigDecimal.ONE, null))
                .let {
                    Assert.assertEquals("PRICE_OUT IS NOT NULL AND PRICE_OUT NOT IN (?)", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String>("100"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterEmptyStringToNullUsualUsage() {
        MyEntityFilterBuilder()
                .name.notInside(arrayListOf("1", "2"))
                .let {
                    Assert.assertEquals("NAME NOT IN (?,?)", it.selection.toString())
                    Assert.assertEquals(arrayListOf("1", "2"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterEmptyStringToNullEmptyList() {
        MyEntityFilterBuilder()
                .name.notInside(arrayListOf())
                .let {
                    Assert.assertEquals("NAME NOT IN ()", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String>(), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterEmptyStringToNull1NullValue() {
        MyEntityFilterBuilder()
                .name.notInside(arrayListOf(null))
                .let {
                    Assert.assertEquals("NAME IS NOT NULL AND NAME NOT IN ()", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String>(), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterEmptyStringToNull2NullValues() {
        MyEntityFilterBuilder()
                .name.notInside(arrayListOf(null, null))
                .let {
                    Assert.assertEquals("NAME IS NOT NULL AND NAME NOT IN ()", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String>(), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterEmptyStringToNull2NullValuesAndString() {
        MyEntityFilterBuilder()
                .name.notInside(arrayListOf(null, "1", null))
                .let {
                    Assert.assertEquals("NAME IS NOT NULL AND NAME NOT IN (?)", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String>("1"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterEmptyStringToNull1EmptyValue() {
        MyEntityFilterBuilder()
                .name.notInside(arrayListOf(""))
                .let {
                    Assert.assertEquals("NAME IS NOT NULL AND NAME NOT IN ()", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String>(), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterEmptyStringToNull1NullAnd1EmptyValues() {
        MyEntityFilterBuilder()
                .name.notInside(arrayListOf(null, ""))
                .let {
                    Assert.assertEquals("NAME IS NOT NULL AND NAME NOT IN ()", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String>(), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }


    @Test
    fun testTypeConverterNullToEmptyStringUsualUsage() {
        MyEntityFilterBuilder()
                .notNullName.inside(arrayListOf("1", "2"))
                .let {
                    Assert.assertEquals("NAME_NOT_NULL IN (?,?)", it.selection.toString())
                    Assert.assertEquals(arrayListOf("1", "2"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterNullToEmptyStringEmptyList() {
        MyEntityFilterBuilder()
                .notNullName.notInside(arrayListOf())
                .let {
                    Assert.assertEquals("NAME_NOT_NULL NOT IN ()", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String>(), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterNullToEmptyString1NullValue() {
        MyEntityFilterBuilder()
                .notNullName.inside(arrayListOf(null))
                .let {
                    Assert.assertEquals("NAME_NOT_NULL IN (?)", it.selection.toString())
                    Assert.assertEquals(arrayListOf(""), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterNullToEmptyString2NullValues() {
        MyEntityFilterBuilder()
                .notNullName.inside(arrayListOf(null, null))
                .let {
                    Assert.assertEquals("NAME_NOT_NULL IN (?)", it.selection.toString())
                    Assert.assertEquals(arrayListOf(""), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterNullToEmptyString2NullValuesAndString() {
        MyEntityFilterBuilder()
                .notNullName.inside(arrayListOf(null, "1", null))
                .let {
                    Assert.assertEquals("NAME_NOT_NULL IN (?,?)", it.selection.toString())
                    Assert.assertEquals(arrayListOf("", "1"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterNullToEmptyString1EmptyValue() {
        MyEntityFilterBuilder()
                .notNullName.inside(arrayListOf(""))
                .let {
                    Assert.assertEquals("NAME_NOT_NULL IN (?)", it.selection.toString())
                    Assert.assertEquals(arrayListOf(""), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterNullToEmptyString1NullAnd1EmptyValues() {
        MyEntityFilterBuilder()
                .notNullName.inside(arrayListOf(null, ""))
                .let {
                    Assert.assertEquals("NAME_NOT_NULL IN (?)", it.selection.toString())
                    Assert.assertEquals(arrayListOf(""), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

}