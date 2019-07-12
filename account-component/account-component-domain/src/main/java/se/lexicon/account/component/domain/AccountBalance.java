package se.lexicon.account.component.domain;

import com.so4it.common.domain.DomainClass;
import com.so4it.common.util.object.Required;
import com.so4it.common.util.object.ValueObject;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 *
 *
 * Represents a immutable balance statement on an account.
 *
 *
 * @author Magnus Poromaa {@literal <mailto:magnus.poromaa@so4it.com/>}
 */
@DomainClass
public class AccountBalance extends ValueObject {

    //The unique id
    private String id;

    //The arrangement id of this account balance
    private String arrangementId;

    // Unique id for the posting
    private String batchId;

    // Timestamp from core system when update was made
    private Instant insertionTimestamp;

    //A set of all the registered balances for this arrangement. 1 - *
    private Set<Balance> balances;

    private AccountBalance() {
    }

    private AccountBalance(Builder builder) {
        this.id = Required.notNull(builder.id,"id");
        this.arrangementId = Required.notNull(builder.arrangementId,"arrangementId");
        this.batchId = Required.notNull(builder.batchId,"batchId");
        this.insertionTimestamp = Required.notNull(builder.insertionTimestamp,"insertionTimestamp");
        this.balances = builder.balances;
    }

    @Override
    protected Object[] getIdFields() {
        return new Object[]{id,arrangementId,batchId};
    }


    public String getId() {
        return id;
    }

    public String getArrangementId() {
        return arrangementId;
    }

    public String getBatchId() {
        return batchId;
    }

    public Instant getInsertionTimestamp() { return insertionTimestamp; }

    public Set<Balance> getBalances() {
        return balances;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder implements com.so4it.common.builder.Builder<AccountBalance>{

        private String id;

        private String arrangementId;

        private String batchId;

        private Instant insertionTimestamp;

        private Set<Balance> balances;

        public Builder() {
            this.balances = new HashSet<>();
        }

        public Builder withId(String id){
            this.id = id;
            return this;
        }

        public Builder withArrangementId(String arrangementId){
            this.arrangementId = arrangementId;
            return this;
        }

        public Builder withBatchId(String batchId){
            this.batchId = batchId;
            return this;
        }

        public Builder withInsertionTimestamp(Instant insertionTimestamp){
            this.insertionTimestamp = insertionTimestamp;
            return this;
        }

        public Builder withBalances(Set<Balance> balances){
            this.balances = balances;
            return this;
        }

        @Override
        public AccountBalance build() {
            return new AccountBalance(this);
        }
    }


}
