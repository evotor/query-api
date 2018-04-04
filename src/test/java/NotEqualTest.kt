import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.math.BigDecimal

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class NotEqualTest {

    @Test
    fun testUsualUsage() {
        MyEntityFilterBuilder()
                .uuid.notEqual("some_uuid")
                .let {
                    Assert.assertEquals("UUID<>?", it.selection.toString())
                    Assert.assertEquals(arrayListOf("some_uuid"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testSpecialSymbols() {
        MyEntityFilterBuilder()
                .uuid.notEqual("( % \\ LIKE _ \" ' * NULL")
                .let {
                    Assert.assertEquals("UUID<>?", it.selection.toString())
                    Assert.assertEquals(arrayListOf("( % \\ LIKE _ \" ' * NULL"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testEmptyValue() {
        MyEntityFilterBuilder()
                .uuid.notEqual("")
                .let {
                    Assert.assertEquals("UUID<>?", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String>(""), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun test1NullValue() {
        MyEntityFilterBuilder()
                .uuid.notEqual(null)
                .let {
                    Assert.assertEquals("UUID IS NOT NULL", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String>(), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterUsualUsage() {
        MyEntityFilterBuilder()
                .price.notEqual(BigDecimal(2))
                .let {
                    Assert.assertEquals("PRICE_OUT<>?", it.selection.toString())
                    Assert.assertEquals(arrayListOf("200"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterNullValue() {
        MyEntityFilterBuilder()
                .price.notEqual(null)
                .let {
                    Assert.assertEquals("PRICE_OUT IS NOT NULL", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String>(), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterEmptyStringToNullUsualUsage() {
        MyEntityFilterBuilder()
                .name.notEqual("1")
                .let {
                    Assert.assertEquals("NAME<>?", it.selection.toString())
                    Assert.assertEquals(arrayListOf("1"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterEmptyStringToNull_NullValue() {
        MyEntityFilterBuilder()
                .name.notEqual(null)
                .let {
                    Assert.assertEquals("NAME IS NOT NULL", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String>(), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterEmptyStringToNullEmptyValue() {
        MyEntityFilterBuilder()
                .name.notEqual("")
                .let {
                    Assert.assertEquals("NAME IS NOT NULL", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String>(), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterNullToEmptyStringUsualUsage() {
        MyEntityFilterBuilder()
                .notNullName.notEqual("1")
                .let {
                    Assert.assertEquals("NAME_NOT_NULL<>?", it.selection.toString())
                    Assert.assertEquals(arrayListOf("1"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterNullToEmptyString1NullValue() {
        MyEntityFilterBuilder()
                .notNullName.notEqual(null)
                .let {
                    Assert.assertEquals("NAME_NOT_NULL<>?", it.selection.toString())
                    Assert.assertEquals(arrayListOf(""), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterNullToEmptyString1EmptyValue() {
        MyEntityFilterBuilder()
                .notNullName.notEqual("")
                .let {
                    Assert.assertEquals("NAME_NOT_NULL<>?", it.selection.toString())
                    Assert.assertEquals(arrayListOf(""), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }
}