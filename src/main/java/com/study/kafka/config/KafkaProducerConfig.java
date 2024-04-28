package com.study.kafka.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.kafka.config.interceptor.KafkaProducerInterceptor;
import com.study.kafka.config.listener.KafkaProducerListener;
import com.study.kafka.order.dto.OrderDto;
import com.study.kafka.order.dto.ProductCountDto;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Value("${spring.kafka.producer.key-serializer}")
  private String keySerializer;

  @Value("${spring.kafka.producer.value-serializer}")
  private String valueSerializer;

  @Value("${spring.kafka.producer.transactional.id}")
  private String transactionalId;

  private final KafkaProducerInterceptor producerInterceptor;
  private final KafkaProducerListener producerListener;

  /**
   * BOOTSTRAP_SERVERS_CONFIG
   *   - Kafka의 기본 서버는 Bootstrap 서버로 Docker 환경에서 설정한 Kafka 서버 정보를 지정합니다.
   * KEY_SERIALIZER_CLASS_CONFIG
   *   - 토픽의 메시지 Key 타입을 지정해줍니다.
   * VALUE_SERIALIZER_CLASS_CONFIG
   *   - 토픽의 메시지 Value 타입을 지정해줍니다.
   * TRANSACTIONAL_ID_CONFIG
   *   - Producer에 트랜잭션 지원을 위해 설정합니다.
   *   - 하나 이상의 메시지를 안전하게 전송하고, 모든 메시지가 성공적으로 처리되거나 실패한 경우 롤백하기 위해 설정합니다.
   */
  @Bean
  public ProducerFactory<String, Map<String, Object>> producerFactory() {
    Map<String, Object> configs = new HashMap<>();
    configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
    configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
    configs.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, transactionalId);  //Producer factory does not support transactions 오류 발생 해결법

    DefaultKafkaProducerFactory<String, Map<String, Object>> producerFactory = new DefaultKafkaProducerFactory<>(configs);
    return producerFactory;
  }

  /**
   * setProducerInterceptor
   *   - 메시지 발송 전/후에 대한 확인을 위해 Interceptor를 등록합니다.
   * setProducerListener
   *   - 메시지 발송 결과 확인을 위해 Listener를 등록합니다.
   * setTransactionIdPrefix
   *   - 트랜잭션 ID의 접두사를 설정하는 데 사용됩니다.
   *   - 이를 통해 Kafka Producer는 여러 트랜잭션을 구분하기 위해 트랜잭션 ID에 접두사를 추가할 수 있습니다.
   */
  @Bean
  public KafkaTemplate<String, Map<String, Object>> kafkaTemplate() {
    KafkaTemplate<String, Map<String, Object>> kafkaTemplate = new KafkaTemplate<>(producerFactory());
    kafkaTemplate.setProducerInterceptor(producerInterceptor);
    kafkaTemplate.setProducerListener(producerListener);
    kafkaTemplate.setTransactionIdPrefix(transactionalId+"-tx-");
    return kafkaTemplate;
  }

}
