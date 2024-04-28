package com.study.kafka.order.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.kafka.order.dto.OrderDto;
import com.study.kafka.order.dto.ProductCountDto;
import com.study.kafka.order.entity.OrderRepository;
import com.study.kafka.producer.StockProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final StockProducer stockProducer;

  /**
   * 1. 제품 체크 및 제고 개수 수정(Kafka 메세지 발송)
   * 2. 주문
   * 3. 결제
   * @param orderDto
   * @return
   * @throws Exception
   */
  @Transactional
  public HttpStatus order(final OrderDto orderDto) {
    String orderId = orderRepository.save(orderDto.toEntity()).getOrderId();
    stockProducer.sendMessage(
        "ORDER_" + orderDto.getProductId(),
        new ProductCountDto(
            orderId,
            orderDto.getProductId(),
            orderDto.getOrderCount(),
            LocalDateTime.now()
        )
    );
    return orderId != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
  }

}
