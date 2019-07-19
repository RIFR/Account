package se.lexicon.account.component.dao;

import com.j_spaces.core.client.SQLQuery;
import com.so4it.component.dao.gs.AbstractSpaceDao;
import org.openspaces.core.GigaSpace;
import org.openspaces.extensions.QueryExtension;
import se.lexicon.account.component.entity.OrderBookEntity;
import se.lexicon.account.component.entity.OrderEntity;
import se.lexicon.account.componment.dao.OrderBookDao;
import se.lexicon.account.componment.dao.OrderDao;

import java.math.BigDecimal;


/**
 * @author Magnus Poromaa {@literal <mailto:magnus.poromaa@so4it.com/>}
 */
public class OrderBookDaoImpl extends AbstractSpaceDao<OrderBookEntity, String> implements OrderBookDao {

    public OrderBookDaoImpl(GigaSpace gigaSpace) {
        super(gigaSpace);
    }

//    @Override
//    public BigDecimal sum() {
//        return QueryExtension.sum(getGigaSpace(),new SQLQuery<>(OrderBookEntity.class,""),"amount");
//    }

}



