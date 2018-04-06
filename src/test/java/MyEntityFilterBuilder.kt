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