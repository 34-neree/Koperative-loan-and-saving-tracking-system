package dao;

import model.Share;
import java.util.List;

public interface ShareDao {

    void save(Share share);

    List<Share> findByMember(String memberId);

    int getTotalSharesByMember(String memberId);
}
