package zerobase.reservation.domain;

import jakarta.persistence.*;
import lombok.*;
import zerobase.reservation.dto.Reservation;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "reservation")
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String clientName; // 예약자 이름, unique 해야 한다.

    private String storeName;

    private LocalDateTime reservationDateTime; // 예약 시간

    public ReservationEntity(Reservation reservation) {
        this.clientName = reservation.getClientName();
        this.storeName = reservation.getStoreName();
        this.reservationDateTime = reservation.getReservationDateTime();
    }

}
