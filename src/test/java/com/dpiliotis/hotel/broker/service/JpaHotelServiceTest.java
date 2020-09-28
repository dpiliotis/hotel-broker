package com.dpiliotis.hotel.broker.service;

import com.dpiliotis.hotel.broker.entity.Hotel;
import com.dpiliotis.hotel.broker.repository.HotelRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JpaHotelServiceTest {

  private HotelRepository repository;
  private HotelService service;

  @BeforeEach
  public void setup() {
    repository = mock(HotelRepository.class);
    service = new JpaHotelService(repository);
  }

  @Test
  public void testGetAllHotels() {

    // given
    List<Hotel> expected = Collections.singletonList(new Hotel());
    when(repository.findAll()).thenReturn(expected);

    // when
    List<Hotel> result = service.getAllHotels();

    // then
    Assertions.assertEquals(expected, result);
  }

  @Test
  public void testGetHotelById() {

    // given
    long hotelId = 1L;
    Hotel expected = new Hotel();
    when(repository.findById(hotelId)).thenReturn(Optional.of(expected));

    // when
    Hotel result = service.getHotel(hotelId);

    // then
    Assertions.assertEquals(expected, result);
  }

  @Test
  public void testInsertHotel() {

    // given
    Hotel input = new Hotel();
    Hotel expected = new Hotel();
    when(repository.save(input)).thenReturn(expected);

    // when
    Hotel result = service.insertHotel(input);

    // then
    Assertions.assertEquals(expected, result);
  }

  @Test
  public void testUpdateHotel() {

    // given
    Hotel input = new Hotel();
    input.setId(1L);
    Hotel expected = new Hotel();
    when(repository.getOne(1L)).thenReturn(expected);
    when(repository.save(input)).thenReturn(expected);

    // when
    Hotel result = service.updateHotel(input);

    // then
    Assertions.assertEquals(expected, result);
  }

  @Test
  public void testDeleteBooking() {

    // given
    long hotelId = 1L;
    Hotel hotel = new Hotel();
    when(repository.findById(hotelId)).thenReturn(Optional.of(hotel));

    // when
    service.deleteHotel(hotelId);

    // then
    verify(repository, times(1)).delete(hotel);
  }

  @Test
  public void testDeleteBookingIdempotent() {

    // given
    long hotelId = 1L;
    when(repository.findById(hotelId)).thenReturn(Optional.empty());

    // when
    service.deleteHotel(hotelId);

    // then
    verify(repository, never()).delete(any(Hotel.class));
  }

  @Test
  public void testGetHotelsByCustomerSurname() {

    // given
    String customerSurname = "Montana";
    List<Hotel> expected = Arrays.asList(new Hotel(), new Hotel(), new Hotel());
    when(repository.findHotelsByBookingCustomerSurname(customerSurname)).thenReturn(expected);

    // when
    List<Hotel> result = service.getHotelsByCustomerSurname(customerSurname);

    // then
    Assertions.assertEquals(expected, result);
  }
}
