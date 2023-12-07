package zerobase.reservation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import zerobase.reservation.dto.Visit;
import zerobase.reservation.service.VisitService;

import java.util.List;

@Slf4j
@RequestMapping("/visit")
@RequiredArgsConstructor
@RestController
public class VisitController {

    private final VisitService visitService;

    // 방문 체크
    @PostMapping("/arrive")
    public String arrive(@RequestParam String clientName) {
       return this.visitService.arrive(clientName);
    }

    // 리뷰 작성(or 수정)
    @PutMapping("/update")
    @PreAuthorize("hasRole('WRITE')")
    public ResponseEntity<?> updateReview(
            @RequestParam String clientName, @RequestBody String review) {
        Visit visit = this.visitService.updateReview(clientName,review);
        return ResponseEntity.ok(visit);
    }

    // 리뷰 삭제
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('WRITE')")
    public ResponseEntity<?> deleteReview(@RequestParam String clientName) {
        Visit visit = this.visitService.deleteReview(clientName);
        return ResponseEntity.ok(visit);
    }

    // 리뷰 조회
    @GetMapping("/read")
    @PreAuthorize("hasRole('READ')")
    public ResponseEntity<?> readReview(@RequestParam String StoreName) {
        List<String> reviews = this.visitService.readReview(StoreName);
        return ResponseEntity.ok(reviews);
    }
}
