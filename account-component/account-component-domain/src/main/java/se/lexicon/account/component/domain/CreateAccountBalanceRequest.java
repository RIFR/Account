package se.lexicon.account.component.domain;

import com.so4it.common.domain.DomainClass;
import com.so4it.common.util.object.Required;
import com.so4it.common.util.object.ValueObject;

import java.time.Instant;
import java.util.Set;

/**
 * @author Magnus Poromaa {@literal <mailto:magnus.poromaa@so4it.com/>}
 */
@DomainClass
public class CreateAccountBalanceRequest extends ValueObject {

    //The arrangement id of this account balance
    private String arrangementId;

    // Unique id for the posting
    private String batchId;

    // Timestamp from core system when update was made
    private Instant insertionTimestamp;

    //A set of all the registered balances for this arrangement
    private Set<Balance> balances;

    private CreateAccountBalanceRequest() {}
    private CreateAccountBalanceRequest(Builder builder) {
        this.arrangementId = Required.notNull(builder.arrangementId,"arrangementId");
        this.batchId = Required.notNull(builder.batchId,"batchId");
        this.insertionTimestamp = Required.notNull(builder.insertionTimestamp,"insertionTimestamp");
        this.balances = Required.notNull(builder.balances,"balances");
    }

    public String getArrangementId() {
        return arrangementId;
    }

    public String getBatchId() { return batchId; }

    public Instant getInsertionTimestamp() { return insertionTimestamp; }

    public Set<Balance> getBalances() {
        return balances;
    }

    @Override
    protected Object[] getIdFields() {
        return new Object[] { arrangementId, batchId, insertionTimestamp, balances };
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder implements com.so4it.common.builder.Builder<CreateAccountBalanceRequest>{

        //The arrangement id of this account balance
        private String arrangementId;

        // Unique id for the posting
        private String batchId;

        // Timestamp from core system when update was made
        private Instant insertionTimestamp;

        //A set of all the registered balances for this arrangement
        private Set<Balance> balances;

        public Builder withArrangementId(String arrangementId){
            this.arrangementId = arrangementId;
            return this;
        }

        public Builder withBatchId(String batchId){
            this.batchId = batchId;
            return this;
        }

        public Builder withInsertionTimestamp(Instant insertionTimestamp) {
            this.insertionTimestamp = insertionTimestamp;
            return this;
        }

        public Builder withBalances(Set<Balance> balances){
            this.balances = balances;
            return this;
        }


        @Override
        public CreateAccountBalanceRequest build() {
            return new CreateAccountBalanceRequest(this);
        }
    }
}
