

package dao;

import model.Member;
import java.util.List;

public interface MemberDao {

    void save(Member member);

    void update(Member member);

    void delete(String memberId);

    Member findById(String memberId);

    List<Member> findAll();
boolean authenticate(String memberId, String password) throws Exception;
   
}
