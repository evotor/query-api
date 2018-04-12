import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class UnionTest {
    @Test
    fun testUnionOfNoFilters() {
        MyEntityFilterBuilder()
                .noFilters()
                .or(MyEntityFilterBuilder().noFilters())
                .let {
                    Assert.assertEquals("", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String>(), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testUnionOfNoFilterAndLike() {
        MyEntityFilterBuilder()
                .noFilters()
                .or(MyEntityFilterBuilder().name.like("Test"))
                .let {
                    Assert.assertEquals("NAME LIKE ?", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String>("Test"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testUnionOfLikeAndNoFilter() {
        MyEntityFilterBuilder()
                .name.like("Test")
                .or(
                        MyEntityFilterBuilder()
                                .noFilters()
                )
                .let {
                    Assert.assertEquals("NAME LIKE ?", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String>("Test"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }
}