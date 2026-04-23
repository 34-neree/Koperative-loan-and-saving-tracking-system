package rw.koperative.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rw.koperative.model.Share;

import java.util.List;

@Repository
public interface ShareRepository extends JpaRepository<Share, Long> {
    List<Share> findByMemberIdOrderByPurchaseDateDesc(Long memberId);

    @Query("SELECT COALESCE(SUM(s.numberOfShares), 0) FROM Share s WHERE s.member.id = :memberId AND s.status = 'PAID'")
    Integer countTotalSharesByMemberId(@Param("memberId") Long memberId);
}
