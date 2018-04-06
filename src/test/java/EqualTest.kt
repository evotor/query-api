import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.math.BigDecimal

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class EqualTest {

    @Test
    fun testUsualUsage() {
        MyEntityFilterBuilder()
                .uuid.equal("some_uuid")
                .let {
                    Assert.assertEquals("UUID=?", it.selection.toString())
                    Assert.assertEquals(arrayListOf("some_uuid"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testSpecialSymbols() {
        MyEntityFilterBuilder()
                .uuid.equal("( % \\ LIKE _ \" ' * NULL")
                .let {
                    Assert.assertEquals("UUID=?", it.selection.toString())
                    Assert.assertEquals(arrayListOf("( % \\ LIKE _ \" ' * NULL"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testEmptyValue() {
        MyEntityFilterBuilder()
                .uuid.equal("")
                .let {
                    Assert.assertEquals("UUID=?", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String>(""), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun test1NullValue() {
        MyEntityFilterBuilder()
                .uuid.equal(null)
                .let {
                    Assert.assertEquals("UUID IS NULL", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String>(), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterUsualUsage() {
        MyEntityFilterBuilder()
                .price.equal(BigDecimal(2))
                .let {
                    Assert.assertEquals("PRICE_OUT=?", it.selection.toString())
                    Assert.assertEquals(arrayListOf("200"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterNullValue() {
        MyEntityFilterBuilder()
                .price.equal(null)
                .let {
                    Assert.assertEquals("PRICE_OUT IS NULL", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String>(), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterEmptyStringToNullUsualUsage() {
        MyEntityFilterBuilder()
                .name.equal("1")
                .let {
                    Assert.assertEquals("NAME=?", it.selection.toString())
                    Assert.assertEquals(arrayListOf("1"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterEmptyStringToNull_NullValue() {
        MyEntityFilterBuilder()
                .name.equal(null)
                .let {
                    Assert.assertEquals("NAME IS NULL", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String>(), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterEmptyStringToNullEmptyValue() {
        MyEntityFilterBuilder()
                .name.equal("")
                .let {
                    Assert.assertEquals("NAME IS NULL", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String>(), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterNullToEmptyStringUsualUsage() {
        MyEntityFilterBuilder()
                .notNullName.equal("1")
                .let {
                    Assert.assertEquals("NAME_NOT_NULL=?", it.selection.toString())
                    Assert.assertEquals(arrayListOf("1"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterNullToEmptyString1NullValue() {
        MyEntityFilterBuilder()
                .notNullName.equal(null)
                .let {
                    Assert.assertEquals("NAME_NOT_NULL=?", it.selection.toString())
                    Assert.assertEquals(arrayListOf(""), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterNullToEmptyString1EmptyValue() {
        MyEntityFilterBuilder()
                .notNullName.equal("")
                .let {
                    Assert.assertEquals("NAME_NOT_NULL=?", it.selection.toString())
                    Assert.assertEquals(arrayListOf(""), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }
}