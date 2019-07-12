package se.lexicon.account.componment.dao;

import se.lexicon.account.component.entity.AccountBalanceEntity;
import com.so4it.component.GenericDao;

/**
 * @author Magnus Poromaa {@literal <mailto:magnus.poromaa@so4it.com/>}
 */
public interface AccountBalanceDao extends GenericDao<AccountBalanceEntity, String> {
    AccountBalanceEntity getLatest(String arrangementId);
}

