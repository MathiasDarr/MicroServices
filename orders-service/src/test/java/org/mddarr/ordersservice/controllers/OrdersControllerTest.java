package org.mddarr.ordersservice.controllers;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mddarr.ordersservice.services.AvroOrdersProducerService;
import org.mddarr.ordersservice.services.OrdersService;
import org.mddarr.ordersservice.utils.OrdersTableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith({SpringExtension.class})
@WebMvcTest(OrdersController.class)
class OrdersControllerTest {

    @MockBean
    private OrdersService ordersService;

    @MockBean
    private AvroOrdersProducerService avroOrdersProducerService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void hello() throws Exception {
        RequestBuilder request = get("/orders/hello");
        MvcResult result = mockMvc.perform(request).andReturn();
        Assert.assertEquals("Hello, World", result.getResponse().getContentAsString());
    }

    @Test
    void testHelloWithName() throws Exception{
        mockMvc.perform(get("/orders/hello?name=Dan"))
            .andExpect(content().string("Hello, Dan"));
    }

    @Test
    void testGetOrderByIdAndCreationDate() throws Exception {
//        mockMvc.perform(get("/orders/20171129-29970/20150135"))
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk());
//                .andExpect(jsonPath("orderID").value("20171129-29970"));
    }

    @Test
    void testGetOrdersByCustomer() throws Exception {
        mockMvc.perform(get("/orders/customerOrders/jerry@gmail.com"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteOrderByIdAndCreationDate() throws Exception {
        mockMvc.perform(delete("http://localhost:8080/orders/20171129-29970/20150135"))
            .andExpect(status().is(202));
    }




}