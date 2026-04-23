package rmi;

import model.Member;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface MemberService extends Remote {

    boolean authenticate(String memberId, String password) throws RemoteException;

    void save(Member member) throws RemoteException;

    void update(Member member) throws RemoteException;

    void delete(String memberId) throws RemoteException;

    Member findById(String memberId) throws RemoteException;

    List<Member> findAll() throws RemoteException;
}
