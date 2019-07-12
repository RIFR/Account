package se.lexicon.account.component.mapper;

import se.lexicon.account.component.entity.AccountTransactionEntity;
import se.lexicon.account.component.domain.AccountTransaction;
import se.lexicon.account.component.domain.CreateAccountTransactionRequest;
import com.so4it.common.util.Mapper;
import se.lexicon.account.component.entity.AccountTransactionEntity;


public final class AccountTransactionMapper {



    public static AccountTransactionEntity map(CreateAccountTransactionRequest createAccountTransactionRequest){
        return Mapper.of(createAccountTransactionRequest, AccountTransactionEntity::builder)
                .map(CreateAccountTransactionRequest::getArrangementId,AccountTransactionEntity.Builder::withArrangementId)
                .map(CreateAccountTransactionRequest::getBatchId,AccountTransactionEntity.Builder::withBatchId)
                .map(CreateAccountTransactionRequest::getInsertionTimestamp,AccountTransactionEntity.Builder::withInsertionTimestamp)
                .map(CreateAccountTransactionRequest::getAddress,AccountTransactionEntity.Builder::withAddress)
                .map(CreateAccountTransactionRequest::getPhase,AccountTransactionEntity.Builder::withPhase)
                .map(CreateAccountTransactionRequest::getAmount, AccountTransactionEntity.Builder::withAmount)
                .build(AccountTransactionEntity.Builder::build);
    }


    public static AccountTransaction map(AccountTransactionEntity entity){
        return entity != null ? Mapper.of(entity, AccountTransaction::builder)
                .map(AccountTransactionEntity::getId,AccountTransaction.Builder::withId)
                .map(AccountTransactionEntity::getArrangementId,AccountTransaction.Builder::withArrangementId)
                .map(AccountTransactionEntity::getBatchId,AccountTransaction.Builder::withBatchId)
                .map(AccountTransactionEntity::getInsertionTimestamp,AccountTransaction.Builder::withInsertionTimestamp)
                .map(AccountTransactionEntity::getAddress,AccountTransaction.Builder::withAddress)
                .map(AccountTransactionEntity::getPhase,AccountTransaction.Builder::withPhase)
                .map(AccountTransactionEntity::getAmount,AccountTransaction.Builder::withAmount)
                .build(AccountTransaction.Builder::build) : null;
    }
}
