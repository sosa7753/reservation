package zerobase.reservation.type;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // 매장 등록 파트
    ALREADY_USE_STORE_NAME("이미 사용 중인 매장명 입니다."),
    NOT_REGISTERED_STORE_NAME("등록 되지 않는 매장명 입니다."),
    NOT_EXIST_OWNER_NAME("등록되지 않은 소유주 입니다."),
    // 로그인 파트
    NOT_USER_NAME("존재하지 않는 유저명 입니다."),
    ALREADY_USE_NAME("이미 사용 중인 유저명 입니다."),
    NOT_EQUALS_PASSWORD("비밀번호가 일치하지 않습니다."),

    // 예약 파트
    NOT_EXIST_CLIENT_NAME("예약자 명단에 없습니다."),
    IMPOSSIBLE_RESERVATION("현재 예약 불가능한 매장입니다."),
    FULLY_RESERVATION("현재 예약이 다 찬 상태입니다."),

    // 리뷰 파트
    ALREADY_EXPIRED_RESERVATION("이미 만료된 예약입니다."),
    NOT_REVIEWER("리뷰 작성 대상이 아닙니다."),
    NOT_EXIST_REVIEW("리뷰가 존재하지 않습니다.");

    private final String description;

    ErrorCode(String description) {
        this.description = description;
    }
}
