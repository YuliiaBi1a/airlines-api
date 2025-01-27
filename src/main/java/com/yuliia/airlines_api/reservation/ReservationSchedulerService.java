package com.yuliia.airlines_api.reservation;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ReservationSchedulerService {
    private final ReservationService reservationService;

    public ReservationSchedulerService(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Scheduled(fixedRate = 60000) //un minuto
    public void updateOutdatedReservationsAutomatically() {
        reservationService.updateOutdatedReservations();
    }
}
