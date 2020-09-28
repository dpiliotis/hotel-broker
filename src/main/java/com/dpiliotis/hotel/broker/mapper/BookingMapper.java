package com.dpiliotis.hotel.broker.mapper;

import com.dpiliotis.hotel.broker.dto.BookingDto;
import com.dpiliotis.hotel.broker.entity.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

  private final PriceMapper priceMapper;
  private final HotelMapper hotelMapper;

  @Autowired
  public BookingMapper(PriceMapper priceMapper, HotelMapper hotelMapper) {
    this.priceMapper = priceMapper;
    this.hotelMapper = hotelMapper;
  }

  public BookingDto convertToDto(Booking booking) {

    BookingDto dto = new BookingDto();

    dto.setId(booking.getId());
    dto.setCustomerName(booking.getCustomerName());
    dto.setCustomerSurname(booking.getCustomerSurname());
    dto.setNumberOfPax(booking.getNumberOfPax());
    dto.setPrice(priceMapper.convertToDto(booking.getPrice()));
    dto.setHotel(hotelMapper.convertToDto(booking.getHotel()));

    return dto;
  }

  public Booking convertToEntity(BookingDto booking) {

    Booking entity = new Booking();

    entity.setId(booking.getId());
    entity.setCustomerName(booking.getCustomerName());
    entity.setCustomerSurname(booking.getCustomerSurname());
    entity.setNumberOfPax(booking.getNumberOfPax());
    entity.setPrice(priceMapper.convertToEntity(booking.getPrice()));
    entity.setHotel(hotelMapper.convertToEntity(booking.getHotel()));

    return entity;
  }

}
