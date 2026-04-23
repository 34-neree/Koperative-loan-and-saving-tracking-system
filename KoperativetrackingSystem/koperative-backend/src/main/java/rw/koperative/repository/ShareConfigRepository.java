package rw.koperative.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.koperative.model.ShareConfig;

@Repository
public interface ShareConfigRepository extends JpaRepository<ShareConfig, Long> {
}
