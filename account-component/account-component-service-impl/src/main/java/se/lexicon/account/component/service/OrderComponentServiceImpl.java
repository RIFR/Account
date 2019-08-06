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


        return Orders.valueOf(orderDao.readAll(OrderEntity.templateBuilder().withSsn(ssn).build()).stream().
                map(entity -> Order.builder()
                        .withId(entity.getId())
                        .withSsn(ssn)
                        .withAmount(entity.getAmount())
                        .withInsertionTimestamp(entity.getInsertionTimestamp())
                        .withOrderBookId(OrderBooks.valueOf(orderBookEntities.stream()
                                .filter(f -> f.getOrderId().equals(entity.getId()))
                                .map(obentity -> OrderBook.builder()
                                        .withId(obentity.getId())
                                        .withInstrument(obentity.getInstrument())
                                        .withValue(obentity.getValue())
                                        .withPhase(obentity.getPhase())
                                        .withSellOrder(obentity.getSellOrder())
                                        .build())
                                .collect(Collectors.toSet())))
                        .build()).collect(Collectors.toSet()));

    }


    @Override
    public void placeOrder(Order order) {

        if (order.getOrderBookId().getFirst()==null) return; //Dummy order?

        orderDao.insert(OrderEntity.builder()
                .withId(order.getId())
                .withSsn(order.getSsn())
                .withAmount(order.getAmount())
                .withInsertionTimestamp(order.getInsertionTimestamp())
                .build());

//        order.getOrderBookId().asList().stream().map(orderBook -> OrderBookEntity.builder()
//                .withOrderId(order.getId())
//                .withSsn(order.getSsn())
//                .withInstrument(orderBook.getInstrument())
//                .withNoOfItems(orderBook.getNoOfItems())
//                .withValue(orderBook.getValue())
//                .withPhase(Phase.PENDING_INCOMING)
//                .withSellOrder(orderBook.getSellOrder())
//                .build())
//                .forEach(orderBookDao::insert);


        // Sweep through all orderBooks and try to match them against all corresponding orders
        for (OrderBook orderBook : order.getOrderBookId()) {

            if (orderBook.getPhase() == Phase.UNKNOWN || orderBook.getPhase() == Phase.PENDING_INCOMING) {

                MatchOrder (order, orderBook);

            }

        } // End of loop
    }

    private void MatchOrder (Order order, OrderBook orderBook) {

        // GET ALL ORDERBOOKS, FILTER AGAINST ALL OTHERS BUY/SELL with same Instrument
        Set<OrderBookEntity> orderBookEntities = orderBookDao.readAll
                (OrderBookEntity.templateBuilder()
                        .withSellOrder(!orderBook.getSellOrder())
                        .withInstrument(orderBook.getInstrument())
                        //.withPhase(Phase.PENDING_INCOMING)
                        .build());

        double minMaxValue = 0d;

        OrderBookEntity bestMatchingOrderBook = null;

        boolean allPossibleMatchingFound = false;
        int noOfItemsToMatch = orderBook.getNoOfItems();
        int noOfItemsMatched = 0;

        while (!allPossibleMatchingFound) {

            for (OrderBookEntity orderBookEntity : orderBookEntities) {
                if (orderBookEntity.getPhase() == Phase.UNKNOWN ||
                        orderBookEntity.getPhase() == Phase.PENDING_INCOMING) {

                    minMaxValue = AmountOf(orderBookEntity.getValue().getAmount().doubleValue(),
                            orderBookEntity.getValue().getCurrency(),
                            orderBook.getValue().getCurrency());

                    if (orderBook.getSellOrder() ?
                            orderBook.getValue().getAmount().doubleValue() <= minMaxValue :
                            orderBook.getValue().getAmount().doubleValue() >= minMaxValue) {

                        bestMatchingOrderBook = chooseEntity
                                (noOfItemsToMatch, orderBook.getValue().getCurrency(),
                                        bestMatchingOrderBook,orderBookEntity);

                        if (bestMatchingOrderBook.getNoOfItems().equals(noOfItemsToMatch) &&
                                bestMatchingOrderBook.getValue().getCurrency().equals(orderBook.getValue().getCurrency()))
                            break; //Full matching found, exit loop
                    }
                }
            } // loop end;

           if (bestMatchingOrderBook == null){

                // No matching, enter in Dao for later use
                orderBookDao.insert(OrderBookEntity.builder()
                        .withSsn(order.getSsn())
                        .withOrderId(order.getId())
                        .withNoOfItems(noOfItemsToMatch)
                        .withPhase(Phase.PENDING_INCOMING)
                        .withSellOrder(orderBook.getSellOrder())
                        .withValue(orderBook.getValue())
                        .withInstrument(orderBook.getInstrument())
                        .build());

                return; //No matching found, exit this procedure
            }

            // Handle the result from the seach

            OrderEntity matchingOrder = orderDao.read(bestMatchingOrderBook.getOrderId());

            int itemsRemaining = noOfItemsToMatch - bestMatchingOrderBook.getNoOfItems();
            allPossibleMatchingFound = itemsRemaining <= 0;
            noOfItemsMatched = itemsRemaining > 0 ? bestMatchingOrderBook.getNoOfItems() : noOfItemsToMatch;

            orderBookDao.insert(OrderBookEntity.builder()
                    .withSsn(order.getSsn())
                    .withOrderId(order.getId())
                    .withNoOfItems(noOfItemsMatched)
                    .withPhase(Phase.PENDING_OUTGOING)
                    .withSellOrder(orderBook.getSellOrder())
                    .withValue(orderBook.getValue())
                    .withInstrument(orderBook.getInstrument())
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

            if (itemsRemaining < 0) {
                // Insert the remaining matching items
                orderBookDao.insert(OrderBookEntity.builder()
                        .withSsn(matchingOrder.getSsn())
                        .withOrderId(matchingOrder.getId())
                        .withNoOfItems(Math.abs(itemsRemaining))
                        .withPhase(bestMatchingOrderBook.getPhase())
                        .withSellOrder(bestMatchingOrderBook.getSellOrder())
                        .withValue(bestMatchingOrderBook.getValue())
                        .withInstrument(bestMatchingOrderBook.getInstrument())
                        .build());
                return; //All possible matchings found, exit this procedure

            } else {noOfItemsToMatch = itemsRemaining;} // Continue with the rest

        } // While loop
    }

    private double AmountOf(double amount, Currency fromCurrency, Currency toCurrency) {
        if (fromCurrency == toCurrency) return amount;
        // NOT SAME CURRACY, CONVERT IT TO toCurrency
        return amount; // * ConvertionFactor(fromCurrency,toCurrency)
    }

    private OrderBookEntity chooseEntity
            (int noOfItemsToMatch, Currency inCurrency, OrderBookEntity current, OrderBookEntity compareWith) {

        if (current.getValue().getCurrency().equals(compareWith.getValue().getCurrency())) { // same currency

            if (current.getNoOfItems().equals(noOfItemsToMatch)) return current;
            if (compareWith.getNoOfItems().equals(noOfItemsToMatch)) return compareWith;
            if (current.getNoOfItems() >= compareWith.getNoOfItems()) return current;
            return compareWith;

        }

        // Otherwise, always choose the same currency as in the order
        if (inCurrency.equals(current.getValue().getCurrency())) return current;
        return compareWith;

    }

    @Override
    public BigDecimal getTotalOrderValueOfAllAccounts() { return orderDao.sum(); }

}