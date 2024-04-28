package com.study.kafka.order.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "orders")
@Comment("주문")
@Getter
@NoArgsConstructor
public class Order {

  @Id
  @Comment("PK")
  @GeneratedValue(strategy = GenerationType.UUID)
  @UuidGenerator
  private String orderId;

  @Comment("주문자-이메일")
  @Column(length = 50)
  private String userEmail;

  @Comment("상품번호")
  private Long productId;

  @Comment("주문개수")
  private Integer orderCount;

  @Comment("주문일시")
  @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
  private LocalDateTime orderDate = LocalDateTime.now();

  @Builder
  public Order(String orderId, String userEmail, Long productId, Integer orderCount){
    this.orderId = orderId;
    this.userEmail = userEmail;
    this.productId = productId;
    this.orderCount = orderCount;
  }
}
