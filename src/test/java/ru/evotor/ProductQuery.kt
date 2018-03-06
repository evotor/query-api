package ru.evotor

import android.net.Uri
import java.math.BigDecimal


/**
 * Created by a.lunkov on 27.02.2018.
 */

class ProductQuery : FilterBuilder<ProductQuery, ProductQuery.SortOrder, String?>(Uri.EMPTY) {

    @JvmField
    val uuid = addFieldFilter<String>("UUID")
    @JvmField
    val parentUuid = addFieldFilter<String?>("PARENT_UUID")
    @JvmField
    val code = addFieldFilter<String?>("CODE")
    @JvmField
    val name = addFieldFilter<String>("NAME")
    @JvmField
    val taxNumber = addFieldFilter<String>("TAX_NUMBER")
    @JvmField
    val type = addFieldFilter<String>("TYPE")
    @JvmField
    val price = addFieldFilter<BigDecimal>("PRICE_OUT")
    @JvmField
    val quantity = addFieldFilter<BigDecimal>("QUANTITY")
    @JvmField
    val description = addFieldFilter<String?>("DESCRIPTION")
    @JvmField
    val measureName = addFieldFilter<String>("MEASURE_NAME")
    @JvmField
    val measurePrecision = addFieldFilter<Int>("MEASURE_PRECISION")
    @JvmField
    val alcoholByVolume = addFieldFilter<BigDecimal?>("ALCOHOL_BY_VOLUME")
    @JvmField
    val alcoholProductKindCode = addFieldFilter<Long?>("ALCOHOL_PRODUCT_KIND_CODE")
    @JvmField
    val tareVolume = addFieldFilter<BigDecimal?>("TARE_VOLUME")

    override val currentQuery: ProductQuery
        get() = this

    class SortOrder : FilterBuilder.SortOrder<SortOrder>() {

        @JvmField
        val uuid = addFieldSorter("UUID")
        @JvmField
        val parentUuid = addFieldSorter("PARENT_UUID")
        @JvmField
        val code = addFieldSorter("CODE")
        @JvmField
        val name = addFieldSorter("NAME")
        @JvmField
        val taxNumber = addFieldSorter("TAX_NUMBER")
        @JvmField
        val type = addFieldSorter("TYPE")
        @JvmField
        val price = addFieldSorter("PRICE")
        @JvmField
        val quantity = addFieldSorter("QUANTITY")
        @JvmField
        val description = addFieldSorter("DESCRIPTION")
        @JvmField
        val measureName = addFieldSorter("MEASURE_NAME")
        @JvmField
        val measurePrecision = addFieldSorter("MEASURE_PRECISION")
        @JvmField
        val alcoholByVolume = addFieldSorter("ALCOHOL_BY_VOLUME")
        @JvmField
        val alcoholProductKindCode = addFieldSorter("ALCOHOL_PRODUCT_KIND_CODE")
        @JvmField
        val tareVolume = addFieldSorter("TARE_VOLUME")

        override val currentSortOrder: SortOrder
            get() = this

    }

    override fun getValue(cursor: Cursor<String?>): String? {
        return null
    }

}
