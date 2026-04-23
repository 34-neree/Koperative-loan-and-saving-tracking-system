package rw.koperative.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.koperative.model.Fine;
import rw.koperative.model.enums.FineStatus;

import java.util.List;

@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {
    List<Fine> findByMemberIdOrderByIssuedDateDesc(Long memberId);
    List<Fine> findByStatus(FineStatus status);
}
