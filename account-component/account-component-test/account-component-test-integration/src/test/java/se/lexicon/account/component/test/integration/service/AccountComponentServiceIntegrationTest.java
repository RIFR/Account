package se.lexicon.account.component.test.integration.service;

import com.lexicon.account.component.domain.Account;
import com.lexicon.account.component.test.common.domain.AccountTestBuilder;
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
import se.lexicon.account.component.domain.Orders;
import se.lexicon.account.component.service.AccountComponentService;
import se.lexicon.account.component.service.OrderComponentService;

import java.math.BigDecimal;

/**
 * @author Magnus Poromaa {@literal <mailto:magnus.poromaa@so4it.com/>}
 */
@Category(IntegrationTest.class)
public class AccountComponentServiceIntegrationTest {

    @ClassRule
    public static final RuleChain SUITE_RULE_CHAIN = AccountComponentServiceIntegrationTestSuite.SUITE_RULE_CHAIN;
    public static final String SSN = "1111111111";

    @Rule
    public ClearGigaSpaceTestRule clearGigaSpaceTestRule = new ClearGigaSpaceTestRule(AccountComponentServiceIntegrationTestSuite.getExportContext().getBean(GigaSpace.class));

    @Test
    public void testCreatingAccountAndCheckBalanceIsCorrect() {

        AccountComponentService accountComponentService = AccountComponentServiceIntegrationTestSuite.getImportContext().getBean(AccountComponentService.class);
        OrderComponentService orderComponentService = AccountComponentServiceIntegrationTestSuite.getImportContext().getBean(OrderComponentService.class);



        //Create the account
        Account created = AccountTestBuilder.builder().withSsn(SSN).build();
        accountComponentService.createAccount(created);


        //Fetch the account
        Account fetched = accountComponentService.getAccount(SSN);

        //Assert the created and fetched accounts are the same (e.g. "equals")
        Assert.assertNotNull(fetched);
        Assert.assertEquals(created,fetched);


        orderComponentService.placeOrder(OrderTestBuilder.builder().withSsn(SSN).withAmount(BigDecimal.ONE).build());
        orderComponentService.placeOrder(OrderTestBuilder.builder().withSsn(SSN).withAmount(BigDecimal.TEN).build());



        Orders orders = orderComponentService.getOrders(SSN);
        Assert.assertEquals(2, orders.size());
        Assert.assertEquals(BigDecimal.valueOf(11.0), orderComponentService.getTotalOrderValueOfAllAccounts());




    }


    @Test
    public void testGettingTotalAmount() {
        AccountComponentService accountComponentService = AccountComponentServiceIntegrationTestSuite.getImportContext().getBean(AccountComponentService.class);
        accountComponentService.createAccount(AccountTestBuilder.builder().withSsn("account2").withAmount(BigDecimal.valueOf(100)).build());
        accountComponentService.createAccount(AccountTestBuilder.builder().withSsn("account1").withAmount(BigDecimal.valueOf(100)).build());
        Assert.assertEquals(BigDecimal.valueOf(200.0), accountComponentService.getTotalAmountOnAccounts());
    }

}
