package com.study.kafka.consumer;

import com.study.kafka.order.entity.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class StockConsumerListener implements AcknowledgingMessageListener<String, Map<String, Object>> {

  private static final String RESPONSE_TOPIC = "stock-valid-response";

  private final OrderRepository orderRepository;

  //@KafkaListener(groupId = "order-product", topics = RESPONSE_TOPIC, containerFactory = "kafkaListenerContainerFactory")
  public void onMessage(ConsumerRecord<String, Map<String, Object>> record, Acknowledgment ack){
    try {
      Map<String, Object> map = record.value();
      if(map == null || map.get("statusCode") == null || map.get("record") == null) {
        return;
      }

      int statusCode = (Integer)map.get("statusCode");
      Map<String, Object> data = (Map<String, Object>)map.get("record");
      log.info("응답 코드 ->  {} " + statusCode);
      log.info("응답 데이터 ->  {} " + data);
      if (statusCode != 200 && data.get("orderId") != null) {
        orderRepository.deleteById((String) data.get("orderId"));
      }
      ack.acknowledge();
    }
    catch (Exception e){
      e.printStackTrace();
    }
  }
}
