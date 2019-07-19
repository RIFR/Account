package se.lexicon.account.component.service;

import se.lexicon.account.component.domain.OrderBook;
import se.lexicon.account.component.domain.OrderBooks;
import se.lexicon.account.component.entity.OrderBookEntity;
import se.lexicon.account.component.entity.OrderEntity;
import com.so4it.common.util.object.Required;
import com.so4it.gs.rpc.ServiceExport;
import se.lexicon.account.component.domain.Order;
import se.lexicon.account.component.domain.Orders;
import se.lexicon.account.componment.dao.OrderBookDao;
import se.lexicon.account.componment.dao.OrderDao;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@ServiceExport({OrderComponentService.class})
public class OrderComponentServiceImpl implements OrderComponentService {

    private OrderDao orderDao;
    private OrderBookDao orderBookDao;

    public OrderComponentServiceImpl(OrderDao orderDao, OrderBookDao orderBookDao) {

        this.orderDao     = Required.notNull(orderDao,"orderDao");
        this.orderBookDao = Required.notNull(orderBookDao,"orderBookDao");
    }

    @Override
    public Orders getOrders(String ssn) {

        //where ssn =  ssn
        //Set<OrderEntity> orderEntities = orderDao.readAll(OrderEntity.templateBuilder().withSsn(ssn).build());

        Set<OrderBookEntity> orderBookEntities =
                orderBookDao.readAll(OrderBookEntity.templateBuilder().withSsn(ssn).build());


//        Orders orders = Orders.valueOf(orderEntities.stream().map(entity -> Order.builder()
//                .withId(entity.getId())
//                .withSsn(ssn)
//                .withAmount(entity.getAmount())
//                .withOrderBookId(new OrderBooks())
//                .build()).collect(Collectors.toSet()));
//
//        for (Order order : orders) {
//            OrderBook orderBook = orderBookEntities.stream()
//                    .filter(f -> f.getOrderId().equals(order.getId()))
//                    .map(entity -> OrderBook.builder()
//                            .withId(entity.getId())
//                            .withMinValue(entity.getMinValue())
//                            .build())
//                    .findFirst().get();
//
//            OrderBooks orderBooks = OrderBooks.valueOf(orderBookEntities.stream()
//                    .filter(f -> f.getOrderId().equals(order.getId()))
//                    .map(obentity -> OrderBook.builder()
//                            .withId(obentity.getId())
//                            .withInstrument(obentity.getInstrument())
//                            .withMinValue(obentity.getMinValue())
//                            .withMaxValue(obentity.getMaxValue())
//                            .withPhase(obentity.getPhase())
//                            .withSellOrder(obentity.getSellOrder())
//                            .build())
//                    .collect(Collectors.toSet()));
//         }


        return Orders.valueOf(orderDao.readAll(OrderEntity.templateBuilder().withSsn(ssn).build()).stream().
                map(entity -> Order.builder()
                        .withId(entity.getId())
                        .withSsn(ssn)
                        .withAmount(entity.getAmount())
                        .withOrderBookId(OrderBooks.valueOf(orderBookEntities.stream()
                                .filter(f -> f.getOrderId().equals(entity.getId()))
                                .map(obentity -> OrderBook.builder()
                                        .withId(obentity.getId())
                                        .withInstrument(obentity.getInstrument())
                                        .withMinValue(obentity.getMinValue())
                                        .withMaxValue(obentity.getMaxValue())
                                        .withPhase(obentity.getPhase())
                                        .withSellOrder(obentity.getSellOrder())
                                        .build())
                                .collect(Collectors.toSet())))
                        .build()).collect(Collectors.toSet()));

    }


    @Override
    public void placeOrder(Order order) {
        orderDao.insert(OrderEntity.builder()
                .withSsn(order.getSsn())
                .withAmount(order.getAmount())
                //.withOrderBookId(order.getOrderBookId())
                .build());

        order.getOrderBookId().asList().stream().map(orderBook -> OrderBookEntity.builder()
                .withOrderId(order.getId())
                .withSsn(order.getSsn())
                .withInstrument(orderBook.getInstrument())
                .withMinValue(orderBook.getMinValue())
                .withMaxValue(orderBook.getMaxValue())
                .withPhase(orderBook.getPhase())
                .withSellOrder(orderBook.getSellOrder())
                .build())
                .forEach(orderBookDao::insert);

        // GET ALL ORDERBOOKS, MATCH THIS ORDER AGAINST ALL OTHERS BUY or SELL
        Set<OrderBookEntity> orderBookEntities = orderBookDao.readAll
                (OrderBookEntity.templateBuilder()
                        .withSellOrder(!order.getOrderBookId().getFirst().getSellOrder()).build());

        boolean EqualMatchFound = false, FullMatchFound = false, partualMatchFound = false;
        String MatchingOrderId = null;

        for (OrderBookEntity orderBookEntity : orderBookEntities) {

        }

    }


    @Override
    public BigDecimal getTotalOrderValueOfAllAccounts() {
        return orderDao.sum();
    }
}