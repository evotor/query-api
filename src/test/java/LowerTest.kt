import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.math.BigDecimal

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class LowerTest {

    @Test
    fun testUsualUsage() {
        MyEntityFilterBuilder()
                .uuid.lower("some_uuid")
                .let {
                    Assert.assertEquals("UUID<?", it.selection.toString())
                    Assert.assertEquals(arrayListOf("some_uuid"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testSpecialSymbols() {
        MyEntityFilterBuilder()
                .uuid.lower("( % \\ LIKE _ \" ' * NULL")
                .let {
                    Assert.assertEquals("UUID<?", it.selection.toString())
                    Assert.assertEquals(arrayListOf("( % \\ LIKE _ \" ' * NULL"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testEmptyValue() {
        MyEntityFilterBuilder()
                .uuid.lower("")
                .let {
                    Assert.assertEquals("UUID<?", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String>(""), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun test1NullValue() {
        MyEntityFilterBuilder()
                .uuid.lower(null)
                .let {
                    Assert.assertEquals("UUID<?", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String?>(null), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterUsualUsage() {
        MyEntityFilterBuilder()
                .price.lower(BigDecimal(2))
                .let {
                    Assert.assertEquals("PRICE_OUT<?", it.selection.toString())
                    Assert.assertEquals(arrayListOf("200"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterNullValue() {
        MyEntityFilterBuilder()
                .price.lower(null)
                .let {
                    Assert.assertEquals("PRICE_OUT<?", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String?>(null), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterEmptyStringToNullUsualUsage() {
        MyEntityFilterBuilder()
                .name.lower("1")
                .let {
                    Assert.assertEquals("NAME<?", it.selection.toString())
                    Assert.assertEquals(arrayListOf("1"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterEmptyStringToNull_NullValue() {
        MyEntityFilterBuilder()
                .name.lower(null)
                .let {
                    Assert.assertEquals("NAME<?", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String?>(null), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterEmptyStringToNullEmptyValue() {
        MyEntityFilterBuilder()
                .name.lower("")
                .let {
                    Assert.assertEquals("NAME<?", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String?>(null), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterNullToEmptyStringUsualUsage() {
        MyEntityFilterBuilder()
                .notNullName.lower("1")
                .let {
                    Assert.assertEquals("NAME_NOT_NULL<?", it.selection.toString())
                    Assert.assertEquals(arrayListOf("1"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterNullToEmptyString1NullValue() {
        MyEntityFilterBuilder()
                .notNullName.lower(null)
                .let {
                    Assert.assertEquals("NAME_NOT_NULL<?", it.selection.toString())
                    Assert.assertEquals(arrayListOf(""), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterNullToEmptyString1EmptyValue() {
        MyEntityFilterBuilder()
                .notNullName.lower("")
                .let {
                    Assert.assertEquals("NAME_NOT_NULL<?", it.selection.toString())
                    Assert.assertEquals(arrayListOf(""), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }
}