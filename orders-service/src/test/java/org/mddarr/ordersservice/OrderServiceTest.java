package org.mddarr.ordersservice;


import org.junit.jupiter.api.extension.ExtendWith;
import org.mddarr.ordersservice.repository.OrderRepository;
import org.mddarr.ordersservice.services.OrdersService;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    private OrdersService ordersService;
}
