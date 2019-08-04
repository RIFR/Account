package se.lexicon.account.component.service;

import se.lexicon.account.component.domain.*;
import se.lexicon.account.component.entity.OrderBookEntity;
import se.lexicon.account.component.entity.OrderEntity;
import com.so4it.common.util.object.Required;
import com.so4it.gs.rpc.ServiceExport;
import se.lexicon.account.componment.dao.OrderBookDao;
import se.lexicon.account.componment.dao.OrderDao;

import java.math.BigDecimal;
import java.util.*;
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
                                        .withValue(obentity.getValue())
                                        //.withMaxValue(obentity.getMaxValue())
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

        if (order.getOrderBookId().getFirst()==null) return; //Dummy order?

        // GET ALL ORDERBOOKS, FILTER AGAINST ALL OTHERS BUY/SELL with same Instrument
        Set<OrderBookEntity> orderBookEntities = orderBookDao.readAll
                (OrderBookEntity.templateBuilder()
                        .withSellOrder(!order.getOrderBookId().getFirst().getSellOrder())
                        .withInstrument(order.getOrderBookId().getFirst().getInstrument())
                        //.withPhase(Phase.PENDING_INCOMING)
                        .build());

        double minMaxValue = 0d;

        OrderBookEntity bestMatchingOrderBook = null;
        List<OrderBookEntity> matchingOrderBooks = new ArrayList<>();

        for (OrderBookEntity orderBookEntity : orderBookEntities) {
            if (orderBookEntity.getPhase() == Phase.UNKNOWN ||
                    orderBookEntity.getPhase() == Phase.PENDING_INCOMING) {

                minMaxValue = AmountOf(orderBookEntity.getValue().getAmount().doubleValue(),
                        orderBookEntity.getValue().getCurrency(),
                        order.getOrderBookId().getFirst().getValue().getCurrency());

                if (order.getOrderBookId().getFirst().getSellOrder() ?
                            order.getOrderBookId().getFirst().getValue().getAmount().doubleValue() <= minMaxValue :
                            order.getOrderBookId().getFirst().getValue().getAmount().doubleValue() >= minMaxValue) {

                    bestMatchingOrderBook = chooseEntity
                            (order.getOrderBookId().getFirst(),bestMatchingOrderBook,orderBookEntity);

                    if (bestMatchingOrderBook.getNoOfItems().equals(order.getOrderBookId().getFirst().getNoOfItems()) &&
                            bestMatchingOrderBook.getValue().getCurrency().equals(order.getOrderBookId().getFirst().getValue().getCurrency()))
                        break; //Full matching found

                    if (orderBookEntity.getNoOfItems() < order.getOrderBookId().getFirst().getNoOfItems())
                        matchingOrderBooks.add(orderBookEntity); // for later use if not full matching found from one entity
                }
            }
        }

        if (bestMatchingOrderBook == null){

            // No matching, enter all in Dao for later use
            order.getOrderBookId().asList().stream().map(orderBook -> OrderBookEntity.builder()
                    .withOrderId(order.getId())
                    .withSsn(order.getSsn())
                    .withInstrument(orderBook.getInstrument())
                    .withNoOfItems(orderBook.getNoOfItems())
                    .withValue(orderBook.getValue())
                    //.withMaxValue(orderBook.getMaxValue())
                    .withPhase(Phase.PENDING_INCOMING)
                    .withSellOrder(orderBook.getSellOrder())
                    .build())
                    .forEach(orderBookDao::insert);

            return;
        }// No match found

        OrderEntity matchingOrder = orderDao.read(bestMatchingOrderBook.getOrderId());

        int itemsRemaining = order.getOrderBookId().getFirst().getNoOfItems() - bestMatchingOrderBook.getNoOfItems();
        int noOfItemsMatched;
        if (itemsRemaining == 0) {
            noOfItemsMatched = bestMatchingOrderBook.getNoOfItems();
        } else {
            noOfItemsMatched = itemsRemaining > 0 ? bestMatchingOrderBook.getNoOfItems() : order.getOrderBookId().getFirst().getNoOfItems();
        }

        orderBookDao.insert(OrderBookEntity.builder()
                //.withId(order.getOrderBookId().getFirst().getId())
                .withSsn(order.getSsn())
                .withOrderId(order.getId())
                .withNoOfItems(noOfItemsMatched)
                .withPhase(Phase.PENDING_OUTGOING)
                .withSellOrder(order.getOrderBookId().getFirst().getSellOrder())
                .withValue(order.getOrderBookId().getFirst().getValue())
                .withInstrument(order.getOrderBookId().getFirst().getInstrument())
                .withMatchingOrderId(matchingOrder.getId())
                .build());

        orderBookDao.insertOrUpdate(OrderBookEntity.builder()
                .withId(bestMatchingOrderBook.getId())
                .withSsn(matchingOrder.getSsn())
                .withOrderId(matchingOrder.getId())
                .withNoOfItems(noOfItemsMatched)
                .withPhase(Phase.PENDING_OUTGOING)
                .withSellOrder(bestMatchingOrderBook.getSellOrder())
                .withValue(bestMatchingOrderBook.getValue())
                .withInstrument(bestMatchingOrderBook.getInstrument())
                .withMatchingOrderId(order.getId())
                .build());

        if (itemsRemaining == 0) return;
        if (itemsRemaining < 0) {
            orderBookDao.insert(OrderBookEntity.builder()
                    .withSsn(matchingOrder.getSsn())
                    .withOrderId(matchingOrder.getId())
                    .withNoOfItems(Math.abs(itemsRemaining))
                    .withPhase(bestMatchingOrderBook.getPhase())
                    .withSellOrder(bestMatchingOrderBook.getSellOrder())
                    .withValue(bestMatchingOrderBook.getValue())
                    .withInstrument(bestMatchingOrderBook.getInstrument())
                    .build());
            return;
        }

        if (matchingOrderBooks.size() == 0) {

            orderBookDao.insert(OrderBookEntity.builder()
                    .withSsn(order.getSsn())
                    .withOrderId(order.getId())
                    .withNoOfItems(itemsRemaining)
                    .withPhase(Phase.PENDING_INCOMING)
                    .withSellOrder(order.getOrderBookId().getFirst().getSellOrder())
                    .withValue(order.getOrderBookId().getFirst().getValue())
                    .withInstrument(order.getOrderBookId().getFirst().getInstrument())
                    .build());
            return;
        } // Finished or No more match found

        for (OrderBookEntity orderBookEntity: matchingOrderBooks){
            matchingOrder = orderDao.read(orderBookEntity.getOrderId());
            itemsRemaining = itemsRemaining - orderBookEntity.getNoOfItems();

        }

    }

//    private boolean MatchOrder (Order forOrder, OrderBook orderBook) {
//
//    }

    private double AmountOf(double amount, Currency fromCurrency, Currency toCurrency) {
        if (fromCurrency == toCurrency) return amount;
        // NOT SAME
        return amount;
    }

    private OrderBookEntity chooseEntity
            (OrderBook order, OrderBookEntity current, OrderBookEntity compareWith) {

        if (current.getValue().getCurrency().equals(compareWith.getValue().getCurrency())) { // same currency

            if (current.getNoOfItems().equals(order.getNoOfItems())) return current;
            if (compareWith.getNoOfItems().equals(order.getNoOfItems())) return compareWith;
            if (current.getNoOfItems() >= compareWith.getNoOfItems()) return current;
            return compareWith;

        }

        // Otherwise, always choose the same currency as in the order
        if (order.getValue().getCurrency().equals(current.getValue().getCurrency())) return current;
        return compareWith;

    }

    @Override
    public BigDecimal getTotalOrderValueOfAllAccounts() {
        return orderDao.sum();
    }
}