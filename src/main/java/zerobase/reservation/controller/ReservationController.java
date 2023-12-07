package zerobase.reservation.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import zerobase.reservation.dto.Reservation;
import zerobase.reservation.service.ReservationService;

import java.time.LocalDateTime;

@Slf4j
@RequestMapping("/reservation")
@RequiredArgsConstructor
@RestController
public class ReservationController {

    private final ReservationService reservationService;

    // 에약 등록
    @PostMapping("/register")
    @PreAuthorize("hasRole('WRITE')")
    public ResponseEntity<?> reservationStore(@RequestBody Reservation reservation) {
        Reservation completeReservation = this.reservationService.reservationStore(reservation);
        log.info("예약 완료");
        return ResponseEntity.ok(completeReservation);
    }

    // 예약 수정(방문 시간만 가능)
    @PutMapping("/update")
    @PreAuthorize("hasRole('WRITE')")
    public ResponseEntity<?> updateReservation(
            @RequestParam String clientName, @RequestParam LocalDateTime dateTime) {
        Reservation changeReservation = this.reservationService.updateReservation(clientName,dateTime);
        log.info("예약 시간 변경 완료");
        return ResponseEntity.ok(changeReservation);
    }

    // 예약 취소
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('WRITE')")
    public ResponseEntity<?> cancelReservation(@RequestParam String clientName) {
        Reservation cancel = this.reservationService.cancelReservation(clientName);
        log.info("에약 취소 완료");
        return ResponseEntity.ok(cancel);
    }

    // 예약 조회
    @GetMapping("/read")
    @PreAuthorize("hasRole('READ')")
    public ResponseEntity<?> readReservation(@RequestParam String clientName) {
       Reservation result = this.reservationService.readReservation(clientName);

       return ResponseEntity.ok(result);
    }
}
