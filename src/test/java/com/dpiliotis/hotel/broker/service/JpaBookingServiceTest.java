package com.dpiliotis.hotel.broker.service;

import com.dpiliotis.hotel.broker.entity.Booking;
import com.dpiliotis.hotel.broker.entity.Hotel;
import com.dpiliotis.hotel.broker.entity.Price;
import com.dpiliotis.hotel.broker.exception.ResourceNotFound;
import com.dpiliotis.hotel.broker.repository.BookingRepository;
import com.dpiliotis.hotel.broker.repository.HotelRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JpaBookingServiceTest {

  private BookingRepository repository;
  private BookingService service;
  private HotelRepository hotelRepository;

  @BeforeEach
  public void setup() {
    repository = mock(BookingRepository.class);
    hotelRepository = mock(HotelRepository.class);
    service = new JpaBookingService(hotelRepository, repository);
  }

  @Test
  public void testGetBookingById() {

    // given
    long bookingId = 1L;
    Booking expected = new Booking();
    when(repository.findById(bookingId)).thenReturn(Optional.of(expected));

    // when
    Booking result = service.getBooking(bookingId);

    // then
    Assertions.assertEquals(expected, result);
  }

  @Test
  public void testInsertBooking() {

    // given
    Booking input = new Booking();
    Hotel hotel = new Hotel();
    input.setHotel(hotel);
    Booking expected = new Booking();
    when(hotelRepository.save(hotel)).thenReturn(hotel);
    when(repository.save(input)).thenReturn(expected);

    // when
    Booking result = service.insertBooking(input);

    // then
    Assertions.assertEquals(expected, result);
  }

  @Test
  public void testUpdateBooking() {

    // given
    Booking input = new Booking();
    input.setId(1L);
    Hotel hotel = new Hotel();
    hotel.setId(2L);
    input.setHotel(hotel);
    Booking expected = new Booking();
    when(repository.findById(1L)).thenReturn(Optional.of(new Booking()));
    when(hotelRepository.findById(2L)).thenReturn(Optional.of(hotel));
    when(repository.save(input)).thenReturn(expected);

    // when
    Booking result = service.updateBooking(input);

    // then
    Assertions.assertEquals(expected, result);
  }

  @Test
  public void testUpdateBookingNotExist() {

    // given
    Booking input = new Booking();
    when(repository.findById(1L)).thenReturn(Optional.empty());

    // when
    ResourceNotFound e = Assertions.assertThrows(ResourceNotFound.class, () -> service.updateBooking(input));

    // then
    assertEquals("Booking does not exist.", e.getMessage());
  }

  @Test
  public void testDeleteBooking() {

    // given
    long bookingId = 1L;
    Booking booking = new Booking();
    when(repository.findById(bookingId)).thenReturn(Optional.of(booking));

    // when
    service.deleteBooking(bookingId);

    // then
    verify(repository, times(1)).delete(booking);
  }

  @Test
  public void testDeleteBookingIdempotent() {

    // given
    long bookingId = 1L;
    when(repository.findById(bookingId)).thenReturn(Optional.empty());

    // when
    service.deleteBooking(bookingId);

    // then
    verify(repository, never()).delete(any(Booking.class));
  }

  @Test
  public void testGetAllBookingsByHotel() {

    // given
    long hotelId = 1L;
    List<Booking> expected = Arrays.asList(new Booking(), new Booking(), new Booking());
    when(repository.findBookingsByHotelId(hotelId)).thenReturn(expected);

    // when
    List<Booking> result = service.getBookings(hotelId);

    // then
    Assertions.assertEquals(expected, result);
  }

  @Test
  public void testGetAllPricesByHotel() {

    // given
    long hotelId = 1L;

    Booking booking1 = new Booking();
    Price price1 = new Price();
    booking1.setPrice(price1);

    Booking booking2 = new Booking();
    Price price2 = new Price();
    booking2.setPrice(price2);

    List<Booking> bookings = Arrays.asList(booking1, booking2);
    when(repository.findBookingsByHotelId(hotelId)).thenReturn(bookings);

    // when
    List<Price> result = service.getPrices(hotelId);

    // then
    Assertions.assertEquals(Arrays.asList(price1, price2), result);
  }
}
