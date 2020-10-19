package org.mddarr.ordersservice.services;


import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.mddarr.ordersservice.dto.Order;
import org.mddarr.ordersservice.dto.OrderRequest;
import org.mddarr.ordersservice.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdersService {

    private final OrderRepository orderRepository;
    private final AvroOrdersProducerService avroOrderService;


    public List<Order> getAll() {
        return orderRepository.findAll().stream().map(OrdersService::map).collect(Collectors.toList());
    }

    public List<Order> fetchCustomersOrders(String customerID){
        return orderRepository.fetchCustomerOrders(customerID).stream().map(OrdersService::map).collect(Collectors.toList());
    }

    public List<Order> fetchCustomersOrders(String customerID, String orderDate){
        return orderRepository.fetchCustomerOrders(customerID, orderDate).stream().map(OrdersService::map).collect(Collectors.toList());
    }


    public Optional<Order> get(String orderID, long creationDate) {
        return orderRepository.get(orderID, creationDate).map(OrdersService::map);
    }
    public void delete(String orderID, long creationDate) {
        orderRepository.delete(orderID, creationDate);
    }

//    private List<AvroOrder> orderToAvroOrders(Order order){
//        List<AvroOrder> avroOrders = new ArrayList<>();
//        DateTime date = new DateTime();
//
//        for(int i =0; i < order.getProductBrands().size(); i++){
//            avroOrders.add(new AvroOrder(UUID.randomUUID().toString(), order.getCustomerID(), OrderState.PENDING,
//                    order.getProductBrands().get(i), order.getQuantities().get(i), order.getPrices().get(i), date));
//        }
//        return avroOrders;
//    }

    public Order createOrder(OrderRequest order){
        Order newOrder = orderRepository.put(order);
        orderRepository.put(order);
//        List<AvroOrder> avroOrders = orderToAvroOrders(newOrder);
//        for(AvroOrder avroOrder: avroOrders){
//            avroOrderService.sendOrder(avroOrder);
//        }
        return newOrder;
    }

//    public void delete(String orderID, String customerID) {
//        orderRepository.delete(orderID, customerID);
//    }

    private static Order map(OrderRepository.OrderEntity entity) {
        return new Order(entity.getOrderID(), entity.getOrderCreationDate(), entity.getCustomerID(), entity.getBrands(), entity.getProducts(), entity.getQuantities(), entity.getPrices(), entity.getOrderState());
    }

}
