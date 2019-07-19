package se.lexicon.account.component.test.common.entity;

import com.so4it.common.util.object.Required;
import com.so4it.test.domain.AbstractTestBuilder;
import se.lexicon.account.component.domain.Money;
import se.lexicon.account.component.domain.Phase;
import se.lexicon.account.component.entity.OrderBookEntity;
import se.lexicon.account.component.entity.OrderEntity;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * @author Magnus Poromaa {@literal <mailto:magnus.poromaa@so4it.com/>}
 */
public class OrderBookEntityTestBuilder extends AbstractTestBuilder<OrderBookEntity> {

    private OrderBookEntity.Builder builder;


    public OrderBookEntityTestBuilder(OrderBookEntity.Builder builder) {
        this.builder = Required.notNull(builder, "builder");
        this.builder
                .withId("1111111111")
                .withOrderId("1111111111")
                .withSsn("1111111111")
                .withMinValue(Money.builder().withAmount(BigDecimal.ONE).withCurrency(Currency.getInstance("SWE")).build())
                .withMaxValue(Money.builder().withAmount(BigDecimal.TEN).withCurrency(Currency.getInstance("SWE")).build())
                .withInstrument("ABB")
                .withPhase(Phase.UNKNOWN)
                .withSellOrder(false)
                .build();
    }

    public OrderBookEntityTestBuilder withSsn(String ssn){
        builder.withSsn(ssn);
        return this;
    }

    public OrderBookEntityTestBuilder withInstrument(String instrument){
        builder.withInstrument(instrument);
        return this;
    }

    public static OrderBookEntityTestBuilder builder() {
        return new OrderBookEntityTestBuilder(OrderBookEntity.builder());
    }

    @Override
    public OrderBookEntity build() {
        return builder.build();
    }
}
