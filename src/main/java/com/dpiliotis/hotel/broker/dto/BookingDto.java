package com.dpiliotis.hotel.broker.dto;

import javax.validation.constraints.NotNull;

public class BookingDto {

  private Long id;
  private String customerName;
  private String customerSurname;
  private Integer numberOfPax;
  private @NotNull PriceDto price;
  private @NotNull HotelDto hotel;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public String getCustomerSurname() {
    return customerSurname;
  }

  public void setCustomerSurname(String customerSurname) {
    this.customerSurname = customerSurname;
  }

  public Integer getNumberOfPax() {
    return numberOfPax;
  }

  public void setNumberOfPax(Integer numberOfPax) {
    this.numberOfPax = numberOfPax;
  }

  public PriceDto getPrice() {
    return price;
  }

  public void setPrice(PriceDto price) {
    this.price = price;
  }

  public HotelDto getHotel() {
    return hotel;
  }

  public void setHotel(HotelDto hotel) {
    this.hotel = hotel;
  }
}
