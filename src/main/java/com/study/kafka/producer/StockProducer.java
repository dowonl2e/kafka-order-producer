package com.study.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.kafka.order.dto.ProductCountDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockProducer {

  private final ObjectMapper objectMapper;
  private static final String REQUEST_TOPIC = "stock-valid-request";

  private final KafkaTemplate<String, Map<String, Object>> kafkaTemplate;

  public void sendMessage(final String key, final ProductCountDto productCountDto) {
    kafkaTemplate.send(
        new ProducerRecord<>(
            REQUEST_TOPIC, 0,
            LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli(),
            key, objectMapper.convertValue(productCountDto, Map.class)
        )
    );
  }
}
