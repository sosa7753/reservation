package zerobase.reservation.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import zerobase.reservation.dto.Store;
import zerobase.reservation.type.ReservationStatus;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(name ="store")
public class StoreEntity {

    @ManyToOne
    private MemberEntity memberEntity; // (외래키)한 사람이 여러 개의 매장을 가질 수 있다.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String location;

    private String text; // 상점 설명

    private int amount; // 예약 가능 수량

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    public StoreEntity(Store store, MemberEntity memberEntity) {
        this.memberEntity = memberEntity;
        this.name = store.getName();
        this.location = store.getLocation();
        this.text = store.getText();
        this.amount = store.getAmount();
        this.reservationStatus = store.getReservationStatus();
    }

    public void registerUpdate() {
        this.amount = amount - 1;
        if(amount == 0) {
            this.reservationStatus = ReservationStatus.RESERVATION_IMPOSSIBLE;
        }
    }

    public void cancelUpdate() {
        this.amount = amount + 1;
        if(this.reservationStatus.equals(ReservationStatus.RESERVATION_IMPOSSIBLE)) {
            this.reservationStatus = ReservationStatus.RESERVATION_AVAILABLE;
        }
    }
}






