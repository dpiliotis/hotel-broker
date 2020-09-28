package com.dpiliotis.hotel.broker.dto;

import java.math.BigDecimal;

public class AggregatedPriceDto {

  private Integer numberOfBookings;
  private BigDecimal amount;
  private String currency;

  public Integer getNumberOfBookings() {
    return numberOfBookings;
  }

  public void setNumberOfBookings(Integer numberOfBookings) {
    this.numberOfBookings = numberOfBookings;
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
