package zerobase.reservation.service;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.reservation.domain.ReservationEntity;
import zerobase.reservation.domain.VisitEntity;
import zerobase.reservation.dto.Visit;
import zerobase.reservation.exception.ReservationException;
import zerobase.reservation.repository.ReservationRepository;
import zerobase.reservation.repository.VisitRepository;
import zerobase.reservation.type.ErrorCode;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static zerobase.reservation.type.VisitType.VISIT_NO;
import static zerobase.reservation.type.VisitType.VISIT_OK;

@Slf4j
@Service
@AllArgsConstructor
@Builder
public class VisitService {

    private final VisitRepository visitRepository;
    private final ReservationRepository reservationRepository;

    // 방문 체크
    @Transactional
    public String arrive(String clientName) {
        String visitType = String.valueOf(VISIT_NO);
        ReservationEntity reservation =
                this.reservationRepository.findByClientName(clientName)
                        .orElseThrow(() -> new ReservationException(ErrorCode.NOT_EXIST_CLIENT_NAME));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationTime = reservation.getReservationDateTime();

        // 예약 시간 - 도착 시간
        Duration duration = Duration.between(now, reservationTime);

        // 도착했을경우 리뷰 엔티티 생성
        if(duration.toMinutes() >= 10) {
            this.visitRepository.save(new VisitEntity(reservation));
            visitType = String.valueOf(VISIT_OK);
            log.info("방문 확인");
        }
        // 이후에는 예약 정보 삭제
        this.reservationRepository.delete(reservation);
        return visitType;
    }

    // 리뷰 작성 및 수정
    @Transactional
    public Visit updateReview(String clientName, String review) {
       VisitEntity visitEntity = this.visitRepository.findByClientName(clientName)
               .orElseThrow(() -> new ReservationException(ErrorCode.NOT_REVIEWER));

       visitEntity.setReview(review);

       Visit visit = new Visit(visitEntity);
       this.visitRepository.save(visitEntity);
       log.info("리뷰 수정");
       return visit;
    }

    // 리뷰 삭제
    @Transactional
    public Visit deleteReview(String clientName) {
        VisitEntity visitEntity = this.visitRepository.findByClientName(clientName)
                .orElseThrow(() -> new ReservationException(ErrorCode.NOT_EXIST_REVIEW));

        Visit visit = new Visit(visitEntity);

        this.visitRepository.delete(visitEntity);
        log.info("리뷰 삭제");
        return visit;
    }

    // 리뷰 조회
    public List<String> readReview(String storeName) {
       List<VisitEntity> visitEntities =
               this.visitRepository.findAllByStoreName(storeName);

       return visitEntities.stream()
               .map(e -> e.getReview())
               .collect(Collectors.toList());
    }
}
