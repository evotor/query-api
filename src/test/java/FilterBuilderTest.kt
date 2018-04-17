import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
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
                .uuid.like("Молоко_40/%", '/')
                .and().alcoholProductKindCode.equal(null)
                .and().innerFilterBuilder.innerField.greater("123")
                .and().innerFilterBuilder.innerField2.lower(123)
                .and().innerFilterBuilder.innerField3.innerInnerField.lower("123")
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
                        .parentUuid.asc()
                        .innerFilterBuilder.innerField.asc()
                        .innerFilterBuilder.innerField2.desc())
                .limit(10)
        var myEntityQuery = myEntityExecutor.selection.toString()
        myEntityQuery += myEntityExecutor.sortOrderValue + myEntityExecutor.limitValue
        println(myEntityQuery)
        println(Arrays.toString(myEntityExecutor.selectionArgs.toTypedArray()))
        Assert.assertEquals(
                "UUID LIKE ? ESCAPE '/' AND ALCOHOL_PRODUCT_KIND_CODE IS NULL AND INNER_FIELD>? AND INNER_FIELD2<? AND INNER_INNER_FIELD<? OR (PRICE_OUT IS NULL OR PRICE_OUT IN (?,?,?) AND PARENT_UUID IS NOT NULL AND (ALCOHOL_PRODUCT_KIND_CODE>=? AND ENUM_FIELD=?)) AND PRICE_OUT<?UUID ASC,ENUM_FIELD DESC,PARENT_UUID ASC,INNER_FIELD ASC,INNER_FIELD2 DESC LIMIT 10",
                myEntityQuery
        )
    }

    @Test
    fun testLike() {
        Assert.assertEquals(
                "UUID LIKE ?",
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
                "PRICE_OUT ASC,ALCOHOL_PRODUCT_KIND_CODE DESC",
                MyEntityFilterBuilder()
                        .uuid.equal("someUuid")
                        .sortOrder(MyEntityFilterBuilder.SortOrder()
                                .price.asc()
                                .alcoholProductKindCode.desc())
                        .sortOrderValue
        )

        Assert.assertEquals(
                "PRICE_OUT ASC,ALCOHOL_PRODUCT_KIND_CODE DESC,ENUM_FIELD ASC",
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

}