package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Member;

public interface MemberService extends Remote {

    void save(Member member) throws RemoteException;

    void update(Member member) throws RemoteException;

    void delete(String memberId) throws RemoteException;

    Member findById(String memberId) throws RemoteException;

    List<Member> findAll() throws RemoteException;

    boolean authenticate(String memberId, String password) throws RemoteException;
}
