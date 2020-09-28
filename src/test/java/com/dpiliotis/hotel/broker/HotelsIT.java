package com.dpiliotis.hotel.broker;

import com.dpiliotis.hotel.broker.dto.ApiResponse;
import com.dpiliotis.hotel.broker.dto.HotelDto;
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

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "dev"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class HotelsIT {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private HotelRepository hotelRepository;

  private String getBaseUrl() {
    return "http://localhost:" + port + "/hotels";
  }

  @Test
  public void testGetAllHotels() {

    // given
    String url = getBaseUrl();

    // when
    ResponseEntity<ApiResponse<List<HotelDto>>> response =
        restTemplate.exchange(url,
                              HttpMethod.GET,
                              null,
                              new ParameterizedTypeReference<ApiResponse<List<HotelDto>>>() { });

    // then
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals(2, response.getBody().getData().size());
  }

  @Test
  public void testGetBookedHotelsByCustomerSurname() {

    // given
    String url = getBaseUrl() + "?customer_surname=Croft";

    // when
    ResponseEntity<ApiResponse<List<HotelDto>>> response =
        restTemplate.exchange(url,
                              HttpMethod.GET,
                              null,
                              new ParameterizedTypeReference<ApiResponse<List<HotelDto>>>() { });

    // then
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals(2, response.getBody().getData().size());
  }

  @Test
  public void testGetHotelById() {

    // given
    String url = getBaseUrl() + "/1";

    // when
    ResponseEntity<ApiResponse<HotelDto>> response =
        restTemplate.exchange(url,
                              HttpMethod.GET,
                              null,
                              new ParameterizedTypeReference<ApiResponse<HotelDto>>() { });

    // then
    HotelDto data = response.getBody().getData();

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals(1, data.getId());
    Assertions.assertEquals("Alexander", data.getName());
    Assertions.assertEquals("Athens", data.getAddress());
    Assertions.assertEquals(5, data.getStars());
  }

  @Test
  public void testInsertHotel() {

    // given
    String url = getBaseUrl();

    HotelDto newHotel = new HotelDto();
    newHotel.setName("Alexander III");
    newHotel.setAddress("Karditsa");
    newHotel.setStars(5);

    HttpEntity<HotelDto> entity = new HttpEntity<>(newHotel);

    // when
    ResponseEntity<ApiResponse<HotelDto>> response =
        restTemplate.exchange(url,
                              HttpMethod.POST,
                              entity,
                              new ParameterizedTypeReference<ApiResponse<HotelDto>>() { });

    // then
    HotelDto data = response.getBody().getData();

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals(3, data.getId());
    Assertions.assertEquals("Alexander III", data.getName());
    Assertions.assertEquals("Karditsa", data.getAddress());
    Assertions.assertEquals(5, data.getStars());

    Assertions.assertEquals(3, hotelRepository.findAll().size());
  }

  @Test
  public void testUpdateHotel() {

    // given
    String url = getBaseUrl() + "/2";

    HotelDto toBeUpdated = new HotelDto();
    toBeUpdated.setName("Alexander II");
    toBeUpdated.setAddress("Larisa");
    toBeUpdated.setStars(4);

    HttpEntity<HotelDto> entity = new HttpEntity<>(toBeUpdated);

    // when
    ResponseEntity<ApiResponse<HotelDto>> response =
        restTemplate.exchange(url,
                              HttpMethod.PUT,
                              entity,
                              new ParameterizedTypeReference<ApiResponse<HotelDto>>() { });

    // then
    HotelDto data = response.getBody().getData();

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals(2, data.getId());
    Assertions.assertEquals("Alexander II", data.getName());
    Assertions.assertEquals("Larisa", data.getAddress());
    Assertions.assertEquals(4, data.getStars());
  }

  @Test
  public void testDeleteHotel() {

    // given
    String url = getBaseUrl() + "/2";

    // when
    ResponseEntity<ApiResponse<Void>> response =
        restTemplate.exchange(url,
                              HttpMethod.DELETE,
                              null,
                              new ParameterizedTypeReference<ApiResponse<Void>>() { });

    // then
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals(1, hotelRepository.findAll().size());
  }
}
