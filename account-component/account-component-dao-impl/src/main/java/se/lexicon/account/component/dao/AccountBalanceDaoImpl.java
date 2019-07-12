package se.lexicon.account.component.dao;

import com.j_spaces.core.client.SQLQuery;
import se.lexicon.account.componment.dao.AccountBalanceDao;
import se.lexicon.account.component.entity.AccountBalanceEntity;
import com.so4it.component.dao.gs.AbstractSpaceDao;
import org.openspaces.core.GigaSpace;

import static org.openspaces.extensions.QueryExtension.*;

/**
 * @author Magnus Poromaa {@literal <mailto:magnus.poromaa@so4it.com/>}
 */
public class AccountBalanceDaoImpl extends AbstractSpaceDao<AccountBalanceEntity, String> implements AccountBalanceDao {



    public AccountBalanceDaoImpl(GigaSpace gigaSpace) {
        super(gigaSpace);
    }


    /**
     * Fetches the latest (e.g. highest insertion timestamp) {@code AccountBalanceEntity} available in the space.
     *
     *
     * @param arrangementId
     * @return
     */
    @Override
    public AccountBalanceEntity getLatest(String arrangementId) {
        SQLQuery<AccountBalanceEntity> sqlQuery = new SQLQuery<>(AccountBalanceEntity.class,"arrangementId = ?").setParameter(1,arrangementId);
        return maxEntry(getGigaSpace(),sqlQuery,"insertionTimestamp");
    }
}



