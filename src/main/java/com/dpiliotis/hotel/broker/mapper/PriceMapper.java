package com.dpiliotis.hotel.broker.mapper;

import com.dpiliotis.hotel.broker.dto.PriceDto;
import com.dpiliotis.hotel.broker.entity.Price;
import org.springframework.stereotype.Component;

@Component
public class PriceMapper {

  public PriceDto convertToDto(Price price) {

    PriceDto dto = new PriceDto();

    dto.setId(price.getId());
    dto.setAmount(price.getAmount());
    dto.setCurrency(price.getCurrency());

    return dto;
  }

  public Price convertToEntity(PriceDto price) {

    Price entity = new Price();

    entity.setId(price.getId());
    entity.setAmount(price.getAmount());
    entity.setCurrency(price.getCurrency());

    return entity;
  }
}
