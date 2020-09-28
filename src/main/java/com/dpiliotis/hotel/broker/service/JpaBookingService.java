package com.dpiliotis.hotel.broker.service;

import com.dpiliotis.hotel.broker.entity.Booking;
import com.dpiliotis.hotel.broker.entity.Hotel;
import com.dpiliotis.hotel.broker.entity.Price;
import com.dpiliotis.hotel.broker.exception.ResourceNotFound;
import com.dpiliotis.hotel.broker.repository.BookingRepository;
import com.dpiliotis.hotel.broker.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JpaBookingService implements BookingService {

  private final HotelRepository hotelRepository;
  private final BookingRepository bookingRepository;

  @Autowired
  public JpaBookingService(
      HotelRepository hotelRepository,
      BookingRepository bookingRepository
  ) {
    this.hotelRepository = hotelRepository;
    this.bookingRepository = bookingRepository;
  }

  @Override
  public Booking getBooking(Long bookingId) {
    return bookingRepository.findById(bookingId).orElseThrow(() -> new ResourceNotFound("Booking does not exist."));
  }

  @Transactional
  @Override
  public Booking insertBooking(Booking booking) {
    Hotel ifNotExists = createIfNotExists(booking.getHotel());
    booking.setHotel(ifNotExists);
    return bookingRepository.save(booking);
  }

  @Transactional
  @Override
  public Booking updateBooking(Booking booking) {
    getBooking(booking.getId());
    Hotel existingOrNew = createIfNotExists(booking.getHotel());
    booking.setHotel(existingOrNew);
    return bookingRepository.save(booking);
  }

  private Hotel createIfNotExists(Hotel hotel) {

    return Optional.ofNullable(hotel.getId())
                   .map(hotelRepository::findById)
                   .flatMap(Function.identity())
                   .orElseGet(() -> hotelRepository.save(hotel));
  }

  @Override
  public void deleteBooking(Long bookingId) {
    bookingRepository.findById(bookingId).ifPresent(bookingRepository::delete);
  }

  @Override
  public List<Booking> getBookings(Long hotelId) {
    return bookingRepository.findBookingsByHotelId(hotelId);
  }

  @Override
  public List<Price> getPrices(Long hotelId) {

    return getBookings(hotelId)
        .stream()
        .map(Booking::getPrice)
        .collect(Collectors.toList());
  }
}
