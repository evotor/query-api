import android.net.Uri
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import ru.evotor.query.Cursor
import ru.evotor.query.FilterBuilder
import java.math.BigDecimal
import java.util.*

/**
 * Created by a.lunkov on 06.03.2018.
 */
@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class FilterBuilderTest {
    @Test
    fun testMyEntityFilterBuilder() {
        val insideNumbers: Array<BigDecimal?> = arrayOf(BigDecimal(1), BigDecimal(2), null, BigDecimal(3))
        val myEntityExecutor = MyEntityFilterBuilder()
                .uuid.like("%Молоко_40%%", true)
                .and().alcoholProductKindCode.equal(null)
                .or(MyEntityFilterBuilder()
                        .price.inside(insideNumbers)
                        .and().parentUuid.notEqual(null)
                        .and(MyEntityFilterBuilder()
                                .alcoholProductKindCode.greater(10, true)
                                .and().enumField.equal(MyEntityEnum.FIRST_VALUE)))
                .and().price.lower(BigDecimal.valueOf(4))
                .sortOrder(MyEntityFilterBuilder.SortOrder()
                        .uuid.asc()
                        .enumField.desc()
                        .parentUuid.asc())
                .limit(10)
        var myEntityQuery = myEntityExecutor.selection.toString()
        myEntityQuery += myEntityExecutor.sortOrderValue + myEntityExecutor.limitValue
        println(myEntityQuery)
        println(Arrays.toString(myEntityExecutor.selectionArgs.toTypedArray()))
        Assert.assertEquals(
                "UUID LIKE ? ESCAPE '\\' AND ALCOHOL_PRODUCT_KIND_CODE IS NULL OR (PRICE_OUT IS NULL OR PRICE_OUT IN (?,?,?) AND PARENT_UUID IS NOT NULL AND (ALCOHOL_PRODUCT_KIND_CODE>=? AND ENUM_FIELD=?)) AND PRICE_OUT<? UUID ASC,ENUM_FIELD DESC,PARENT_UUID ASC LIMIT 10",
                myEntityQuery
        )
    }

    @Test
    fun testInside() {
        Assert.assertEquals(
                "UUID IN (\"1\",\"2\")",
                MyEntityFilterBuilder()
                        .uuid.inside(arrayListOf("1", "2"))
                        .selection.toString()
        )

        Assert.assertEquals(
                "UUID IN (\"(\",\")\")",
                MyEntityFilterBuilder()
                        .uuid.inside(arrayListOf("(", ")"))
                        .selection.toString()
        )

        Assert.assertEquals(
                "UUID IN ()",
                MyEntityFilterBuilder()
                        .uuid.inside(arrayListOf())
                        .selection.toString()
        )

        Assert.assertEquals(
                "UUID NOT IN ()",
                MyEntityFilterBuilder()
                        .uuid.notInside(arrayListOf())
                        .selection.toString()
        )
    }

    @Test
    fun testBetween() {
        Assert.assertEquals(
                "UUID BETWEEN \"1\" AND \"100\")",
                MyEntityFilterBuilder()
                        .uuid.between("1", "100")
                        .selection.toString()
        )
    }

    @Test
    fun testGreater() {
        Assert.assertEquals(
                "UUID>\"5 AND TEST = 6\\\"\\\"\"",
                MyEntityFilterBuilder()
                        .uuid.greater("5 AND TEST = 6\"\"", false)
                        .selection.toString()
        )

        Assert.assertEquals(
                "UUID>=\"1\")",
                MyEntityFilterBuilder()
                        .uuid.greater("1", true)
                        .selection.toString()
        )
    }

    @Test
    fun testLower() {
        Assert.assertEquals(
                "UUID<\"5 AND TEST = 6\\\"\\\"\"",
                MyEntityFilterBuilder()
                        .uuid.lower("5 AND TEST = 6\"\"", false)
                        .selection.toString()
        )

        Assert.assertEquals(
                "UUID<=\"1\")",
                MyEntityFilterBuilder()
                        .uuid.lower("1", true)
                        .selection.toString()
        )
    }

    @Test
    fun testLike() {
        Assert.assertEquals(
                "UUID LIKE \"2%\"",
                MyEntityFilterBuilder()
                        .uuid.like("2%")
                        .selection.toString()
        )
    }

    @Test
    fun testSortOrderByOneField() {
        Assert.assertEquals(
                "PRICE_OUT ASC",
                MyEntityFilterBuilder()
                        .uuid.equal("someUuid")
                        .sortOrder(MyEntityFilterBuilder.SortOrder().price.asc())
                        .sortOrderValue
        )

        Assert.assertEquals(
                "PRICE_OUT DESC",
                MyEntityFilterBuilder()
                        .uuid.equal("someUuid")
                        .sortOrder(MyEntityFilterBuilder.SortOrder().price.desc())
                        .sortOrderValue
        )
    }

    @Test
    fun testSortOrderByManyField() {
        Assert.assertEquals(
                "PRICE_OUT ASC, ALCOHOL_PRODUCT_KIND_CODE DESC",
                MyEntityFilterBuilder()
                        .uuid.equal("someUuid")
                        .sortOrder(MyEntityFilterBuilder.SortOrder()
                                .price.asc()
                                .alcoholProductKindCode.desc())
                        .sortOrderValue
        )

        Assert.assertEquals(
                "PRICE_OUT ASC, ALCOHOL_PRODUCT_KIND_CODE DESC, ENUM_FIELD ASC",
                MyEntityFilterBuilder()
                        .uuid.equal("someUuid")
                        .sortOrder(MyEntityFilterBuilder.SortOrder()
                                .price.asc()
                                .alcoholProductKindCode.desc()
                                .enumField.asc()
                        )
                        .sortOrderValue
        )
    }

    @Test
    fun testLimitField() {
        Assert.assertEquals(
                " LIMIT 100",
                MyEntityFilterBuilder()
                        .uuid.equal("someUuid")
                        .limit(100)
                        .limitValue
        )
    }

    internal class MyEntityFilterBuilder : FilterBuilder<MyEntityFilterBuilder, MyEntityFilterBuilder.SortOrder, String>(Uri.parse("http://google.com")) {

        val uuid = addFieldFilter<String?>("UUID")
        val parentUuid = addFieldFilter<String?>("PARENT_UUID")
        val price = addFieldFilter<BigDecimal?, Int?>("PRICE_OUT", {it?.toInt()?.times(100) })
        val alcoholProductKindCode = addFieldFilter<Long?, Int?>("ALCOHOL_PRODUCT_KIND_CODE", {it?.toInt()?.times(10)})
        val enumField = addFieldFilter<MyEntityEnum>("ENUM_FIELD")

        override val currentQuery: MyEntityFilterBuilder
            get() = this

        internal class SortOrder : FilterBuilder.SortOrder<SortOrder>() {

            val uuid = addFieldSorter("UUID")
            val parentUuid = addFieldSorter("PARENT_UUID")
            val price = addFieldSorter("PRICE_OUT")
            val alcoholProductKindCode = addFieldSorter("ALCOHOL_PRODUCT_KIND_CODE")
            val enumField = addFieldSorter("ENUM_FIELD")

            override val currentSortOrder: SortOrder
                get() = this

        }

        override fun getValue(cursor: Cursor<String>): String {
            return cursor.getString(1)
        }
    }

}