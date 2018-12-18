import android.content.Context
import android.net.Uri
import ru.evotor.query.Cursor
import ru.evotor.query.FilterBuilder
import java.math.BigDecimal

internal class MyEntityFilterBuilder : FilterBuilder<MyEntityFilterBuilder, MyEntityFilterBuilder.SortOrder, String>(Uri.parse("http://google.com")) {

    val uuid = addFieldFilter<String?>("UUID")
    val parentUuid = addFieldFilter<String?>("PARENT_UUID")
    val price = addFieldFilter<BigDecimal?, Int?>("PRICE_OUT", { it?.toInt()?.times(100) })
    val alcoholProductKindCode = addFieldFilter<Long?, Int?>("ALCOHOL_PRODUCT_KIND_CODE", { it?.toInt()?.times(10) })
    val enumField = addFieldFilter<MyEntityEnum>("ENUM_FIELD")
    val name = addFieldFilter<String?, String?>("NAME", {
        // Конвертируем пустую строку в null
        it?.takeIf { it.isNotBlank() }
    })
    val notNullName = addFieldFilter<String?, String?>("NAME_NOT_NULL", {
        // Конвертируем null в пустую строку
        it ?: ""
    })
    class IIFB: Inner<MyEntityFilterBuilder, MyEntityFilterBuilder.SortOrder, String>() {
        val innerInnerField = addFieldFilter<String>("INNER_INNER_FIELD")
    }
    class IFB : Inner<MyEntityFilterBuilder, MyEntityFilterBuilder.SortOrder, String>() {
        val innerField = addFieldFilter<String>("INNER_FIELD")
        val innerField2 = addFieldFilter<Int?, String?>("INNER_FIELD2", {it?.toString()})
        val innerField3 = addInnerFilterBuilder(IIFB())
    }
    val innerFilterBuilder = addInnerFilterBuilder(IFB())

    internal class SortOrder : FilterBuilder.SortOrder<SortOrder>() {

        val uuid = addFieldSorter("UUID")
        val parentUuid = addFieldSorter("PARENT_UUID")
        val price = addFieldSorter("PRICE_OUT")
        val alcoholProductKindCode = addFieldSorter("ALCOHOL_PRODUCT_KIND_CODE")
        val enumField = addFieldSorter("ENUM_FIELD")
        class ISO : Inner.SortOrder<SortOrder>() {
            val innerField = addFieldSorter("INNER_FIELD")
            val innerField2 = addFieldSorter("INNER_FIELD2")
            val innerField3 = addFieldSorter("INNER_FIELD3")
        }
        val innerFilterBuilder = addInnerSortOrder(ISO())

    }

    override fun getValue(context: Context, cursor: Cursor<String>): String {
        return cursor.getString(1)
    }
}