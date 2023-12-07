package zerobase.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import zerobase.reservation.domain.VisitEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Visit {

    private String clientName;

    private String storeName;

    private String review;

    public Visit(VisitEntity visitEntity) {
        this.clientName = visitEntity.getClientName();
        this.storeName = visitEntity.getStoreName();
        this.review = visitEntity.getReview();
    }
}
