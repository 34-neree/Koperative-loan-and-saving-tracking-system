package rw.koperative.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.koperative.model.Member;
import rw.koperative.model.enums.MemberStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByNationalId(String nationalId);
    List<Member> findByStatus(MemberStatus status);
    List<Member> findByDistrictIgnoreCase(String district);
    List<Member> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
    boolean existsByNationalId(String nationalId);
    boolean existsByPhone(String phone);
}
