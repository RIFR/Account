package se.lexicon.account.component.domain;

import com.so4it.annotation.Allowed;
import com.so4it.common.util.object.Required;
import com.so4it.common.util.object.ValueObject;

import java.io.Serializable;

public class OrderBook extends ValueObject implements Serializable {

    String id;

    String instrument;

    Money minValue;
    Money maxValue;

    Phase phase = Phase.UNKNOWN;

    Boolean sellOrder; // Sell or Buy

    private OrderBook() {
    }

    private OrderBook (Builder builder) {
        this.id = Required.notNull(builder.id,"id");
        this.instrument = Required.notNull(builder.instrument,"Name");
        this.minValue = Required.notNull(builder.minValue,"minValue");
        this.maxValue = Required.notNull(builder.maxValue,"maxValue");
        this.phase = Required.notNull(builder.phase,"phase");
        this.sellOrder = Required.notNull(builder.sellOrder,"sellOrder");
    }

    @Override
    protected Object[] getIdFields() {
        return new Object[]{id,instrument,minValue,maxValue,phase};
    }

    @Allowed(value = "Auto generated by GS",types = {Allowed.Type.NULLABLE})
    public String getId() {
        return id;
    }

    public String getInstrument() {
        return instrument;
    }

//    private void setInstrument(String instrument) {
//        this.instrument = instrument;
//    }

    public Money getMinValue() {
        return minValue;
    }

//    private void setMinValue(Money minValue) {
//        this.minValue = minValue;
//    }

    public Money getMaxValue() {
        return maxValue;
    }

//    private void setMaxValue(Money maxValue) {
//        this.maxValue = maxValue;
//    }

    public Phase getPhase() {
        return phase;
    }

//    private void setPhase(Phase phase) {
//        this.phase = phase;
//    }

    public Boolean getSellOrder() {
        return sellOrder;
    }

//    private void setSellOrder(Boolean sellOrder) {
//        this.sellOrder = sellOrder;
//    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder implements com.so4it.common.builder.Builder<OrderBook>{

        String id;

        String instrument;

        Money minValue;
        Money maxValue;

        Phase phase;

        Boolean sellOrder;

        public Builder withId(String id){
            this.id = id;
            return this;
        }

        public Builder withInstrument(String instrument){
            this.instrument = instrument;
            return this;
        }

        public Builder withMinValue(Money minValue){
            this.minValue = minValue;
            return this;
        }

        public Builder withMaxValue(Money maxValue){
            this.maxValue = maxValue;
            return this;
        }

        public Builder withPhase(Phase phase){
            this.phase = phase;
            return this;
        }

        public Builder withSellOrder(Boolean sellOrder){
            this.sellOrder = sellOrder;
            return this;
        }

        @Override
        public OrderBook build() {
            return new OrderBook(this);
        }
    }
}