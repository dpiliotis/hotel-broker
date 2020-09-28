package com.dpiliotis.hotel.broker.dto;

import java.math.BigDecimal;

public class PriceDto {

  private Long id;
  private BigDecimal amount;
  private String currency;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }
}
