package com.dpiliotis.hotel.broker.mapper;

import com.dpiliotis.hotel.broker.dto.HotelDto;
import com.dpiliotis.hotel.broker.entity.Hotel;
import org.springframework.stereotype.Component;

@Component
public class HotelMapper {

  public HotelDto convertToDto(Hotel hotel) {

    HotelDto dto = new HotelDto();

    dto.setId(hotel.getId());
    dto.setName(hotel.getName());
    dto.setAddress(hotel.getAddress());
    dto.setStars(hotel.getStars());

    return dto;
  }

  public Hotel convertToEntity(HotelDto hotel) {

    Hotel entity = new Hotel();

    entity.setId(hotel.getId());
    entity.setName(hotel.getName());
    entity.setAddress(hotel.getAddress());
    entity.setStars(hotel.getStars());

    return entity;
  }

}
