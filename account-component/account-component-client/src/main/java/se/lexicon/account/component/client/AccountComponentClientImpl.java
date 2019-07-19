package se.lexicon.account.component.client;

import com.lexicon.account.component.domain.Account;
import se.lexicon.account.component.service.AccountComponentService;
import com.so4it.common.util.object.Required;

/**
 * @author Magnus Poromaa {@literal <mailto:magnus.poromaa@so4it.com/>}
 */
public class AccountComponentClientImpl implements AccountComponentClient{

    private AccountComponentService accountComponentService;

    public AccountComponentClientImpl(AccountComponentService accountComponentService) {
        this.accountComponentService = Required.notNull(accountComponentService,"accountComponentService");
    }

    @Override
    public void createAccount(Account account) {
        accountComponentService.createAccount(account);
    }
}
