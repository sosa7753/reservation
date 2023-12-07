package zerobase.reservation.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import zerobase.reservation.domain.ReservationEntity;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    private String clientName;

    private String storeName;

    private LocalDateTime reservationDateTime;

    public Reservation(ReservationEntity reservationEntity) {
        this.clientName = reservationEntity.getClientName();
        this.storeName = reservationEntity.getStoreName();
        this.reservationDateTime = reservationEntity.getReservationDateTime();
    }
}
