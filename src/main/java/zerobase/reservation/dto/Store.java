package zerobase.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import zerobase.reservation.domain.StoreEntity;
import zerobase.reservation.type.ReservationStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Store {

    private String name;

    private String location;

    private String text;

    private int amount;

    private ReservationStatus reservationStatus;

    public Store(StoreEntity storeEntity) {
        this.name = storeEntity.getName();
        this.location = storeEntity.getLocation();
        this.text = storeEntity.getText();
        this.amount = storeEntity.getAmount();
        this.reservationStatus = storeEntity.getReservationStatus();
    }
}
