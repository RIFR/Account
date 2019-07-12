package com.seb.account.component.test.integration.dao;

import se.lexicon.account.component.entity.OrderEntity;
import se.lexicon.account.component.test.common.entity.OrderEntityTestBuilder;
import com.seb.account.component.test.integration.service.AccountComponentServiceIntegrationTestSuite;
import se.lexicon.account.componment.dao.OrderDao;
import com.so4it.test.category.IntegrationTest;
import com.so4it.test.gs.rule.ClearGigaSpaceTestRule;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.RuleChain;
import org.openspaces.core.GigaSpace;

/**
 * @author Magnus Poromaa {@literal <mailto:magnus.poromaa@so4it.com/>}
 */
@Category(IntegrationTest.class)
public class OrderComponentDaoIntegrationTest {

    @ClassRule
    public static final RuleChain SUITE_RULE_CHAIN = AccountComponentDaoIntegrationTestSuite.SUITE_RULE_CHAIN;

    @Rule
    public ClearGigaSpaceTestRule clearGigaSpaceTestRule = new ClearGigaSpaceTestRule(AccountComponentServiceIntegrationTestSuite.getExportContext().getBean(GigaSpace.class));

    @Test
    public void testInsertingOrder(){
        OrderDao accountDao = AccountComponentDaoIntegrationTestSuite.getExportContext().getBean(OrderDao.class);
        OrderEntity accountEntity = accountDao.insert(OrderEntityTestBuilder.builder().build());
    }

}
