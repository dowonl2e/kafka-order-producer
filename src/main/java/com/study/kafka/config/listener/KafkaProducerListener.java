package com.study.kafka.config.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaProducerListener implements ProducerListener {

  /**
   * Message 전송된 이후 정상 처리된 경우 호출된다.
   * @param producerRecord
   * @param recordMetadata
   */
  @Override
  public void onSuccess(ProducerRecord producerRecord, RecordMetadata recordMetadata) {
    ProducerListener.super.onSuccess(producerRecord, recordMetadata);
    log.info("Message onSuccess Header -> " + producerRecord.headers());
    log.info("Message onSuccess Topic -> " + recordMetadata.topic());
    log.info("Message onSuccess Offset -> " + recordMetadata.offset());
    log.info("Message onSuccess Body -> " + producerRecord.value());
  }

  /**
   * Message 전송에 실패한 경우 호출된다.
   * @param producerRecord
   * @param recordMetadata
   * @param exception
   */
  @Override
  public void onError(ProducerRecord producerRecord, RecordMetadata recordMetadata, Exception exception) {
    ProducerListener.super.onError(producerRecord, recordMetadata, exception);
    log.info("Message onError Header -> " + producerRecord.headers());
    log.info("Message onError Offset -> " + recordMetadata.offset());
    log.info("Message onError Topic -> " + recordMetadata.topic());
    log.info("Message onError Body -> " + producerRecord.value());
    log.error("Message onError exception -> " + exception);
  }

}
