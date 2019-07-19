package com.lexicon.account.component.test.common.domain;

import com.google.common.collect.Sets;
import com.lexicon.account.component.domain.Account;
import se.lexicon.account.component.domain.Order;
import com.so4it.common.util.object.Required;
import com.so4it.test.domain.AbstractTestBuilder;

import java.math.BigDecimal;

/**
 * @author Magnus Poromaa {@literal <mailto:magnus.poromaa@so4it.com/>}
 */
public class AccountTestBuilder extends AbstractTestBuilder<Account> {

    private Account.Builder builder;


    public AccountTestBuilder(Account.Builder builder) {
        this.builder = Required.notNull(builder, "builder");
        this.builder
                .withSsn("1111111111")
                .withAmount(BigDecimal.TEN);

    }

    public AccountTestBuilder withSsn(String ssn){
        builder.withSsn(ssn);
        return this;
    }

    public AccountTestBuilder withAmount(BigDecimal amount){
        builder.withAmount(amount);
        return this;
    }


    public static AccountTestBuilder builder() {
        return new AccountTestBuilder(Account.builder());
    }

    @Override
    public Account build() {
        return builder.build();
    }
}
