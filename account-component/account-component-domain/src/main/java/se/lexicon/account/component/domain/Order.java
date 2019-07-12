package se.lexicon.account.component.domain;

import com.so4it.common.util.object.Required;
import com.so4it.common.util.object.ValueObject;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Magnus Poromaa {@literal <mailto:magnus.poromaa@so4it.com/>}
 */
public class Order extends ValueObject implements Serializable {

    private static final long serialVersionUID = 2L;

    private String orderBookId;

    private BigDecimal amount;

    private Integer noOfItems;

    private Order() {
    }

    private Order(Builder builder) {

        this.orderBookId = Required.notNull(builder.orderBookId,"orderBookId");
        this.amount = Required.notNull(builder.amount,"amount");
        this.noOfItems = Required.notNull(builder.noOfItems,"noOfItems");
    }

    public String getOrderBookId() {
        return orderBookId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Integer getNoOfItems() {
        return noOfItems;
    }

    @Override
    protected Object[] getIdFields() {
        return new Object[]{orderBookId,amount,noOfItems};
    }

    public static Builder builder(){
        return new Builder();
    }



    public static class Builder implements com.so4it.common.builder.Builder<Order>{

        private String orderBookId;

        private BigDecimal amount;

        private Integer noOfItems;

        public Builder withOrderBookId(String orderBookId){
            this.orderBookId = orderBookId;
            return this;
        }


        public Builder withAmount(BigDecimal amount){
            this.amount = amount;
            return this;
        }

        public Builder withNoOfItems(Integer noOfItems){
            this.noOfItems = noOfItems;
            return this;
        }


        @Override
        public Order build() {
            return new Order(this);
        }
    }
}
