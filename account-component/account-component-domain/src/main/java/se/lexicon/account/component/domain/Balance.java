package se.lexicon.account.component.domain;

import com.so4it.common.domain.DomainClass;
import com.so4it.common.util.object.Required;
import com.so4it.common.util.object.ValueObject;

import java.math.BigDecimal;
import java.util.Currency;

/**
 *
 * @author Magnus Poromaa {@literal <mailto:magnus.poromaa@so4it.com/>}
 */
@DomainClass
public class Balance extends ValueObject {

    private String address;

    private Phase phase;

    private Money amount;


    private Balance() {
    }

    private Balance(Builder builder) {
        this.address = Required.notNull(builder.address,"address");
        this.phase = Required.notNull(builder.phase,"phase");
        this.amount = Required.notNull(builder.amount,"amount");
    }

    public String getAddress() {
        return address;
    }

    public Phase getPhase() {
        return phase;
    }

    public Money getAmount() {
        return amount;
    }

    @Override
    protected Object[] getIdFields() {
        return new Object[]{address,phase,amount};
    }

    public static Builder builder(){
        return new Builder();
    }


    public static class Builder implements com.so4it.common.builder.Builder<Balance>{

        private String address;

        private Phase phase;

        private Money amount;

        public Builder withAddress(String address){
            this.address = address;
            return this;
        }

        public Builder withPhase(Phase phase){
            this.phase = phase;
            return this;
        }

        public Builder withAmount(Money amount){
            this.amount = amount;
            return this;
        }

        @Override
        public Balance build() {
            return new Balance(this);
        }
    }

}
