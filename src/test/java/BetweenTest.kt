import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.math.BigDecimal

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class BetweenTest {

    @Test
    fun testUsualUsage() {
        MyEntityFilterBuilder()
                .uuid.between("1", "2")
                .let {
                    Assert.assertEquals("UUID BETWEEN ? AND ?", it.selection.toString())
                    Assert.assertEquals(arrayListOf("1", "2"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testSpecialSymbols() {
        MyEntityFilterBuilder()
                .uuid.between("( % \\ LIKE _ \" ' * NULL", "5")
                .let {
                    Assert.assertEquals("UUID BETWEEN ? AND ?", it.selection.toString())
                    Assert.assertEquals(arrayListOf("( % \\ LIKE _ \" ' * NULL", "5"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testEmptyValues() {
        MyEntityFilterBuilder()
                .uuid.between("", "")
                .let {
                    Assert.assertEquals("UUID BETWEEN ? AND ?", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String>("", ""), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testNullValue() {
        MyEntityFilterBuilder()
                .uuid.between(null, "49")
                .let {
                    Assert.assertEquals("UUID BETWEEN ? AND ?", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String?>(null, "49"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterUsualUsage() {
        MyEntityFilterBuilder()
                .price.between(BigDecimal(2), BigDecimal.TEN)
                .let {
                    Assert.assertEquals("PRICE_OUT BETWEEN ? AND ?", it.selection.toString())
                    Assert.assertEquals(arrayListOf("200", "1000"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterNullValue() {
        MyEntityFilterBuilder()
                .price.between(null, BigDecimal.TEN)
                .let {
                    Assert.assertEquals("PRICE_OUT BETWEEN ? AND ?", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String?>(null, "1000"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterEmptyStringToNullUsualUsage() {
        MyEntityFilterBuilder()
                .name.between("1", "3")
                .let {
                    Assert.assertEquals("NAME BETWEEN ? AND ?", it.selection.toString())
                    Assert.assertEquals(arrayListOf("1", "3"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterEmptyStringToNull_NullValue() {
        MyEntityFilterBuilder()
                .name.between(null, "3")
                .let {
                    Assert.assertEquals("NAME BETWEEN ? AND ?", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String?>(null, "3"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterEmptyStringToNullEmptyValue() {
        MyEntityFilterBuilder()
                .name.between("", "")
                .let {
                    Assert.assertEquals("NAME BETWEEN ? AND ?", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String?>(null, null), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterNullToEmptyStringUsualUsage() {
        MyEntityFilterBuilder()
                .notNullName.between("1", "3")
                .let {
                    Assert.assertEquals("NAME_NOT_NULL BETWEEN ? AND ?", it.selection.toString())
                    Assert.assertEquals(arrayListOf("1", "3"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterNullToEmptyString1NullValue() {
        MyEntityFilterBuilder()
                .notNullName.between(null, "42")
                .let {
                    Assert.assertEquals("NAME_NOT_NULL BETWEEN ? AND ?", it.selection.toString())
                    Assert.assertEquals(arrayListOf("", "42"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testTypeConverterNullToEmptyString1EmptyValue() {
        MyEntityFilterBuilder()
                .notNullName.between("", "3")
                .let {
                    Assert.assertEquals("NAME_NOT_NULL BETWEEN ? AND ?", it.selection.toString())
                    Assert.assertEquals(arrayListOf("", "3"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }
}