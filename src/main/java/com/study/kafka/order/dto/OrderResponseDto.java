package com.study.kafka.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderResponseDto {

  private String orderId;

  private String userEmail;

  private Long productId;

  private Integer orderCount;

}
