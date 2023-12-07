package zerobase.reservation.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.reservation.domain.MemberEntity;
import zerobase.reservation.domain.StoreEntity;
import zerobase.reservation.dto.Store;
import zerobase.reservation.exception.ReservationException;
import zerobase.reservation.repository.MemberRepository;
import zerobase.reservation.repository.StoreRepository;
import zerobase.reservation.type.ErrorCode;
import zerobase.reservation.type.ReservationStatus;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@Builder
public class StoreService {

    public final StoreRepository storeRepository;
    public final MemberRepository memberRepository;

    // 매장 등록
    @Transactional
    public Store registerStore(String username, Store request) {
        MemberEntity memberEntity = memberRepository.findByUsername(username)
                .orElseThrow(() -> new ReservationException(ErrorCode.NOT_EXIST_OWNER_NAME));

        // 매장 등록 부분
        String name = request.getName();
        boolean existName = storeRepository.existsByName(name);

        if(existName) {
            throw new ReservationException(ErrorCode.ALREADY_USE_STORE_NAME);
        }

        this.storeRepository.save(new StoreEntity(request, memberEntity));
        return request;
    }

    // 매장 정보 수정
    @Transactional
    public Store updateStore(Store store) {
        String name = store.getName();
        StoreEntity newStore = this.storeRepository.findByName(name)
                .orElseThrow(() -> new ReservationException(ErrorCode.NOT_REGISTERED_STORE_NAME));

        changeDetails(store, newStore); // 입력한 정보와 다른 값은 새로운 값으로 저장

        this.storeRepository.save(newStore);
        return store;
    }

    // 매장 정보 삭제
    @Transactional
    public Store deleteStore(String name) {
        StoreEntity storeEntity = this.storeRepository.findByName(name)
                .orElseThrow(() -> new ReservationException(ErrorCode.NOT_REGISTERED_STORE_NAME));
        Store store = new Store(storeEntity);
        this.storeRepository.delete(storeEntity);
        return store;
    }

    // 매장 조회
    public Store searchStore(String name) {
        StoreEntity storeEntity = this.storeRepository.findByName(name)
                .orElseThrow(() -> new ReservationException(ErrorCode.NOT_REGISTERED_STORE_NAME));

        return new Store(storeEntity);
    }

    // 자동 완성 검색
    public List<String> getNameByKeyword(String keyword) {
        List<StoreEntity> StoreEntities = this.storeRepository.
                findByNameStartingWithIgnoreCase(keyword);

        return StoreEntities.stream()
                .map(e -> e.getName())
                .collect(Collectors.toList());
    }

    // 다른 값 변경 로직
    private void changeDetails(Store store, StoreEntity newStore) {
        if(!store.getLocation().equals(newStore.getLocation())) {
            newStore.setLocation(store.getLocation());
            log.info("위치 수정 완료");
        }

        if(!store.getText().equals(newStore.getText())) {
            newStore.setText(store.getText());
            log.info("상세 정보 수정 완료");
        }

        if(store.getAmount() != newStore.getAmount()) {
            newStore.setAmount(store.getAmount());
            if(newStore.getAmount() == 0) {
                newStore.setReservationStatus(ReservationStatus.RESERVATION_IMPOSSIBLE);
            }
            log.info("예약 수량 수정 완료");
        }

        if(!store.getReservationStatus().equals(newStore.getReservationStatus())) {
            newStore.setReservationStatus(store.getReservationStatus());
            log.info("예약 상태 변경 완료");
        }
    }
}
