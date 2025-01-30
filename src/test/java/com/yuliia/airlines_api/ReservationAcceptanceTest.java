package com.yuliia.airlines_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuliia.airlines_api.reservation.ReservationDtoRequest;
import com.yuliia.airlines_api.reservation.ReservationRepository;
import com.yuliia.airlines_api.reservation.ReservationStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureMockMvc
public class ReservationAcceptanceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReservationRepository reservationRepository;


    @Test
    void testCreateReservation() throws Exception {
        ReservationDtoRequest request = new ReservationDtoRequest(3, 2L, 2L);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/public/reservations")
                        .with(SecurityMockMvcRequestPostProcessors.user("client").roles("CLIENT"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.reservedSeats").value(3))
                .andExpect(jsonPath("$.reservationTime", notNullValue()))
                .andExpect(jsonPath("$.reservationDate").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.lockExpirationTime", notNullValue()))
                .andExpect(jsonPath("$.status").value(ReservationStatus.PENDING.toString()))
                .andExpect(jsonPath("$.flight.id").value(2))
                .andExpect(jsonPath("$.user.id").value(2));
    }

    @Test
    void testGetReservationHistory() throws Exception {
        Long userId = 2L;

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/public/reservations/history/{userId}", userId)
                        .with(SecurityMockMvcRequestPostProcessors.user("client").roles("CLIENT")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", anyOf(isA(String.class), hasSize(greaterThanOrEqualTo(0)))));
    }

    @Test
    void testConfirmReservation() throws Exception {
        ReservationDtoRequest request = new ReservationDtoRequest(3, 2L, 2L);

        String response = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/public/reservations")
                        .with(SecurityMockMvcRequestPostProcessors.user("client").roles("CLIENT"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long reservationId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/public/reservations/confirmed/{reservationId}", reservationId)
                        .with(SecurityMockMvcRequestPostProcessors.user("client").roles("CLIENT")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reservationId))
                .andExpect(jsonPath("$.status").value(ReservationStatus.CONFIRMED.toString()));
    }

    @Test
    void testCancelReservation() throws Exception {
        ReservationDtoRequest request = new ReservationDtoRequest(3, 2L, 2L);

        String response = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/public/reservations")
                        .with(SecurityMockMvcRequestPostProcessors.user("client").roles("CLIENT"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long reservationId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(MockMvcRequestBuilders
                .put("/api/v1/public/reservations/confirmed/{reservationId}", reservationId)
                .with(SecurityMockMvcRequestPostProcessors.user("client").roles("CLIENT")));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/public/reservations/cancelled/{reservationId}", reservationId)
                        .with(SecurityMockMvcRequestPostProcessors.user("client").roles("CLIENT")))
                .andExpect(status().isOk());  // Перевірка статусу HTTP

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/public/reservations/history/{userId}", 2L)
                        .with(SecurityMockMvcRequestPostProcessors.user("client").roles("CLIENT")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value(ReservationStatus.CANCELLED.toString()))
                .andExpect(jsonPath("$[0].reservationTime", notNullValue()))
                .andExpect(jsonPath("$[0].reservationDate").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$[0].lockExpirationTime", notNullValue()));
    }

    @Test
    void testDeleteCancelledAndOutdatedReservations() throws Exception {
        Long userId = 2L;
        ReservationDtoRequest request = new ReservationDtoRequest(3, 2L, userId);  // Використовуємо userId
        String response = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/public/reservations")
                        .with(SecurityMockMvcRequestPostProcessors.user("client").roles("CLIENT"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long reservationId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(MockMvcRequestBuilders
                .put("/api/v1/public/reservations/confirmed/{reservationId}", reservationId)
                .with(SecurityMockMvcRequestPostProcessors.user("client").roles("CLIENT")));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/public/reservations/cancelled/{reservationId}", reservationId)
                        .with(SecurityMockMvcRequestPostProcessors.user("client").roles("CLIENT")))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/public/reservations/history/clean/{userId}", userId)
                        .with(SecurityMockMvcRequestPostProcessors.user("client").roles("CLIENT")))
                .andExpect(status().isNoContent());
    }

}
