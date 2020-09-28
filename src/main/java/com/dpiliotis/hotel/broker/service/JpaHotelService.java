package com.dpiliotis.hotel.broker.service;

import com.dpiliotis.hotel.broker.entity.Hotel;
import com.dpiliotis.hotel.broker.exception.ResourceNotFound;
import com.dpiliotis.hotel.broker.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JpaHotelService implements HotelService {

  private final HotelRepository hotelRepository;

  @Autowired
  public JpaHotelService(HotelRepository hotelRepository) {
    this.hotelRepository = hotelRepository;
  }

  @Override
  public List<Hotel> getAllHotels() {
    return hotelRepository.findAll();
  }

  @Override
  public Hotel getHotel(Long hotelId) {
    return hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFound("Hotel does not exist"));
  }

  @Override
  public Hotel insertHotel(Hotel hotel) {
    return hotelRepository.save(hotel);
  }

  @Override
  public Hotel updateHotel(Hotel hotel) {
    Hotel existing = hotelRepository.getOne(hotel.getId());
    hotel.setBookings(existing.getBookings());
    return hotelRepository.save(hotel);
  }

  @Override
  public void deleteHotel(Long hotelId) {
    hotelRepository.findById(hotelId).ifPresent(hotelRepository::delete);
  }

  @Override
  public List<Hotel> getHotelsByCustomerSurname(String customerSurname) {
    return hotelRepository.findHotelsByBookingCustomerSurname(customerSurname);
  }

}
