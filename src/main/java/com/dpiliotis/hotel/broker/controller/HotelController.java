package com.dpiliotis.hotel.broker.controller;

import com.dpiliotis.hotel.broker.dto.ApiResponse;
import com.dpiliotis.hotel.broker.dto.HotelDto;
import com.dpiliotis.hotel.broker.entity.Hotel;
import com.dpiliotis.hotel.broker.mapper.HotelMapper;
import com.dpiliotis.hotel.broker.service.HotelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping(path="/hotels")
public class HotelController {

  private static final Logger logger = LoggerFactory.getLogger(HotelController.class);

  private final HotelService hotelService;
  private final HotelMapper hotelMapper;

  @Autowired
  public HotelController(
      HotelService hotelService,
      HotelMapper hotelMapper
  ) {
    this.hotelService = hotelService;
    this.hotelMapper = hotelMapper;
  }

  @GetMapping
  public ApiResponse<List<HotelDto>> getHotels(@RequestParam(value = "customer_surname", required = false) String customerSurname) {

    List<HotelDto> result = Optional.ofNullable(customerSurname)
                                    .map(this::getHotelsByCustomer)
                                    .orElseGet(this::getAllHotels);

    return new ApiResponse<>(result);
  }

  private List<HotelDto> getHotelsByCustomer(String customerSurname) {
    logger.info("Request all booked hotels for customer: {}", customerSurname);

    return hotelService.getHotelsByCustomerSurname(customerSurname)
                                        .stream()
                                        .map(hotelMapper::convertToDto)
                                        .collect(Collectors.toList());
  }

  private List<HotelDto> getAllHotels() {
    logger.info("Request all hotels");

    List<Hotel> hotels = hotelService.getAllHotels();
    return hotels
        .stream()
        .map(hotelMapper::convertToDto)
        .collect(Collectors.toList());
  }

  @GetMapping(path = "/{hotelId}")
  public ApiResponse<HotelDto> getHotel(@PathVariable @Positive Long hotelId) {
    logger.info("Request hotel with id: {}", hotelId);

    Hotel result = hotelService.getHotel(hotelId);

    return new ApiResponse<>(hotelMapper.convertToDto(result));
  }

  @PostMapping
  public ApiResponse<HotelDto> createHotel(@RequestBody HotelDto hotel) {
    logger.info("Create new hotel");

    Hotel entity = hotelMapper.convertToEntity(hotel);
    entity.setId(null);
    Hotel result = hotelService.insertHotel(entity);

    return new ApiResponse<>(hotelMapper.convertToDto(result));
  }

  @PutMapping("/{hotelId}")
  public ApiResponse<HotelDto> updateHotel(@PathVariable @Positive Long hotelId, @RequestBody HotelDto hotel) {
    logger.info("Update hotel with id: {}", hotel.getId());

    Hotel entity = hotelMapper.convertToEntity(hotel);
    entity.setId(hotelId);
    Hotel result = hotelService.updateHotel(entity);

    return new ApiResponse<>(hotelMapper.convertToDto(result));
  }

  @DeleteMapping("/{hotelId}")
  public void deleteHotel(@PathVariable @Positive Long hotelId) {
    logger.info("Delete hotel with id: {}", hotelId);

    hotelService.deleteHotel(hotelId);
  }
}
