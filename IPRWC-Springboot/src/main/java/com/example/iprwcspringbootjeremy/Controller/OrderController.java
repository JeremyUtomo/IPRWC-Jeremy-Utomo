package com.example.iprwcspringbootjeremy.Controller;

import com.example.iprwcspringbootjeremy.DTO.Request.OrderRequest;
import com.example.iprwcspringbootjeremy.DTO.Response.OrderResponse;
import com.example.iprwcspringbootjeremy.Model.Order;
import com.example.iprwcspringbootjeremy.Service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<OrderResponse>> getOrdersByUser(@PathVariable("userId") String userId) {
        try {
            return ResponseEntity.ok(this.orderService.getOrdersByUser(userId));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<OrderResponse> addOrder(@RequestBody OrderRequest orderRequest) {
        return ResponseEntity.ok(this.orderService.addOrder(orderRequest));
    }
}
