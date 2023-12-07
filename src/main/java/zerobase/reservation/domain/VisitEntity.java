package zerobase.reservation.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(name ="visit")
public class VisitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String clientName;

    private String storeName;

    private String review;

    public VisitEntity(ReservationEntity reservationEntity) {
        this.clientName = reservationEntity.getClientName();
        this.storeName = reservationEntity.getStoreName();
        this.review = null;
    }
}
