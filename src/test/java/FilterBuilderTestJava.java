import android.net.Uri;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.math.BigDecimal;

import kotlin.jvm.functions.Function1;
import ru.evotor.query.Cursor;
import ru.evotor.query.FieldFilter;
import ru.evotor.query.FieldSorter;
import ru.evotor.query.FilterBuilder;

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class FilterBuilderTestJava {

    @Test
    public void testMyEntityFilterBuilder() {
        new MyEntityFilterBuilder()
                .uuid.equal(new BigDecimal(1))
                .sortOrder(new MyEntityFilterBuilder.SortOrder()
                        .uuid.asc());
    }

    static class MyEntityFilterBuilder extends FilterBuilder<MyEntityFilterBuilder, MyEntityFilterBuilder.SortOrder, String> {

        public MyEntityFilterBuilder() {
            super(Uri.parse("http://google.com"));
        }

        private interface TypeConverter extends Function1<BigDecimal, Integer> {
            Integer invoke(BigDecimal source);
        }

        FieldFilter<BigDecimal, Integer, MyEntityFilterBuilder, SortOrder, String> uuid = addFieldFilter(
                "UUID",
                new TypeConverter() {
                    @Override
                    public Integer invoke(BigDecimal source) {
                        return source.intValue() * 100;
                    }
                }
        );

        @Override
        protected MyEntityFilterBuilder getCurrentQuery() {
            return this;
        }

        @Override
        protected String getValue(@NotNull Cursor<? extends String> cursor) {
            return null;
        }


        static class SortOrder extends FilterBuilder.SortOrder<SortOrder> {

            FieldSorter<SortOrder> uuid = addFieldSorter("UUID");

            @NotNull
            @Override
            protected SortOrder getCurrentSortOrder() {
                return this;
            }

        }

    }


}
