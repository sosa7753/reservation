package zerobase.reservation.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.reservation.domain.ReservationEntity;
import zerobase.reservation.domain.StoreEntity;
import zerobase.reservation.dto.Reservation;
import zerobase.reservation.exception.ReservationException;
import zerobase.reservation.repository.ReservationRepository;
import zerobase.reservation.repository.StoreRepository;
import zerobase.reservation.type.ErrorCode;
import zerobase.reservation.type.ReservationStatus;

import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
@Builder
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;

    // 예약 등록
    @Transactional
    public Reservation reservationStore(Reservation reservation) {
        StoreEntity store = this.storeRepository.findByName(reservation.getStoreName())
                .orElseThrow(() -> new ReservationException(ErrorCode.NOT_REGISTERED_STORE_NAME));

        // 예약 불가능한 상태
        if(store.getReservationStatus().equals(ReservationStatus.RESERVATION_IMPOSSIBLE)) {
            throw new ReservationException(ErrorCode.IMPOSSIBLE_RESERVATION);
        }
        if(store.getAmount() == 0) {
            throw new ReservationException(ErrorCode.FULLY_RESERVATION);
        }

        this.reservationRepository.save(new ReservationEntity(reservation));

        store.registerUpdate(); // 수량 및 예약 상태 변경
        this.storeRepository.save(store);
        log.info("예약 저장");
        return reservation;
    }

    @Transactional
    // 예약 수정(방문 시간만 변경 가능)
    public Reservation updateReservation(String clientName, LocalDateTime dateTime) {
        ReservationEntity reservationEntity =
                this.reservationRepository.findByClientName(clientName)
                        .orElseThrow(() -> new ReservationException(ErrorCode.NOT_EXIST_CLIENT_NAME));

        reservationEntity.setReservationDateTime(dateTime);

        Reservation reservation = new Reservation(reservationEntity);
        this.reservationRepository.save(reservationEntity);
        return reservation;
    }

    @Transactional
    // 예약 취소
    public Reservation cancelReservation(String clientName) {
        ReservationEntity reservationEntity = this.reservationRepository.findByClientName(clientName)
                .orElseThrow(() -> new ReservationException(ErrorCode.NOT_EXIST_CLIENT_NAME));

        Reservation reservation = new Reservation(reservationEntity);

        StoreEntity store = this.storeRepository.findByName(reservationEntity.getStoreName())
                .orElseThrow(() -> new ReservationException(ErrorCode.NOT_REGISTERED_STORE_NAME));

        this.reservationRepository.delete(reservationEntity);

        store.cancelUpdate(); // 수량 및 예약 상태 변경
        this.storeRepository.save(store);
        log.info("예약 취소");
        return reservation;
    }

    // 예약 조회
    public Reservation readReservation(String clientName) {
        ReservationEntity reservationEntity = this.reservationRepository.findByClientName(clientName)
                .orElseThrow(() -> new ReservationException(ErrorCode.NOT_EXIST_CLIENT_NAME));

        return new Reservation(reservationEntity);
    }

}
