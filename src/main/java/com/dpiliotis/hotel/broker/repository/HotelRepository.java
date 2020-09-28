package com.dpiliotis.hotel.broker.repository;

import com.dpiliotis.hotel.broker.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

  @Query("select distinct h from Hotel h, Booking b where b.hotel = h and b.customerSurname = ?1")
  List<Hotel> findHotelsByBookingCustomerSurname(String customerSurname);
}
