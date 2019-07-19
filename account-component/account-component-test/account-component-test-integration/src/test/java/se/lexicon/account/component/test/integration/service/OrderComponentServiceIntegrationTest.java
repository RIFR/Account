package se.lexicon.account.component.test.integration.service;

import com.so4it.test.category.IntegrationTest;
import com.so4it.test.common.Assert;
import com.so4it.test.gs.rule.ClearGigaSpaceTestRule;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.RuleChain;
import org.openspaces.core.GigaSpace;
import se.lexicon.account.component.domain.Order;
import se.lexicon.account.component.domain.Orders;
import se.lexicon.account.component.service.OrderComponentService;
import se.lexicon.account.component.test.common.domain.OrderTestBuilder;

import java.math.BigDecimal;

/**
 * @author Magnus Poromaa {@literal <mailto:magnus.poromaa@so4it.com/>}
 */
@Category(IntegrationTest.class)
public class OrderComponentServiceIntegrationTest {

    @ClassRule
    public static final RuleChain SUITE_RULE_CHAIN = AccountComponentServiceIntegrationTestSuite.SUITE_RULE_CHAIN;

    @Rule
    public ClearGigaSpaceTestRule clearGigaSpaceTestRule = new ClearGigaSpaceTestRule(AccountComponentServiceIntegrationTestSuite.getExportContext().getBean(GigaSpace.class));

    @Test
    public void testOrderComponentServiceExists() {
        OrderComponentService orderComponentService = AccountComponentServiceIntegrationTestSuite.getImportContext().getBean(OrderComponentService.class);
        Assert.assertNotNull(orderComponentService);
    }

    @Test
    public void testPlaceOrder() {
        OrderComponentService orderComponentService = AccountComponentServiceIntegrationTestSuite.getImportContext().getBean(OrderComponentService.class);

        Order order = OrderTestBuilder.builder().withSsn("111111").withAmount(BigDecimal.ONE).build();
        orderComponentService.placeOrder(order);
        Orders orders = orderComponentService.getOrders("111111");

        Assert.assertEquals(1, orders.size());
        Assert.assertEquals(order, orders.getFirst());
    }

    @Test
    public void testGetAllOrderComponent() {
        OrderComponentService orderComponentService = AccountComponentServiceIntegrationTestSuite.getImportContext().getBean(OrderComponentService.class);

        orderComponentService.placeOrder(OrderTestBuilder.builder().withSsn("111111").withAmount(BigDecimal.ONE).build());
        orderComponentService.placeOrder(OrderTestBuilder.builder().withSsn("111111").withAmount(BigDecimal.TEN).build());

        orderComponentService.placeOrder(OrderTestBuilder.builder().withSsn("222222").withAmount(BigDecimal.ONE).build());
        orderComponentService.placeOrder(OrderTestBuilder.builder().withSsn("222222").withAmount(BigDecimal.TEN).build());

        Orders orders = orderComponentService.getOrders("111111");
        Assert.assertEquals(2, orders.size());
        //Assert.assertEquals(11, orders.asStream().forEach(item -> item.getAmount().c));

        Assert.assertEquals(BigDecimal.valueOf(22.0), orderComponentService.getTotalOrderValueOfAllAccounts());

    }

}
