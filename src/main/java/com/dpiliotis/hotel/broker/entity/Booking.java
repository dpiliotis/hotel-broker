package com.dpiliotis.hotel.broker.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Booking {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String customerName;
  private String customerSurname;
  private Integer numberOfPax;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="hotel_id", nullable=false)
  private Hotel hotel;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "price_id", referencedColumnName = "id")
  private Price price;

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

  public Hotel getHotel() {
    return hotel;
  }

  public void setHotel(Hotel hotel) {
    this.hotel = hotel;
  }

  public Price getPrice() {
    return price;
  }

  public void setPrice(Price price) {
    this.price = price;
  }
}
