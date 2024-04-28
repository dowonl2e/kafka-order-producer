package com.study.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.kafka.config.KafkaProducerConfig;
import com.study.kafka.config.interceptor.KafkaProducerInterceptor;
import com.study.kafka.config.listener.KafkaProducerListener;
import com.study.kafka.order.dto.OrderDto;
import com.study.kafka.order.entity.OrderRepository;
import com.study.kafka.producer.StockProducer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.doThrow;

@DataJpaTest
@ActiveProfiles("local")
@Import({KafkaProducerConfig.class})
class KafkaApplicationTests {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private KafkaTemplate<String, Map<String, Object>> kafkaTemplate;

	@Test
	void contextLoads() {
	}

	@Test
	public void Kafka_프로듀서_메세지_발송() {
//		producerService.send("Producer1", "Message1");
	}



	@Test
	public void Kafka_프로듀서_메세지_발송2() {
//		String orderId = UUID.randomUUID().toString();
//		OrderDto orderDto = new OrderDto(
//				orderId, (long)1, "제품1", LocalDateTime.now()
//		);
//		producerService.send("Order_"+orderId, orderDto);
	}

	@Test
	public void Kafka_주문체크_메세지_발송2() {
//		try {
//			OrderDto orderDto = new OrderDto();
//			orderDto.setProductId((long) 1);
//			orderDto.setOrderCount(2);
//			orderService.order(orderDto);
//		}
//		catch (Exception e){
//			e.printStackTrace();
//		}
	}

	@Test
	@Transactional
	public void testSendMessageWithException() {
		// Mock KafkaTemplate을 생성하여 send 메서드를 강제로 실패시키도록 설정
//		doThrow(new RuntimeException("Simulated exception")).when(mockKafkaTemplate).send(Mockito.anyString(), Mockito.any());
		OrderDto orderDto = new OrderDto();
		orderDto.setProductId((long) 1);
		orderDto.setUserEmail("test@naver.com");
		orderDto.setOrderCount(2);
		String orderId = orderRepository.save(orderDto.toEntity()).getOrderId();

		// 예외 발생을 확인하기 위한 추가적인 테스트 코드 작성

		// 실패 유도를 포함한 메시지 발송
		kafkaTemplate.send(
				new ProducerRecord<>(
						"stock-valid-request-topic", 0,
						LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli(),
						"ORDER_"+orderId, new HashMap<>()
				)
		);

	}


}
