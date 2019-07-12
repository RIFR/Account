package se.lexicon.account.component.service;


import se.lexicon.account.component.domain.Order;
import se.lexicon.account.component.domain.Account;
import com.so4it.gs.rpc.Broadcast;
import com.so4it.gs.rpc.RemoteResultReducer;
import com.so4it.gs.rpc.Routing;
import com.so4it.gs.rpc.exception.ResultReducerException;
import com.so4it.gs.rpc.remoting.RemoteResult;

import java.math.BigDecimal;
import java.util.List;

public interface AccountComponentService {

    String DEFAULT_BEAN_NAME = "accountComponentService";

    //account.hashCode() % numberOfPartitions
    void createAccount(@Routing("getSsn") Account account);

    //account.hashCode() % numberOfPartitions
    Account getAccount(@Routing String ssn);


    void placeOrder(@Routing String ssn, Order order);


    @Broadcast(reducer =  BigDecimalRemoteResultReducer.class)
    BigDecimal getTotalAmountOnAccounts();

    public static class BigDecimalRemoteResultReducer implements RemoteResultReducer<BigDecimal>{

        @Override
        public BigDecimal reduce(List<RemoteResult<BigDecimal>> list) throws ResultReducerException {
            return BigDecimal.valueOf( list.stream().map( rr -> rr.getResult().doubleValue()).reduce(0.0,Double::sum));
        }
    }



}
