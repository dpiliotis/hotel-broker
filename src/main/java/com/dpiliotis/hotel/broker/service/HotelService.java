package com.dpiliotis.hotel.broker.service;

import com.dpiliotis.hotel.broker.entity.Hotel;

import java.util.List;

public interface HotelService {

  List<Hotel> getAllHotels();
  Hotel getHotel(Long hotelId);
  Hotel insertHotel(Hotel hotel);
  Hotel updateHotel(Hotel hotel);
  void deleteHotel(Long hotelId);

  List<Hotel> getHotelsByCustomerSurname(String customerSurname);
}
