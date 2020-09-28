package com.dpiliotis.hotel.broker.repository;

import com.dpiliotis.hotel.broker.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

  List<Booking> findBookingsByHotelId(Long hotelId);
}
