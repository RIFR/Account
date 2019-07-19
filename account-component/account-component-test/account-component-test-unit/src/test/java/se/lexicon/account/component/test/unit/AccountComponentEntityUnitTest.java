package se.lexicon.account.component.test.unit;

import com.so4it.serialization.jackson.ObjectMapperFactory;
import com.so4it.test.builder.domain.DomainMatchers;
import com.so4it.test.builder.entity.EntityMatchers;
import com.so4it.test.category.UnitTest;
import com.so4it.test.domain.TestBuilderExecutor;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(UnitTest.class)
public class AccountComponentEntityUnitTest {

    private static final String PACKAGE_NAME = "se.lexicon.account.component.test.common.entity";

    @Test
    public void testDomainCompliance() {

        TestBuilderExecutor.execute(PACKAGE_NAME, EntityMatchers.getMatchers());
    }
}
