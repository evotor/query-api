import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class IntersectionTest {
    @Test
    fun testIntersectionOfNoFilters() {
        MyEntityFilterBuilder()
                .noFilters()
                .and(MyEntityFilterBuilder().noFilters())
                .let {
                    Assert.assertEquals("", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String>(), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testIntersectionOfNoFilterAndLike() {
        MyEntityFilterBuilder()
                .noFilters()
                .and(MyEntityFilterBuilder().name.like("Test"))
                .let {
                    Assert.assertEquals("NAME LIKE ?", it.selection.toString())
                    Assert.assertEquals(arrayListOf<String>("Test"), it.selectionArgs)
                    Assert.assertEquals("", it.limitValue)
                    Assert.assertEquals("", it.sortOrderValue)
                }
    }

    @Test
    fun testIntersectionOfLikeAndNoFilter() {
        MyEntityFilterBuilder()
                .name.like("Test")
                .and(
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