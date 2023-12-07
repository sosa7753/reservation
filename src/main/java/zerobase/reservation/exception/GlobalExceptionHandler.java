package zerobase.reservation.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import zerobase.reservation.dto.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    // Client -> 서버 시점의 에러
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)

    // Controller 안의 에러
    @ExceptionHandler
    public ErrorResponse handleReservationException(ReservationException e) {
        log.info("Reservation Application Error");
        return new ErrorResponse(e.getErrorCode(), e.getErrorMessage());
    }

    // 이외의 에러
    @ExceptionHandler(Exception.class)
    public Exception handleAllException() {
        System.out.println("error from GlobalExceptionHandler");
        return new Exception();
    }
}