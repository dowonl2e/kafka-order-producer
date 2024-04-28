package com.study.kafka.order;

import com.study.kafka.order.domain.OrderService;
import com.study.kafka.order.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderApiController {

  private final OrderService orderService;

  @PostMapping("/write")
  public ResponseEntity write() {
    try {
      OrderDto orderDto = new OrderDto();
      orderDto.setProductId((long) 1);
      orderDto.setUserEmail("test@naver.com");
      orderDto.setOrderCount(2);
      orderService.order(orderDto);
    }
    catch (Exception e){
      e.printStackTrace();
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }
}
