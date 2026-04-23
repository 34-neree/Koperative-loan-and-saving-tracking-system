package dao;

import model.Notification;
import java.util.List;

public interface NotificationDao {

    void save(Notification notification);

    List<Notification> findByMember(String memberId);

    List<Notification> findAll();
}
