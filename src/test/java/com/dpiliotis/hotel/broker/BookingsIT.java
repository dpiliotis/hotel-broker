package com.dpiliotis.hotel.broker;

import com.dpiliotis.hotel.broker.dto.AggregatedPriceDto;
import com.dpiliotis.hotel.broker.dto.ApiResponse;
import com.dpiliotis.hotel.broker.dto.BookingDto;
import com.dpiliotis.hotel.broker.dto.HotelDto;
import com.dpiliotis.hotel.broker.dto.PriceDto;
import com.dpiliotis.hotel.broker.repository.BookingRepository;
import com.dpiliotis.hotel.broker.repository.HotelRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "dev"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookingsIT {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private BookingRepository bookingRepository;
  @Autowired
  private HotelRepository hotelRepository;

  private String getBaseUrl() {
    return "http://localhost:" + port + "/bookings";
  }

  @Test
  public void testGetBookingById() {

    // given
    String url = getBaseUrl() + "/1";

    // when
    ResponseEntity<ApiResponse<BookingDto>> response =
        restTemplate.exchange(url,
                              HttpMethod.GET,
                              null,
                              new ParameterizedTypeReference<ApiResponse<BookingDto>>() { });

    // then
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals("Croft", response.getBody().getData().getCustomerSurname());
  }

  @Test
  public void testGetBookingsByHotel() {

    // given
    String url = getBaseUrl() + "?hotel_id=1";

    // when
    ResponseEntity<ApiResponse<List<BookingDto>>> response =
        restTemplate.exchange(url,
                              HttpMethod.GET,
                              null,
                              new ParameterizedTypeReference<ApiResponse<List<BookingDto>>>() { });

    // then
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals(3, response.getBody().getData().size());
  }

  @Test
  public void testInsertBooking() {

    // given
    String url = getBaseUrl();

    HotelDto hotel = new HotelDto();
    hotel.setId(1L);

    PriceDto newPrice = new PriceDto();
    newPrice.setAmount(BigDecimal.TEN);
    newPrice.setCurrency("GRD");

    BookingDto newBooking = new BookingDto();
    newBooking.setHotel(hotel);
    newBooking.setPrice(newPrice);
    newBooking.setNumberOfPax(3);
    newBooking.setCustomerName("Katy");
    newBooking.setCustomerSurname("Perry");

    HttpEntity<BookingDto> entity = new HttpEntity<>(newBooking);

    // when
    ResponseEntity<ApiResponse<BookingDto>> response =
        restTemplate.exchange(url,
                              HttpMethod.POST,
                              entity,
                              new ParameterizedTypeReference<ApiResponse<BookingDto>>() { });

    // then
    BookingDto data = response.getBody().getData();

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

    Assertions.assertEquals(5, data.getId());
    Assertions.assertEquals("Katy", data.getCustomerName());
    Assertions.assertEquals("Perry", data.getCustomerSurname());
    Assertions.assertEquals(3, data.getNumberOfPax());
    Assertions.assertEquals(hotel.getId(), data.getHotel().getId());
    Assertions.assertEquals(newPrice.getAmount(), data.getPrice().getAmount());

    Assertions.assertEquals(5, bookingRepository.findAll().size());
  }

  @Test
  public void testInsertBookingHotelNotExists() {

    // given
    String url = getBaseUrl();

    HotelDto hotel = new HotelDto();
    hotel.setName("Alexander III");
    hotel.setAddress("Karditsa");
    hotel.setStars(5);

    PriceDto newPrice = new PriceDto();
    newPrice.setAmount(BigDecimal.TEN);
    newPrice.setCurrency("GRD");

    BookingDto newBooking = new BookingDto();
    newBooking.setHotel(hotel);
    newBooking.setPrice(newPrice);
    newBooking.setNumberOfPax(3);
    newBooking.setCustomerName("Katy");
    newBooking.setCustomerSurname("Perry");

    HttpEntity<BookingDto> entity = new HttpEntity<>(newBooking);

    // when
    ResponseEntity<ApiResponse<BookingDto>> response =
        restTemplate.exchange(url,
                              HttpMethod.POST,
                              entity,
                              new ParameterizedTypeReference<ApiResponse<BookingDto>>() { });

    // then
    BookingDto data = response.getBody().getData();

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

    Assertions.assertEquals(5, data.getId());
    Assertions.assertEquals("Katy", data.getCustomerName());
    Assertions.assertEquals("Perry", data.getCustomerSurname());
    Assertions.assertEquals(3, data.getNumberOfPax());
    Assertions.assertEquals("Alexander III", data.getHotel().getName());
    Assertions.assertEquals(newPrice.getAmount(), data.getPrice().getAmount());

    Assertions.assertEquals(5, bookingRepository.findAll().size());
    Assertions.assertEquals(3, hotelRepository.findAll().size());
  }

  @Test
  public void testUpdateBooking() {

    // given
    String url = getBaseUrl() + "/1";

    HotelDto hotel = new HotelDto();
    hotel.setId(1L);

    PriceDto newPrice = new PriceDto();
    newPrice.setAmount(BigDecimal.TEN);
    newPrice.setCurrency("GRD");

    BookingDto newBooking = new BookingDto();

    newBooking.setHotel(hotel);
    newBooking.setPrice(newPrice);
    newBooking.setNumberOfPax(3);
    newBooking.setCustomerName("Katy");
    newBooking.setCustomerSurname("Perry");

    HttpEntity<BookingDto> entity = new HttpEntity<>(newBooking);

    // when
    ResponseEntity<ApiResponse<BookingDto>> response =
        restTemplate.exchange(url,
                              HttpMethod.PUT,
                              entity,
                              new ParameterizedTypeReference<ApiResponse<BookingDto>>() { });

    // then
    BookingDto data = response.getBody().getData();

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

    Assertions.assertEquals(1, data.getId());
    Assertions.assertEquals("Katy", data.getCustomerName());
    Assertions.assertEquals("Perry", data.getCustomerSurname());
    Assertions.assertEquals(3, data.getNumberOfPax());
    Assertions.assertEquals(hotel.getId(), data.getHotel().getId());
    Assertions.assertEquals(newPrice.getAmount(), data.getPrice().getAmount());

    Assertions.assertEquals(4, bookingRepository.findAll().size());
  }

  @Test
  public void testUpdateBookingHotelNotExists() {

    // given
    String url = getBaseUrl() + "/1";

    HotelDto hotel = new HotelDto();
    hotel.setName("Alexander III");
    hotel.setAddress("Karditsa");
    hotel.setStars(5);

    PriceDto newPrice = new PriceDto();
    newPrice.setAmount(BigDecimal.TEN);
    newPrice.setCurrency("GRD");

    BookingDto newBooking = new BookingDto();
    newBooking.setHotel(hotel);
    newBooking.setPrice(newPrice);
    newBooking.setNumberOfPax(3);
    newBooking.setCustomerName("Katy");
    newBooking.setCustomerSurname("Perry");

    HttpEntity<BookingDto> entity = new HttpEntity<>(newBooking);

    // when
    ResponseEntity<ApiResponse<BookingDto>> response =
        restTemplate.exchange(url,
                              HttpMethod.PUT,
                              entity,
                              new ParameterizedTypeReference<ApiResponse<BookingDto>>() { });

    // then
    BookingDto data = response.getBody().getData();

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

    Assertions.assertEquals(1, data.getId());
    Assertions.assertEquals("Katy", data.getCustomerName());
    Assertions.assertEquals("Perry", data.getCustomerSurname());
    Assertions.assertEquals(3, data.getNumberOfPax());
    Assertions.assertEquals("Alexander III", data.getHotel().getName());
    Assertions.assertEquals(newPrice.getAmount(), data.getPrice().getAmount());

    Assertions.assertEquals(4, bookingRepository.findAll().size());
    Assertions.assertEquals(3, hotelRepository.findAll().size());
  }

  @Test
  public void testDeleteBooking() {

    // given
    String url = getBaseUrl() + "/1";

    // when
    ResponseEntity<ApiResponse<Void>> response =
        restTemplate.exchange(url,
                              HttpMethod.DELETE,
                              null,
                              new ParameterizedTypeReference<ApiResponse<Void>>() { });

    // then
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

    Assertions.assertEquals(3, bookingRepository.findAll().size());
  }

  @Test
  public void testGetAggregatedPricesByHotel() {

    // given
    String url = getBaseUrl() + "/price?hotel_id=1";

    // when
    ResponseEntity<ApiResponse<List<AggregatedPriceDto>>> response =
        restTemplate.exchange(url,
                              HttpMethod.GET,
                              null,
                              new ParameterizedTypeReference<ApiResponse<List<AggregatedPriceDto>>>() { });

    // then
    List<AggregatedPriceDto> data = response.getBody().getData();
    AggregatedPriceDto usdPrices = data
        .stream()
        .filter(dto -> "USD".equals(dto.getCurrency()))
        .findFirst()
        .get();

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals(2, data.size());
    Assertions.assertEquals(0, BigDecimal.valueOf(501).compareTo(usdPrices.getAmount()));
  }

  @Test
  public void testNotFound() {

    // given
    String url = getBaseUrl() + "/11";

    BookingDto newBooking = new BookingDto();
    newBooking.setPrice(new PriceDto());
    newBooking.setHotel(new HotelDto());
    HttpEntity<BookingDto> entity = new HttpEntity<>(newBooking);

    // when
    ResponseEntity<ApiResponse<Void>> response =
        restTemplate.exchange(url,
                              HttpMethod.PUT,
                              entity,
                              new ParameterizedTypeReference<ApiResponse<Void>>() { });

    // then
    Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    Assertions.assertEquals(1, response.getBody().getErrors().size());
  }

  @Test
  public void testBadRequest() {

    // given
    String url = getBaseUrl() + "/-1";

    BookingDto newBooking = new BookingDto();
    HttpEntity<BookingDto> entity = new HttpEntity<>(newBooking);

    // when
    ResponseEntity<ApiResponse<Void>> response =
        restTemplate.exchange(url,
                              HttpMethod.PUT,
                              entity,
                              new ParameterizedTypeReference<ApiResponse<Void>>() { });

    // then
    Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    Assertions.assertEquals(1, response.getBody().getErrors().size());
  }
}
