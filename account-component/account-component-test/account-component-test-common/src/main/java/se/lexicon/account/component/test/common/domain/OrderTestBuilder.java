package se.lexicon.account.component.test.common.domain;

import se.lexicon.account.component.domain.*;
import com.so4it.common.util.object.Required;
import com.so4it.test.domain.AbstractTestBuilder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

/**
 * @author Magnus Poromaa {@literal <mailto:magnus.poromaa@so4it.com/>}
 */
public class OrderTestBuilder extends AbstractTestBuilder<Order> {

    private Order.Builder builder;

    public OrderTestBuilder(Order.Builder builder) {
        this.builder = Required.notNull(builder, "builder");
        this.builder
                //.withId("1111111111")
                .withSsn("1111111111")
                .withAmount(BigDecimal.TEN)
                .withInsertionTimestamp(Instant.now())
                .withOrderBookId (OrderBooks.valueOf(
                        new OrderBook.Builder()
                            //.withId("1111111111")
                            .withInstrument("ABB")
                            .withNoOfItems(100)
                            .withSellOrder(false)
                            .withPhase(Phase.UNKNOWN)
                            .withValue(Money.builder()
                                .withAmount((BigDecimal.valueOf(500d)))
                                .withCurrency(Currency.getInstance("SEK"))
                                .build())
                            .build()));
    }

    public static OrderTestBuilder builder() {
        return new OrderTestBuilder(Order.builder());
    }


    public OrderTestBuilder withId(String id){
        builder.withId(id);
        return this;
    }

    public OrderTestBuilder withSsn(String ssn){
        builder.withSsn(ssn);
        return this;
    }

    public OrderTestBuilder withAmount(BigDecimal amount){
        builder.withAmount(amount);
        return this;
    }

    public OrderTestBuilder withInsertionTimestamp(Instant insertionTimestamp){
        builder.withInsertionTimestamp(insertionTimestamp);
        return this;
    }

    public OrderTestBuilder withOrderBookId(OrderBooks orderBookId){
        builder.withOrderBookId(orderBookId);
        return this;
    }


    @Override
    public Order build() {
        return builder.build();
    }
}
