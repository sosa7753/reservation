package zerobase.reservation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import zerobase.reservation.dto.Store;
import zerobase.reservation.service.StoreService;

import java.util.List;

@Slf4j
@RequestMapping("/store")
@RequiredArgsConstructor
@RestController
public class StoreController {

    public final StoreService storeService;

    // 매장 등록
    @PostMapping("/register")
    @PreAuthorize("hasRole('WRITE')")
    public ResponseEntity<?> registerStore(
            @RequestBody Store request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String username = userDetails.getUsername();
        Store store = this.storeService.registerStore(username, request);
        log.info("매장 등록 완료");
        return ResponseEntity.ok(store);
    }

    // 매장 수정
    @PutMapping("/update")
    @PreAuthorize("hasRole('WRITE')")
    public ResponseEntity<?> updateStore(@RequestBody Store store) {
        Store newStore = this.storeService.updateStore(store); // 매장명 변경시 자동완성 수정 포함
        log.info("매장 정보 수정 완료");
        return ResponseEntity.ok(newStore);
    }

    // 매장 삭제
    @DeleteMapping("/delete/{name}")
    @PreAuthorize("hasRole('WRITE')")
    public ResponseEntity<?> deleteStore(@PathVariable String name) {
        Store deleteStore = this.storeService.deleteStore(name);
        log.info("매장 정보 삭제 완료");
        return ResponseEntity.ok(deleteStore);
    }

    // 매장 검색
    @GetMapping("/read/{name}")
    @PreAuthorize("hasRole('READ')")
    public ResponseEntity<?> searchStore(@PathVariable String name) {
        Store readStore = this.storeService.searchStore(name);
        return ResponseEntity.ok(readStore);
    }

    // 자동완성으로 매장 리스트 검색
    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam String keyword) {
        List<String> stores = this.storeService.getNameByKeyword(keyword);
        return ResponseEntity.ok(stores);
    }
}
