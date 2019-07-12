package se.lexicon.account.component.dao;

import com.j_spaces.core.client.SQLQuery;
import se.lexicon.account.component.entity.AccountTransactionEntity;
import se.lexicon.account.componment.dao.AccountTransactionDao;
import com.so4it.component.dao.gs.AbstractSpaceDao;
import org.openspaces.core.GigaSpace;

import static org.openspaces.extensions.QueryExtension.maxEntry;

/**
 * @author Magnus Poromaa {@literal <mailto:magnus.poromaa@so4it.com/>}
 */
public class AccountTransactionDaoImpl extends AbstractSpaceDao<AccountTransactionEntity, String> implements AccountTransactionDao {



    public AccountTransactionDaoImpl(GigaSpace gigaSpace) {
        super(gigaSpace);
    }


    /**
     * Fetches the latest (e.g. highest insertion timestamp) {@code AccountTransactionEntity} available in the space.
     *
     *
     * @param arrangementId
     * @return
     */
    @Override
    public AccountTransactionEntity getLatest(String arrangementId) {
        SQLQuery<AccountTransactionEntity> sqlQuery = new SQLQuery<>(AccountTransactionEntity.class,"arrangementId = ?").setParameter(1,arrangementId);
        return maxEntry(getGigaSpace(),sqlQuery,"insertionTimestamp");
    }
}



