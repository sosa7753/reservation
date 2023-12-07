package zerobase.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.reservation.domain.StoreEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<StoreEntity, Long> {

    boolean existsByName(String name); // 매장 명이 존재 하는지

    Optional<StoreEntity> findByName(String name);

    List<StoreEntity> findByNameStartingWithIgnoreCase(String keyword);

}
