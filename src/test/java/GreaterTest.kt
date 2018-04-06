import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.math.BigDecimal

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class GreaterTest {

    @Test
    fun testUsualUsage() {
        MyEntityFilterBuilder()
                .uuid.greater("some_uuid")
                .let {
                    Assert.assertEquals("UUID>?", it.selection.toString())
                    Assert.assertEquals(arrayListOf("some_uuid"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testSpecialSymbols() {
        MyEntityFilterBuilder()
                .uuid.greater("( % \\ LIKE _ \" ' * NULL")
                .let {
                    Assert.assertEquals("UUID>?", it.selection.toString())
                    Assert.assertEquals(arrayListOf("( % \\ LIKE _ \" ' * NULL"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testEmptyValue() {
        MyEntityFilterBuilder()
                .uuid.greater("")
                .let {
                    Assert.assertEquals("UUID>?", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String>(""), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun test1NullValue() {
        MyEntityFilterBuilder()
                .uuid.greater(null)
                .let {
                    Assert.assertEquals("UUID>?", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String?>(null), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterUsualUsage() {
        MyEntityFilterBuilder()
                .price.greater(BigDecimal(2))
                .let {
                    Assert.assertEquals("PRICE_OUT>?", it.selection.toString())
                    Assert.assertEquals(arrayListOf("200"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterNullValue() {
        MyEntityFilterBuilder()
                .price.greater(null)
                .let {
                    Assert.assertEquals("PRICE_OUT>?", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String?>(null), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterEmptyStringToNullUsualUsage() {
        MyEntityFilterBuilder()
                .name.greater("1")
                .let {
                    Assert.assertEquals("NAME>?", it.selection.toString())
                    Assert.assertEquals(arrayListOf("1"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterEmptyStringToNull_NullValue() {
        MyEntityFilterBuilder()
                .name.greater(null)
                .let {
                    Assert.assertEquals("NAME>?", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String?>(null), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterEmptyStringToNullEmptyValue() {
        MyEntityFilterBuilder()
                .name.greater("")
                .let {
                    Assert.assertEquals("NAME>?", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String?>(null), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterNullToEmptyStringUsualUsage() {
        MyEntityFilterBuilder()
                .notNullName.greater("1")
                .let {
                    Assert.assertEquals("NAME_NOT_NULL>?", it.selection.toString())
                    Assert.assertEquals(arrayListOf("1"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterNullToEmptyString1NullValue() {
        MyEntityFilterBuilder()
                .notNullName.greater(null)
                .let {
                    Assert.assertEquals("NAME_NOT_NULL>?", it.selection.toString())
                    Assert.assertEquals(arrayListOf(""), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterNullToEmptyString1EmptyValue() {
        MyEntityFilterBuilder()
                .notNullName.greater("")
                .let {
                    Assert.assertEquals("NAME_NOT_NULL>?", it.selection.toString())
                    Assert.assertEquals(arrayListOf(""), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }
}