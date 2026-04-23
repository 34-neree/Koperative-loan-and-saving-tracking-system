package rmi.impl;

import dao.ShareDao;
import dao.ShareConfigDao;
import dao.impl.ShareDaoImpl;
import dao.impl.ShareConfigDaoImpl;
import model.Share;
import model.ShareConfig;
import rmi.ShareService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ShareServiceImpl extends UnicastRemoteObject implements ShareService {

    private final ShareDao shareDao = new ShareDaoImpl();
    private final ShareConfigDao configDao = new ShareConfigDaoImpl();

    public ShareServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public void purchaseShares(Share share) throws RemoteException {
        try {
            ShareConfig config = configDao.getConfig();
            share.setSharePriceAtPurchase(config.getSharePrice());
            share.setTotalValue(share.getNumberOfShares() * config.getSharePrice());
            share.setStatus("PAID");
            shareDao.save(share);
        } catch (Exception e) {
            throw new RemoteException("Error purchasing shares", e);
        }
    }

    @Override
    public List<Share> findByMember(String memberId) throws RemoteException {
        try {
            return shareDao.findByMember(memberId);
        } catch (Exception e) {
            throw new RemoteException("Error fetching shares", e);
        }
    }

    @Override
    public int getTotalSharesByMember(String memberId) throws RemoteException {
        try {
            return shareDao.getTotalSharesByMember(memberId);
        } catch (Exception e) {
            throw new RemoteException("Error counting shares", e);
        }
    }

    @Override
    public ShareConfig getConfig() throws RemoteException {
        try {
            return configDao.getConfig();
        } catch (Exception e) {
            throw new RemoteException("Error fetching share config", e);
        }
    }

    @Override
    public void updateConfig(ShareConfig config) throws RemoteException {
        try {
            configDao.updateConfig(config);
        } catch (Exception e) {
            throw new RemoteException("Error updating share config", e);
        }
    }

    @Override
    public boolean isEligibleForLoan(String memberId) throws RemoteException {
        try {
            ShareConfig config = configDao.getConfig();
            int totalShares = shareDao.getTotalSharesByMember(memberId);
            return totalShares >= config.getMinimumSharesForLoan();
        } catch (Exception e) {
            throw new RemoteException("Error checking eligibility", e);
        }
    }
}
