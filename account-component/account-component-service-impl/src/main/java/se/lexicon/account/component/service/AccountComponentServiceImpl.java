package se.lexicon.account.component.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.lexicon.account.component.domain.Account;
import se.lexicon.account.component.domain.Order;
import se.lexicon.account.component.entity.OrderEntity;
import se.lexicon.account.componment.dao.OrderDao;
import com.so4it.common.util.object.Required;
import com.so4it.gs.rpc.ServiceExport;
import se.lexicon.account.component.entity.AccountEntity;
import se.lexicon.account.componment.dao.AccountDao;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@ServiceExport({AccountComponentService.class})
public class AccountComponentServiceImpl implements AccountComponentService {

    static final Logger LOGGER = LoggerFactory.getLogger(AccountComponentService.class);

    private final AccountDao accountDao;

    private final OrderDao orderDao;

    public AccountComponentServiceImpl(AccountDao accountDao,OrderDao orderDao) {
        this.accountDao = Required.notNull(accountDao, "accountDao");
        this.orderDao = Required.notNull(orderDao, "orderDao");
    }

    @Override
    public void createAccount(Account account) {
        AccountEntity accountEntity = AccountEntity.builder().withId(account.getSsn()).withAmount(account.getAmount()).build();
        accountDao.insert(accountEntity);


        account.getOrders().stream().map(order -> OrderEntity.builder().withSsn(account.getSsn()).
                    withAmount(order.getAmount()).withOrderBookId(order.getOrderBookId()).
                    withNoOfItems(order.getNoOfItems()).build())
                .forEach(orderDao::insert);
    }


    @Override
    public Account getAccount(String ssn) {
        AccountEntity accountEntity = accountDao.read(ssn);

        //where ssn =  ssn
        Set<OrderEntity> orderEntities = orderDao.readAll(OrderEntity.templateBuilder().withSsn(ssn).build());

        return Account.builder().withSsn(ssn).withAmount(accountEntity.getAmount()).withId(accountEntity.getId())
                .withOrders(orderEntities.stream().map(entity ->
                            Order.builder().withOrderBookId(entity.getOrderBookId()).withAmount(entity.getAmount())
                            .withNoOfItems(entity.getNoOfItems()).build())
                        .collect(Collectors.toSet()))
                .build();

    }

    @Override
    public void placeOrder(String ssn, Order order) {
        AccountEntity accountEntity = accountDao.read(ssn);

        OrderEntity orderEntity = OrderEntity.builder()
                .withSsn(ssn)
                .withAmount(order.getAmount())
                .withOrderBookId(order.getOrderBookId())
                .withNoOfItems(order.getNoOfItems()).build();

        if (accountEntity.getAmount().subtract(orderEntity.getAmount()).doubleValue() >= 0d) {

            orderDao.insert(orderEntity);

            accountDao.insertOrUpdate(accountEntity.builder().withId(accountEntity.getId()).
                    withAmount(accountEntity.getAmount().subtract(orderEntity.getAmount())).build());
        } else {

            LOGGER.error("Not enough money on the account " + accountEntity.getId() + ": "
                    + accountEntity.getAmount() +" < " + orderEntity.getAmount());

            throw new RuntimeException("Not enough money on the account "+ accountEntity.getId());
        }
    }

    @Override
    public BigDecimal getTotalAmountOnAccounts() {
        Set<AccountEntity> entities = accountDao.readAll();
        return BigDecimal.valueOf( entities.stream().map( rr -> rr.getAmount().doubleValue()).reduce(0.0,Double::sum));
    }
}