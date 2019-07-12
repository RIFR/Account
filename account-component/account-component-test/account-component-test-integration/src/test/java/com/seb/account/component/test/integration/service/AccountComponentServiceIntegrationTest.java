package com.seb.account.component.test.integration.service;

import se.lexicon.account.component.domain.Account;
import se.lexicon.account.component.test.common.domain.AccountTestBuilder;
import se.lexicon.account.component.test.common.domain.OrderTestBuilder;
import com.so4it.test.category.IntegrationTest;
import com.so4it.test.common.Assert;
import com.so4it.test.gs.rule.ClearGigaSpaceTestRule;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.RuleChain;
import org.openspaces.core.GigaSpace;
import se.lexicon.account.component.service.AccountComponentService;

import java.math.BigDecimal;

/**
 * @author Magnus Poromaa {@literal <mailto:magnus.poromaa@so4it.com/>}
 */
@Category(IntegrationTest.class)
public class AccountComponentServiceIntegrationTest {

    @ClassRule
    public static final RuleChain SUITE_RULE_CHAIN = AccountComponentServiceIntegrationTestSuite.SUITE_RULE_CHAIN;

    @Rule
    public ClearGigaSpaceTestRule clearGigaSpaceTestRule = new ClearGigaSpaceTestRule(AccountComponentServiceIntegrationTestSuite.getExportContext().getBean(GigaSpace.class));

    @Test
    public void testCreatingAccount() {
        AccountComponentService accountComponentService = AccountComponentServiceIntegrationTestSuite.getImportContext().getBean(AccountComponentService.class);
        accountComponentService.createAccount(AccountTestBuilder.builder().withOrders(OrderTestBuilder.builder().build()).build());
        Account account = accountComponentService.getAccount(AccountTestBuilder.builder().build().getSsn());
        Assert.assertNotNull(account);
        Assert.assertEquals(1, account.getOrders().size());

        accountComponentService.createAccount(AccountTestBuilder.builder().withSsn("account2").withAmount(BigDecimal.valueOf(200)).build());
        Account account2 = accountComponentService.getAccount("account2");
        Assert.assertEquals(0, account2.getOrders().size());

        accountComponentService.createAccount(AccountTestBuilder.builder().withSsn("account3").withAmount(BigDecimal.valueOf(300))
                .withOrders(OrderTestBuilder.builder().build(),OrderTestBuilder.builder()
                        .withOrderBookId("Book3").withAmount(BigDecimal.TEN).withNoOfItems(10).build()).build());
        Account account3 = accountComponentService.getAccount("account3");
        Assert.assertEquals(2, account3.getOrders().size());

    }


    @Test
    public void testPlaceOrder() {
        AccountComponentService accountComponentService = AccountComponentServiceIntegrationTestSuite.getImportContext().getBean(AccountComponentService.class);

        accountComponentService.createAccount(AccountTestBuilder.builder().build());
        accountComponentService.placeOrder(AccountTestBuilder.builder().build().getSsn(), OrderTestBuilder.builder().withOrderBookId("firstBook").build());

        Account account = accountComponentService.getAccount(AccountTestBuilder.builder().build().getSsn());
        Assert.assertNotNull(account);
        Assert.assertEquals(9, account.getAmount().intValue());
        Assert.assertEquals(1, account.getOrders().size());

        accountComponentService.placeOrder(AccountTestBuilder.builder().build().getSsn(),
                OrderTestBuilder.builder().withAmount(BigDecimal.valueOf(2d)).withOrderBookId("otherBook").build());
        account = accountComponentService.getAccount(AccountTestBuilder.builder().build().getSsn());
        Assert.assertEquals(7, account.getAmount().intValue());
        Assert.assertEquals(2, account.getOrders().size());
    }


    @Test
    public void testGettingTotalAmount() {
        AccountComponentService accountComponentService = AccountComponentServiceIntegrationTestSuite.getImportContext().getBean(AccountComponentService.class);
        accountComponentService.createAccount(AccountTestBuilder.builder().withSsn("account2").withAmount(BigDecimal.valueOf(100)).build());
        accountComponentService.createAccount(AccountTestBuilder.builder().withSsn("account1").withAmount(BigDecimal.valueOf(100)).build());
        Assert.assertEquals(BigDecimal.valueOf(200.0), accountComponentService.getTotalAmountOnAccounts());
    }

}
