package zerobase.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.reservation.domain.VisitEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface VisitRepository extends JpaRepository<VisitEntity, Long> {

    Optional<VisitEntity> findByClientName(String clientName);

    List<VisitEntity> findAllByStoreName(String StoreName);
}
