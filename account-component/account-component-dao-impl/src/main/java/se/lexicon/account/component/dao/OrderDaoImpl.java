package se.lexicon.account.component.dao;

import se.lexicon.account.component.entity.OrderEntity;
import se.lexicon.account.componment.dao.OrderDao;
import com.so4it.component.dao.gs.AbstractSpaceDao;
import org.openspaces.core.GigaSpace;

/**
 * @author Magnus Poromaa {@literal <mailto:magnus.poromaa@so4it.com/>}
 */
public class OrderDaoImpl extends AbstractSpaceDao<OrderEntity, String> implements OrderDao {

    public OrderDaoImpl(GigaSpace gigaSpace) {
        super(gigaSpace);
    }
}



