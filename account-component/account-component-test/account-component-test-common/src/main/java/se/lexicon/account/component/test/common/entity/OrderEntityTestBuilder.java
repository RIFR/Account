package se.lexicon.account.component.test.common.entity;

import se.lexicon.account.component.entity.OrderEntity;
import com.so4it.common.util.object.Required;
import com.so4it.test.domain.AbstractTestBuilder;

import java.math.BigDecimal;

/**
 * @author Magnus Poromaa {@literal <mailto:magnus.poromaa@so4it.com/>}
 */
public class OrderEntityTestBuilder extends AbstractTestBuilder<OrderEntity> {

    private OrderEntity.Builder builder;


    public OrderEntityTestBuilder(OrderEntity.Builder builder) {
        this.builder = Required.notNull(builder, "builder");
        this.builder
                .withId("1111111111-1")
                .withSsn("1111111111")
                .withOrderBookId("testBookEntity")
                .withAmount(BigDecimal.TEN)
                .withNoOfItems(10);
    }

    public OrderEntityTestBuilder withAmount(BigDecimal amount){
        this.builder.withAmount(amount);
        return this;
    }

    public OrderEntityTestBuilder withSsn(String ssn){
        this.builder.withId(ssn+"-1");
        this.builder.withSsn(ssn);
        return this;
    }

    public OrderEntityTestBuilder withNoOfItems(Integer noOfItems){
        this.builder.withNoOfItems(noOfItems);
        return this;
    }

    public static OrderEntityTestBuilder builder() {
        return new OrderEntityTestBuilder(OrderEntity.builder());
    }

    @Override
    public OrderEntity build() {
        return builder.build();
    }
}
