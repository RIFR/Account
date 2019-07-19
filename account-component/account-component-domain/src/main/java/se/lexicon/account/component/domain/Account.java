package com.lexicon.account.component.domain;

import com.so4it.annotation.Allowed;
import com.so4it.common.domain.DomainClass;
import com.so4it.common.util.object.Required;
import com.so4it.common.util.object.ValueObject;
import se.lexicon.account.component.domain.Order;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Magnus Poromaa {@literal <mailto:magnus.poromaa@so4it.com/>}
 */
@DomainClass
public class Account extends ValueObject {

    private static final long serialVersionUID = 2L;

    private String ssn;

    private BigDecimal amount;

    private Account() {
    }

    private Account(Builder builder) {
        this.ssn = Required.notNull(builder.ssn,"ssn");
        this.amount = Required.notNull(builder.amount,"amount");
    }

    public String getSsn() {
        return ssn;
    }

    public BigDecimal getAmount() {
        return amount;
    }


    @Override
    protected Object[] getIdFields() {
        return new Object[]{ssn,amount};
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder implements com.so4it.common.builder.Builder<Account>{

        private String ssn;

        private BigDecimal amount;

        public Builder withSsn(String ssn){
            this.ssn = ssn;
            return this;
        }

        public Builder withAmount(BigDecimal amount){
            this.amount = amount;
            return this;
        }

        @Override
        public  Account build() {
            return new Account(this);
        }
    }

}
