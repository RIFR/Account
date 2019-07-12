package se.lexicon.account.component.client;


import se.lexicon.account.component.domain.Order;
import se.lexicon.account.component.domain.Account;

/**
 * @author Magnus Poromaa {@literal <mailto:magnus.poromaa@so4it.com/>}
 */
public interface AccountComponentClient {

    void createAccount(Account account);

    void PlaceOrder(Account account, Order order);

}
