package se.lexicon.account.component.entity;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceRouting;
import com.so4it.annotation.Allowed;
import com.so4it.common.util.object.Required;
import com.so4it.component.entity.AbstractEntityBuilder;
import com.so4it.component.entity.IdEntity;
import se.lexicon.account.component.domain.Money;
import se.lexicon.account.component.domain.OrderBooks;
import se.lexicon.account.component.domain.Phase;

import java.math.BigDecimal;

/**
 *
 * @author Magnus Poromaa {@literal <mailto:magnus.poromaa@so4it.com/>}
 */
@SpaceClass
public class OrderBookEntity extends IdEntity<String> {

    @Allowed(value = "Auto generated by GS", types = {Allowed.Type.NULLABLE})
    private String id;
    private String orderId;
    private String ssn;

    String instrument;

    Money minValue;
    Money maxValue;

    Phase phase = Phase.UNKNOWN;

    Boolean sellOrder; // Sell or Buy

    private OrderBookEntity() {
    }

    private OrderBookEntity(Builder builder) {
        this.id = builder.id;
        this.orderId = Required.notNull(builder.orderId,"orderId",builder.isTemplate());
        this.ssn = Required.notNull(builder.ssn,"ssn",builder.isTemplate());
        this.instrument = Required.notNull(builder.instrument,"instrument",builder.isTemplate());
        this.minValue = Required.notNull(builder.minValue,"minValue",builder.isTemplate());
        this.maxValue = Required.notNull(builder.maxValue,"maxValue",builder.isTemplate());
        this.phase = Required.notNull(builder.phase,"phase",builder.isTemplate());
        this.sellOrder = Required.notNull(builder.sellOrder,"sellOrder",builder.isTemplate());
    }

    @Override
    @SpaceId(autoGenerate = true)
    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    private void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @SpaceRouting
    public String getSsn() {
        return ssn;
    }

    private void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getInstrument() {
        return instrument;
    }

    private void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public Money getMinValue() {
        return minValue;
    }

    private void setMinValue(Money minValue) {
        this.minValue = minValue;
    }

    public Money getMaxValue() {
        return maxValue;
    }

    private void setMaxValue(Money maxValue) {
        this.maxValue = maxValue;
    }

    public Phase getPhase() {
        return phase;
    }

    private void setPhase(Phase phase) {
        this.phase = phase;
    }

    public Boolean getSellOrder() {
        return sellOrder;
    }

    private void setSellOrder(Boolean sellOrder) {
        this.sellOrder = sellOrder;
    }

    public static Builder builder() {
        return new Builder(false);
    }

    public static Builder templateBuilder() {
        return new Builder(true);
    }


    public static class Builder extends AbstractEntityBuilder<OrderBookEntity> {

        private String id;

        private String orderId;

        //The arrangement id of this account balance
        private String ssn;

        String instrument;

        Money minValue;
        Money maxValue;

        Phase phase = Phase.UNKNOWN;

        Boolean sellOrder; // Sell or Buy

        public Builder(boolean template) {
            super(template);
        }

        public OrderBookEntity.Builder withId(String id) {
            this.id = id;
            return this;
        }

        public OrderBookEntity.Builder withOrderId(String orderId) {
            this.orderId = orderId;
            return this;
        }

        public OrderBookEntity.Builder withSsn(String ssn) {
            this.ssn = ssn;
            return this;
        }

        public OrderBookEntity.Builder withInstrument(String instrument) {
            this.instrument = instrument;
            return this;
        }

        public OrderBookEntity.Builder withMinValue(Money minValue) {
            this.minValue = minValue;
            return this;
        }

        public OrderBookEntity.Builder withMaxValue(Money maxValue) {
            this.maxValue = maxValue;
            return this;
        }

        public OrderBookEntity.Builder withPhase(Phase phase) {
            this.phase = phase;
            return this;
        }

        public OrderBookEntity.Builder withSellOrder(Boolean sellOrder) {
            this.sellOrder = sellOrder;
            return this;
        }

        @Override
        public OrderBookEntity build() {
            return new OrderBookEntity(this);
        }
    }
}
