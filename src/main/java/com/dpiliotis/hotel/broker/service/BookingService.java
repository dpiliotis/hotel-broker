package com.dpiliotis.hotel.broker.service;

import com.dpiliotis.hotel.broker.entity.Booking;
import com.dpiliotis.hotel.broker.entity.Price;

import java.util.List;

public interface BookingService {

  Booking getBooking(Long bookingId);
  Booking insertBooking(Booking booking);
  Booking updateBooking(Booking booking);
  void deleteBooking(Long bookingId);

  List<Booking> getBookings(Long hotelId);
  List<Price> getPrices(Long hotelId);
}
