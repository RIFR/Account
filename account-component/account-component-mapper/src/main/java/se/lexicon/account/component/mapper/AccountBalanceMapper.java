package se.lexicon.account.component.mapper;

import se.lexicon.account.component.entity.AccountBalanceEntity;
import se.lexicon.account.component.domain.AccountBalance;
import se.lexicon.account.component.domain.CreateAccountBalanceRequest;
import com.so4it.common.util.Mapper;

/**
 * @author Magnus Poromaa {@literal <mailto:magnus.poromaa@so4it.com/>}
 */
public final class AccountBalanceMapper {



    public static AccountBalanceEntity map(CreateAccountBalanceRequest createAccountBalanceRequest){
        return Mapper.of(createAccountBalanceRequest, AccountBalanceEntity::builder)
                .map(CreateAccountBalanceRequest::getArrangementId,AccountBalanceEntity.Builder::withArrangementId)
                .map(CreateAccountBalanceRequest::getBatchId,AccountBalanceEntity.Builder::withBatchId)
                .map(CreateAccountBalanceRequest::getInsertionTimestamp,AccountBalanceEntity.Builder::withInsertionTimestamp)
                .map(CreateAccountBalanceRequest::getBalances,AccountBalanceEntity.Builder::withBalances)
                .build(AccountBalanceEntity.Builder::build);
    }


    public static AccountBalance map(AccountBalanceEntity entity){
        return entity != null ? Mapper.of(entity, AccountBalance::builder)
                .map(AccountBalanceEntity::getId,AccountBalance.Builder::withId)
                .map(AccountBalanceEntity::getArrangementId,AccountBalance.Builder::withArrangementId)
                .map(AccountBalanceEntity::getBatchId,AccountBalance.Builder::withBatchId)
                .map(AccountBalanceEntity::getInsertionTimestamp,AccountBalance.Builder::withInsertionTimestamp)
                .map(AccountBalanceEntity::getBalances,AccountBalance.Builder::withBalances)
                .build(AccountBalance.Builder::build) : null;
    }
}
