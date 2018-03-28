import android.net.Uri
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import ru.evotor.query.Cursor
import ru.evotor.query.FilterBuilder
import java.math.BigDecimal

/**
 * Created by a.lunkov on 06.03.2018.
 */
@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class FilterBuilderTest {
    @Test
    fun testMyEntityFilterBuilder() {
        Assert.assertEquals(
                "UUID=\"someUuid\" AND ALCOHOL_PRODUCT_KIND_CODE IS NULL OR (PRICE_OUT IN (1,2,3) AND PARENT_UUID IS NOT NULL AND (ALCOHOL_PRODUCT_KIND_CODE>=10 AND ENUM_FIELD=\"FIRST_VALUE\")) AND PRICE_OUT<4",
                MyEntityFilterBuilder()
                        .uuid.equal("someUuid")
                        .and().alcoholProductKindCode.equal(null)
                        .or(
                                MyEntityFilterBuilder()
                                        .price.inside(arrayOf(BigDecimal(1), BigDecimal(2), BigDecimal(3)))
                                        .and().parentUuid.notEqual(null)
                                        .and(
                                                MyEntityFilterBuilder()
                                                        .alcoholProductKindCode.greater(10, true)
                                                        .and().enumField.equal(MyEntityEnum.FIRST_VALUE)
                                        )
                        ).and().price.lower(BigDecimal.valueOf(4), false)
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

        val uuid = addFieldFilter<String>("UUID")
        val parentUuid = addFieldFilter<String?>("PARENT_UUID")
        val price = addFieldFilter<BigDecimal>("PRICE_OUT")
        val alcoholProductKindCode = addFieldFilter<Long?>("ALCOHOL_PRODUCT_KIND_CODE")
        val enumField = addFieldFilter<MyEntityEnum>("ENUM_FIELD")

        override val currentQuery: MyEntityFilterBuilder
            get() = this

        class SortOrder : FilterBuilder.SortOrder<SortOrder>() {

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