package rmi.impl;

import dao.MemberDao;
import dao.impl.MemberDaoImpl;
import model.Member;
import rmi.MemberService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class MemberServiceImpl extends UnicastRemoteObject implements MemberService {

    private final MemberDao memberDao = new MemberDaoImpl();

    public MemberServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public void save(Member member) throws RemoteException {
        try {
            memberDao.save(member);
        } catch (Exception e) {
            throw new RemoteException("Error saving member", e);
        }
    }

    @Override
    public void update(Member member) throws RemoteException {
        try {
            memberDao.update(member);
        } catch (Exception e) {
            throw new RemoteException("Error updating member", e);
        }
    }

    @Override
    public void delete(String memberId) throws RemoteException {
        try {
            memberDao.delete(memberId);
        } catch (Exception e) {
            throw new RemoteException("Error deleting member", e);
        }
    }

    @Override
    public Member findById(String memberId) throws RemoteException {
        try {
            return memberDao.findById(memberId);
        } catch (Exception e) {
            throw new RemoteException("Error finding member", e);
        }
    }

    @Override
    public List<Member> findAll() throws RemoteException {
        try {
            return memberDao.findAll();
        } catch (Exception e) {
            throw new RemoteException("Error retrieving members", e);
        }
    }

    @Override
    public boolean authenticate(String memberId, String password) throws RemoteException {
        try {
            return memberDao.authenticate(memberId, password);
        } catch (Exception e) {
            throw new RemoteException("Authentication failed", e);
        }
    }
}
