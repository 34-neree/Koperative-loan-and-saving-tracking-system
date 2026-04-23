package rmi;

import model.Share;
import model.ShareConfig;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ShareService extends Remote {

    void purchaseShares(Share share) throws RemoteException;

    List<Share> findByMember(String memberId) throws RemoteException;

    int getTotalSharesByMember(String memberId) throws RemoteException;

    ShareConfig getConfig() throws RemoteException;

    void updateConfig(ShareConfig config) throws RemoteException;

    boolean isEligibleForLoan(String memberId) throws RemoteException;
}
