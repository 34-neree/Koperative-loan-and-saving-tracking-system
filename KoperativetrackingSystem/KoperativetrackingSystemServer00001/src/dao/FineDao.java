package dao;

import model.Fine;
import java.util.List;

public interface FineDao {

    void save(Fine fine);

    List<Fine> findByMember(String memberId);

    List<Fine> findUnpaid();

    void markPaid(int fineId);
}
