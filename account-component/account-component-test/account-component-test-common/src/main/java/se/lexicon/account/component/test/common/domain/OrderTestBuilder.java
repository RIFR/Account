package se.lexicon.account.component.test.common.domain;

import se.lexicon.account.component.domain.Order;
import com.so4it.common.util.object.Required;
import com.so4it.test.domain.AbstractTestBuilder;

import java.math.BigDecimal;

/**
 * @author Magnus Poromaa {@literal <mailto:magnus.poromaa@so4it.com/>}
 */
public class OrderTestBuilder extends AbstractTestBuilder<Order> {

    private Order.Builder builder;


    public OrderTestBuilder(Order.Builder builder) {
        this.builder = Required.notNull(builder, "builder");
        this.builder
                .withOrderBookId("testBookDomain")
                .withAmount(BigDecimal.ONE)
                .withNoOfItems(100);
    }

    public OrderTestBuilder withOrderBookId(String orderBookId){
        builder.withOrderBookId(orderBookId);
        return this;
    }

    public OrderTestBuilder withAmount(BigDecimal amount){
        builder.withAmount(amount);
        return this;
    }

    public OrderTestBuilder withNoOfItems(Integer noOfItems){
        builder.withNoOfItems(noOfItems);
        return this;
    }

    public static OrderTestBuilder builder() {
        return new OrderTestBuilder(Order.builder());
    }

    @Override
    public Order build() {
        return builder.build();
    }
}
