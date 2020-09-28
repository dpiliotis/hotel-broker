package com.dpiliotis.hotel.broker.controller;

import com.dpiliotis.hotel.broker.dto.AggregatedPriceDto;
import com.dpiliotis.hotel.broker.dto.ApiResponse;
import com.dpiliotis.hotel.broker.dto.BookingDto;
import com.dpiliotis.hotel.broker.entity.Booking;
import com.dpiliotis.hotel.broker.entity.Price;
import com.dpiliotis.hotel.broker.mapper.BookingMapper;
import com.dpiliotis.hotel.broker.service.BookingService;
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

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping(path="/bookings")
public class BookingController {

  private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

  private final BookingService bookingService;
  private final BookingMapper bookingMapper;

  @Autowired
  public BookingController(
      BookingService bookingService,
      BookingMapper bookingMapper
  ) {
    this.bookingService = bookingService;
    this.bookingMapper = bookingMapper;
  }

  @GetMapping("/{bookingId}")
  public ApiResponse<BookingDto> getBooking(@PathVariable @Positive Long bookingId) {
    logger.info("Retrieve booking with id: {}", bookingId);

    Booking result = bookingService.getBooking(bookingId);

    return new ApiResponse<>(bookingMapper.convertToDto(result));
  }

  @PostMapping
  public ApiResponse<BookingDto> createBooking(@RequestBody @Valid BookingDto booking) {
    logger.info("Create new booking.");

    Booking entity = bookingMapper.convertToEntity(booking);
    Booking result = bookingService.insertBooking(entity);

    return new ApiResponse<>(bookingMapper.convertToDto(result));
  }

  @PutMapping("/{bookingId}")
  public ApiResponse<BookingDto> updateBooking(@PathVariable @Positive Long bookingId, @RequestBody @Valid BookingDto booking) {
    logger.info("Update booking with id: {}", bookingId);

    Booking entity = bookingMapper.convertToEntity(booking);
    entity.setId(bookingId);

    Booking result = bookingService.updateBooking(entity);

    return new ApiResponse<>(bookingMapper.convertToDto(result));
  }

  @DeleteMapping("/{bookingId}")
  public void deleteBooking(@PathVariable @Positive Long bookingId) {
    logger.info("Delete booking with id: {}", bookingId);

    bookingService.deleteBooking(bookingId);
  }

  @GetMapping
  public ApiResponse<List<BookingDto>> getBookingsByHotelId(@RequestParam(value = "hotel_id") @Positive Long hotelId) {

    logger.info("Request bookings for hotel with id: {}", hotelId);

    List<BookingDto> result = bookingService
        .getBookings(hotelId)
        .stream()
        .map(bookingMapper::convertToDto)
        .collect(Collectors.toList());

    return new ApiResponse<>(result);
  }

  @GetMapping("/price")
  public ApiResponse<List<AggregatedPriceDto>> getPricesByHotelId(@RequestParam(value = "hotel_id") @Positive Long hotelId) {

    logger.info("Request price amounts for hotel with id: {}", hotelId);

    List<Price> prices = bookingService.getPrices(hotelId);

    Map<String, AggregatedPriceDto> pricesPerCurrency = new HashMap<>();

    for (Price p : prices) {

      Function<String, AggregatedPriceDto> initializeDto = currency -> {
        AggregatedPriceDto dto = new AggregatedPriceDto();
        dto.setCurrency(currency);
        dto.setNumberOfBookings(0);
        dto.setAmount(BigDecimal.ZERO);
        return dto;
      };

      AggregatedPriceDto dto = pricesPerCurrency.computeIfAbsent(p.getCurrency(), initializeDto);
      dto.setAmount(dto.getAmount().add(p.getAmount()));
      dto.setNumberOfBookings(dto.getNumberOfBookings() + 1);
    }

    return new ApiResponse<>(new ArrayList<>(pricesPerCurrency.values()));
  }
}
